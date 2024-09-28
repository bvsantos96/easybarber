import { NavigationProp, RouteProp, useRoute } from '@react-navigation/native';
import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { EstablishmentDetail, ICategory } from '../declarations';
import { getStyles } from '../styles/EstablishmentDetails';
import { getEstablishmentDetails } from '../utils/ApiRequest';
import { defaultBarberImage } from '../utils/Constants';
import { gotoLocation } from '../utils/Location';
import { ImageWithRating } from '../components/ImageWithRating';
import { Underline } from '../components/Underline';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import Category from '../components/Category';
import { SvgUri } from 'react-native-svg';
import Divider from '../components/Divider';
import { Alert } from '../components/Alert';
import { ALERT_TYPE } from 'react-native-alert-notification';
import Button from '../components/Button';

export type Props = {
    navigation: NavigationProp<any, any>
}

type RouteParams = {
    establishment: { establishmentId: number };
};

export default function EstablishmentDetails({ navigation }: Props) {
    const texts = require("@lang/en.json");
    const styles = getStyles();

    const route = useRoute<RouteProp<RouteParams, 'establishment'>>();
    const { establishmentId } = route.params;
    const [categories, setCategories] = useState<ICategory[]>([]);
    const [establishment, setEstablishment] = useState<EstablishmentDetail>();

    useEffect(() => {
        const fetchEstablishment = async () => {
            const _establishment = await getEstablishmentDetails(establishmentId);
            if (_establishment === null || _establishment === undefined) {
                return;
            }
            let _categories: ICategory[] = [];
            if (_establishment?.availableServices) {
                let _cats: ICategory[] = await retrieveCategories();
                for (let cat of _cats) {
                    if (_establishment?.availableServices.includes(cat.id)) {
                        _categories.push(cat);
                    }
                }
            }
            setEstablishment(_establishment);
            setCategories(_categories);
        }
        fetchEstablishment();
    }, [establishmentId]);

    return (
        <View style={styles.container}>
            <View style={styles.imageStyle} >
                <ImageWithRating right rating={establishment?.rating.toFixed(1) ?? "0.0"} nvotes={establishment?.nvotes ?? 0} data={establishment?.images ? establishment.images[0]?.data : defaultBarberImage} />
            </View>
            <TouchableOpacity style={styles.nameContainer} onPress={() => { if (establishment) gotoLocation(establishment?.address, establishment?.latitude, establishment.longitude) }}>
                <Text style={styles.name}>{establishment?.name}</Text>
                <Text style={styles.address}>{establishment?.address}</Text>
            </TouchableOpacity>
            <View style={styles.serviceTitleContainer}>
                <Text style={styles.serviceTitle}>{texts.services.title}</Text>
                <Underline />
            </View>
            <View style={styles.servicesContainer}>
                {categories && categories.map((category) => (
                    <Category
                        padding={15}
                        style={{ marginHorizontal: 5 }}
                        key={category.id}
                        id={category.id}
                        icon={
                            <SvgUri width={styles.categoryIcon.width}
                                height={styles.categoryIcon.height}
                                style={styles.alignCenter}
                                uri={category.imageURL} />
                        }
                        title={category.name}
                        selectedCategory={category.id} />
                ))}
            </View>
            <View style={styles.aboutTitleContainer}>
                <Text style={styles.aboutTitle}>{texts.about.title}</Text>
                <Underline />
            </View>
            <Text style={styles.aboutText}>{establishment?.description}</Text>
            <View style={styles.button}>
                <Button
                    stylesInput={{ width: '100%' }}
                    onPress={
                        () => {
                            Alert({
                                type: ALERT_TYPE.INFO,
                                title: texts.updateRequired,
                                message: texts.updateRequiredMessage,
                                buttonText: 'checkAvailability'
                            });
                        }
                    } title={texts.checkAvailability} />
            </View>

        </View >
    );
}

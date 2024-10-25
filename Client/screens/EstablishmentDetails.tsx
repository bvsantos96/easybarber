import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Image } from 'expo-image';
import { useQuery } from '@tanstack/react-query';

import { getStyles } from '../styles/EstablishmentDetails';
import { getStyles as getListStyles } from '../styles/List';
import { getStyles as getSelectionStyles } from "../styles/Selection";

import { getEstablishmentCats, getEstablishmentServices, getImageList } from '../utils/ApiRequest';
import { gotoLocation } from '../utils/Location';
import { Underline } from '../components/Underline';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import Category from '../components/Category';
import { SvgUri } from 'react-native-svg';
import Button from '../components/Button';
import { ImageRating } from '../components/ImageRating';
import PageList from '../components/PageList';
import { defaultBarberImage } from '../utils/Constants';
import { PageListType } from '../enums';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { Params, Routes } from '@navigation/Router';

export type Route = EstablishmentInfo;

type Props = NativeStackScreenProps<typeof Params, 'EstablishmentDetails'>;

export default function EstablishmentDetails({ route, navigation }: Props) {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const selectionStyles = getSelectionStyles();
    const listStyles = getListStyles();
    const establishment: EstablishmentInfo = route.params;
    const [categories, setCategories] = useState<ICategory[]>([]);

    useQuery({
        queryKey: [`/establishment/${establishment?.id}/services/list`],
        queryFn: async () => await getEstablishmentServices(establishment.id),
        enabled: !!establishment?.id,
        networkMode: 'offlineFirst',
        staleTime: 60000,
    });


    useEffect(() => {
        const fetchEstablishmentServices = async () => {
            const establishmentCats = await getEstablishmentCats(establishment.id);
            let _categories: ICategory[] = [];
            if (establishmentCats) {
                let _cats: ICategory[] = await retrieveCategories();
                for (let cat of _cats) {
                    if (establishmentCats.includes(cat.id)) {
                        _categories.push(cat);
                    }
                }
            }
            setCategories(_categories);
        }

        fetchEstablishmentServices();
    }, [establishment.id]);

    return (
        <View style={selectionStyles.container}>
            <View style={styles.imageStyle} >
                <PageList<IImage>
                    type={PageListType.PAGERVIEW}
                    initialItems={establishment.images}
                    pageSize={4}
                    renderItem={
                        ({ item, index }: { item: IImage, index: number }) => {
                            return (
                                <Image
                                    key={index}
                                    cachePolicy="memory-disk"
                                    source={{ uri: (item.data || defaultBarberImage) }}
                                    style={listStyles.imageStyle}
                                />
                            )
                        }
                    }
                    requestFunction={(page: IPage<IImage>, params?: Record<string, string | number | boolean>) => getImageList(`establishment/${establishment.id}`, page, params)} />
                <ImageRating
                    rating={establishment.nvotes > 0 ? (establishment.sumVotes / establishment.nvotes).toFixed(1) : "0.0"}
                    nvotes={establishment?.nvotes ?? 0}
                    right
                />
            </View>
            <TouchableOpacity style={styles.nameContainer} onPress={() => { if (establishment) gotoLocation(establishment?.name, establishment?.address, establishment?.latitude, establishment.longitude) }}>
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
                        padding={styles.categoryPadding.padding}
                        style={{ marginHorizontal: styles.categoryPadding.margin }}
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
            <View style={selectionStyles.button}>
                <Button
                    stylesInput={{ width: '100%' }}
                    onPress={
                        () => {
                            navigation.navigate(Routes.ServiceSelection, { establishmentId: establishment.id });
                        }
                    } title={texts.appointments.book} />
            </View>

        </View >
    );
}

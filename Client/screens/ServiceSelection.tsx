import React, { useEffect, useState } from "react";
import { RouteProp, useRoute } from "@react-navigation/native";
import { View, Text, FlatList } from "react-native";

import { getEstablishmentServices } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import { Props } from "./EstablishmentDetails";
import SelectionItem from "../components/SelectionItems";
import Button from '../components/Button';
import { ALERT_TYPE } from "react-native-alert-notification";
import { Banner } from "../components/Alert";

type RouteParams = {
    establishment: { establishmentId: number }
};

export default function ServiceSelection({ navigation }: Props) {
    const styles = getStyles();
    const texts = require('@lang/en.json');
    const route = useRoute<RouteProp<RouteParams, 'establishment'>>();
    const { establishmentId } = route.params;
    const [services, setServices] = useState<ServiceInfo[]>();
    const [selected, setSelected] = useState<number | string>(0);

    useEffect(() => {
        const fetchService = async () => {
            let _services = await getEstablishmentServices(establishmentId);
            setServices(_services);
        }
        fetchService();
    }, [establishmentId]);

    return (
        <View style={styles.container} >
            <Text style={styles.selectTextContainer}>{texts.services.select}</Text>
            <View style={styles.listContainer}>
                {services && services.length > 0 && (
                    <FlatList
                        data={services || []}
                        renderItem={
                            ({ item }: { item: ServiceInfo }) =>
                                <SelectionItem key={item.id} image={item.image.data} selected={item.id == selected} onPress={() => { setSelected(item.id) }}>
                                    <View style={styles.textContainer}>
                                        <Text style={styles.title}>{item.name}</Text>
                                        <Text style={styles.description}>{item.description}</Text>
                                        <Text style={styles.price}>{texts.currency}{item.price.toFixed(2)}</Text>
                                    </View>
                                </SelectionItem>
                        }
                        keyExtractor={(item) => item.id.toString()}
                        showsVerticalScrollIndicator={false}
                        showsHorizontalScrollIndicator={false}
                    />
                )}
            </View>
            <View style={styles.button}>
                <Button
                    disabled={selected === 0}
                    stylesInput={{ width: '100%' }}
                    onPress={
                        () => {
                            navigation.navigate(texts.employees.title, { establishmentId: establishmentId, serviceId: selected });
                        }
                    } title={texts.appointments.selectService} />
            </View>
        </View>
    );
}

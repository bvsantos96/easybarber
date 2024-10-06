import React, { useEffect, useState } from "react";
import { RouteProp, useRoute } from "@react-navigation/native";
import { View, Text, FlatList } from "react-native";

import { getEstablishmentServices } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import { Props } from "./EstablishmentDetails";
import SelectionItem from "../components/SelectionItems";
import Selection from './Selection';

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
            setSelected(0);
            let _services = await getEstablishmentServices(establishmentId);
            setServices(_services);
            if (_services.length == 1) {
                setSelected(_services[0].id);
            }
        }
        fetchService();
    }, [establishmentId]);

    return (
        <Selection
            buttonText={texts.appointments.selectService}
            selectionText={texts.services.select}
            selected={selected !== 0}
            onButtonPress={
                () => {
                    navigation.navigate(texts.employees.title, { establishmentId: establishmentId, serviceId: selected });
                }
            }>
            <View style={styles.listContainer}>
                {services && services.length > 0 && (
                    <FlatList
                        data={services || []}
                        renderItem={
                            ({ item }: { item: ServiceInfo }) =>
                                <SelectionItem key={item.id} image={item.image.data} selected={item.id == selected} onPress={() => { setSelected(item.id) }}>
                                    <View style={styles.textContainer}>
                                        <View style={styles.titleAndSubTitleContainer}>
                                            <Text style={styles.title}>{item.name}</Text>
                                            <Text style={styles.description}>{item.description}</Text>
                                        </View>
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
        </Selection>
    );
}

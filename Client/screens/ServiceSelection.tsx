import React, { useEffect, useState } from "react";
import { RouteProp, useRoute } from "@react-navigation/native";
import { View, Text, FlatList } from "react-native";

import { ServiceInfo } from "../declarations";
import { getEstablishmentServices } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import { Props } from "./EstablishmentDetails";
import SelectionItem from "../components/SelectionItems";
import Button from '../components/Button';

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
            const size = _services.length;
            for (let i = 0; i < size; i++) {
                _services.push({ ..._services[i], id: i + 100 });
                _services.push({ ..._services[i], id: i + 150 });
                _services.push({ ..._services[i], id: i + 170 });
            }
            setServices(_services);
        }
        fetchService();
    }, [establishmentId]);

    return (
        <View style={styles.container} >
            <Text style={styles.selectTextContainer}>{texts.services.select}</Text>
            <View style={styles.listContainer}>
                <FlatList
                    data={services || []}
                    style={styles.flatList}
                    renderItem={
                        ({ item }: { item: ServiceInfo }) =>
                            <SelectionItem key={item.id} service={item} selected={item.id == selected} onPress={() => { setSelected(item.id) }}>
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
            </View>
            <View style={styles.button}>
                <Button
                    stylesInput={{ width: '100%' }}
                    onPress={
                        () => {
                            alert("Goto select employee");
                        }
                    } title={texts.appointments.book} />
            </View>
        </View>
    );
}

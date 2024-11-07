import React, { useState } from "react";
import { View, Text, FlatList } from "react-native";
import { useQuery } from "@tanstack/react-query";

import { getEstablishmentServiceEmployees, getEstablishmentServices } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import SelectionItem from "../components/SelectionItems";
import Selection from './Selection';
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Params, Routes } from "@navigation/Router";
import texts from "@lang/en.json";
import Divider from "@components/Divider";

export type Route = {
    establishmentId: number
};

type Props = NativeStackScreenProps<typeof Params, 'ServiceSelection'>;

export default function ServiceSelection({ route, navigation }: Props) {
    const styles = getStyles();
    const { establishmentId } = route.params;
    const [selected, setSelected] = useState<number | string>(0);
    const [topPadding, setTopPadding] = useState(0);
    const { data } = useQuery({
        queryKey: [`/establishment/${establishmentId}/services/list`],
        queryFn: async () => await getEstablishmentServices(establishmentId),
        networkMode: 'offlineFirst',
        staleTime: 60000
    });

    useQuery({
        queryKey: [`establishment/${establishmentId}/service/${selected}/employees`, selected],
        queryFn: async () => getEstablishmentServiceEmployees(establishmentId, Number.parseInt(`${selected}`)),
        enabled: (!!selected && selected !== 0 && selected !== ''),
        networkMode: 'offlineFirst',
        staleTime: 60000,
    });

    return (
        <Selection
            setTopPadding={setTopPadding}
            buttonText={texts.appointments.selectService}
            selectionText={texts.services.select}
            selected={selected !== 0}
            onButtonPress={
                () => {
                    navigation.navigate(Routes.EmployeeSelection, { establishmentId: establishmentId, serviceId: selected as number });
                }
            }>
            <View style={styles.listContainer}>
                <Divider size={topPadding} />
                {data && data.length > 0 && (
                    <FlatList
                        data={data || []}
                        renderItem={
                            ({ item }: { item: ServiceInfo }) =>
                                <SelectionItem key={item.id} image={item.image?.data} selected={item.id == selected} onPress={() => { setSelected(item.id) }}>
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

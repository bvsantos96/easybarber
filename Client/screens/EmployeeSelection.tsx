import React, { useEffect, useState } from "react";
import { RouteProp, useRoute } from "@react-navigation/native";
import { View, Text, FlatList } from "react-native";

import { getEstablishmentServiceEmployees } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import { getStyles as getSelectionStyles } from "../styles/Selection";
import { Props } from "./EstablishmentDetails";
import SelectionItem from "../components/SelectionItems";
import Button from '../components/Button';
import { ALERT_TYPE } from "react-native-alert-notification";
import { Banner } from "../components/Alert";
import Selection from "./Selection";

type RouteParams = {
    appointment: {
        establishmentId: number;
        serviceId: number;
    };
};

export default function EmployeeSelection({ navigation }: Props) {
    const texts = require('@lang/en.json');
    const styles = getStyles();
    const route = useRoute<RouteProp<RouteParams, 'appointment'>>();
    const { establishmentId, serviceId } = route.params;
    const [employees, setEmployees] = useState<ImageEntity[]>();
    const [selected, setSelected] = useState<number | string>(0);

    useEffect(() => {
        const fetchEmployees = async () => {
            let _employees = await getEstablishmentServiceEmployees(establishmentId, serviceId);
            setEmployees(_employees);
        }
        fetchEmployees();
    }, [establishmentId]);

    return (
        <Selection
            buttonText={texts.appointments.schedule}
            selectionText={texts.employees.select}
            selected={true}
            onButtonPress={
                () => {
                    navigation.navigate(texts.appointments.schedule, { establishmentId, serviceId, selected });
                }
            }>
            <View style={styles.listContainer}>
                {employees && employees.length > 0 && (
                    <FlatList
                        data={employees || []}
                        renderItem={
                            ({ item }: { item: ImageEntity }) =>
                                <SelectionItem
                                    key={item.id}
                                    image={item.image}
                                    selected={item.id == selected}
                                    onPress={() => {
                                        if (selected == item.id) {
                                            setSelected(0);
                                            return;
                                        }
                                        setSelected(item.id);
                                    }}>
                                    <View style={styles.titleContainer}>
                                        <View style={{
                                            justifyContent: 'center',
                                            height: styles.titleContainer.height
                                        }}>
                                            <Text style={styles.singleTitle}>{item.name}</Text>
                                        </View>
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

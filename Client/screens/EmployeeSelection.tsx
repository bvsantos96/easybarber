import React, { useEffect, useState } from "react";
import { RouteProp, useRoute } from "@react-navigation/native";
import { View, Text, FlatList } from "react-native";

import { getEstablishmentServiceEmployees } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import { Props } from "./EstablishmentDetails";
import SelectionItem from "../components/SelectionItems";
import Button from '../components/Button';
import { ALERT_TYPE } from "react-native-alert-notification";
import { Banner } from "../components/Alert";

type AppointmentParams = {
    establishmentId: number;
    serviceId: number;
};

type RouteParams = {
    appointment: AppointmentParams;
};

export default function EmployeeSelection({ navigation }: Props) {
    const styles = getStyles();
    const texts = require('@lang/en.json');
    const route = useRoute<RouteProp<RouteParams>>();
    const { establishmentId, serviceId } = route.params.appointment;
    const [employees, setEmployees] = useState<ImageEntity[]>();
    const [selected, setSelected] = useState<number | string>(0);

    useEffect(() => {
        const fetchEmployees = async () => {
            console.log(establishmentId, serviceId);
            let _employees = await getEstablishmentServiceEmployees(establishmentId, serviceId);
            setEmployees(_employees);
        }
        fetchEmployees();
    }, [establishmentId]);

    return (
        <View style={styles.container} >
            <Text style={styles.selectTextContainer}>{texts.employees.select}</Text>
            <View style={styles.listContainer}>
                {employees && employees.length > 0 && (
                    <FlatList
                        data={employees || []}
                        renderItem={
                            ({ item }: { item: ImageEntity }) =>
                                <SelectionItem key={item.id} image={item.image} selected={item.id == selected} onPress={() => { setSelected(item.id) }}>
                                    <View style={styles.textContainer}>
                                        <Text style={styles.title}>{item.name}</Text>
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
                            Banner({ type: ALERT_TYPE.SUCCESS, message: `Service ${selected} selected` });
                        }
                    } title={texts.appointments.schedule} />
            </View>
        </View>
    );
}

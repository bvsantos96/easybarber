import React, { useEffect, useState } from "react";
import { RouteProp, useRoute } from "@react-navigation/native";
import { View, Text, FlatList } from "react-native";

import { getEstablishmentServiceEmployees, setAppointment } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import { Props } from "./EstablishmentDetails";
import SelectionItem from "../components/SelectionItems";
import Selection from "./Selection";
import { AlertType } from "@components/Alert";
import useAlertStore from "storage/stores/AlertStore";
import texts from "@lang/en.json";

type RouteParams = {
    appointment: {
        establishmentId: number;
        serviceId: number;
        date?: string;
        startHour?: string;
        availableEmployees?: number[];
    };
};

export default function EmployeeSelection({ navigation }: Props) {
    const styles = getStyles();
    const route = useRoute<RouteProp<RouteParams, 'appointment'>>();
    const { establishmentId, serviceId, date, startHour, availableEmployees } = route.params;
    const [employees, setEmployees] = useState<ImageEntity[]>();
    const [selected, setSelected] = useState<number | string>(0);
    const { alert } = useAlertStore();

    useEffect(() => {
        const fetchEmployees = async () => {
            setSelected(0);
            let _employees = await getEstablishmentServiceEmployees(establishmentId, serviceId);
            setEmployees(_employees);
            if (_employees.length == 1) {
                setSelected(_employees[0].id);
            }
        }
        if (!availableEmployees || availableEmployees.length == 0) {
            fetchEmployees();
        }
        fetchEmployees();
    }, [establishmentId]);

    return (
        <Selection
            buttonText={texts.appointments.schedule}
            selectionText={(date && startHour) ? texts.employees.selectNot : texts.employees.select}
            selected={(date && startHour) ? selected != 0 : true}
            onButtonPress={
                async () => {
                    if (date && startHour && selected != 0) {
                        if (await setAppointment({
                            id: 0,
                            establishmentId,
                            establishmentServiceId: serviceId,
                            establishmentStaffId: Number.parseInt(`${selected}`),
                            date,
                            time: startHour
                        })) {
                            alert({
                                type: AlertType.Success, message: texts.appointments.success, buttonText: texts.appointments.seeAppointments, onPress: () => {
                                    navigation.reset({
                                        index: 0,
                                        routes: [{ name: 'Appointments' }],
                                    });
                                }
                            });
                            return;
                        }
                    } else if (!(date && startHour)) {
                        navigation.navigate(texts.appointments.schedule, { establishmentId, serviceId, employeeId: selected });
                    }
                }
            }>
            <View style={styles.listContainer}>
                {employees && employees.length > 0 && (
                    <FlatList
                        data={employees.filter((e) => {
                            if (availableEmployees == undefined) return true;
                            availableEmployees.includes(Number.parseInt("" + e.id))
                        }) || []}
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
        </Selection >
    );
}

import React, { useState } from "react";
import { View, Text, FlatList } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

import { getEstablishmentServiceEmployees, getNowHourAndMinutes, getStartingHour, getToken, getUnavailableDates, setAppointment } from "../utils/ApiRequest";
import { getStyles } from "../styles/ServiceSelection";
import SelectionItem from "../components/SelectionItems";
import Selection from "./Selection";
import { AlertType } from "@components/Alert";
import useAlertStore from "storage/stores/AlertStore";
import texts from "@lang/en.json";
import { useQuery } from "@tanstack/react-query";
import { Params, Routes } from "@navigation/Router";
import Divider from "@components/Divider";
import { buildCurrencyString } from "utils/Utils";

export type Route = {
    establishmentId: number;
    serviceId: number;
    date?: string;
    startHour?: string;
    availableEmployees?: number[];
};

type Props = NativeStackScreenProps<typeof Params, 'EmployeeSelection'>;

export default function EmployeeSelection({ navigation, route }: Props) {
    const styles = getStyles();
    const { establishmentId, serviceId, date, startHour, availableEmployees } = route.params;
    const [selected, setSelected] = useState<number | string>(0);
    const [topPadding, setTopPadding] = useState(0);
    const { alert } = useAlertStore();
    const { data } = useQuery({
        queryKey: [`establishment/${establishmentId}/service/${serviceId}/employees`, serviceId, startHour, date],
        queryFn: async () => {
            return getEstablishmentServiceEmployees(establishmentId, serviceId, date, startHour)
        },
        enabled: !!(establishmentId) && !!(serviceId) && serviceId != 0 && (availableEmployees == undefined || availableEmployees == null || availableEmployees.length == 0),
        networkMode: 'offlineFirst',
        staleTime: 60000
    });

    const today = new Date();
    const month = today.getMonth() + 1;
    const year = today.getFullYear();

    useQuery({
        queryKey: [`getUnavailableDates`, establishmentId, serviceId, selected, year, month],
        queryFn: async () =>
            await getUnavailableDates(establishmentId, serviceId, +selected, year, month, getStartingHour(new Date(), getNowHourAndMinutes())),
        enabled: !!(establishmentId) && !!(serviceId) && serviceId != 0 && (availableEmployees == undefined || availableEmployees == null || availableEmployees.length == 0),
        networkMode: 'offlineFirst',
        staleTime: 60000
    });

    return (
        <Selection
            setTopPadding={setTopPadding}
            buttonText={texts.appointments.schedule}
            selectionText={(date && startHour) ? texts.employees.selectNot : texts.employees.select}
            selected={(date && startHour) ? selected != 0 : true}
            onButtonPress={
                async () => {
                    if (date && startHour && selected != 0) {
                        if (await getToken() === null) {
                            alert({
                                type: AlertType.Error,
                                message: texts.login.required,
                                buttonText: texts.login.signIn,
                                onPress: () => {
                                    navigation.navigate(Routes.Sign);
                                },
                                buttonText2: texts.dismiss,
                            });

                            return;
                        }
                        alert({ type: AlertType.Loading, message: "" });
                        const msg = await setAppointment({
                            id: 0,
                            establishmentId,
                            establishmentServiceId: serviceId,
                            establishmentStaffId: +selected,
                            date,
                            time: startHour
                        });
                        if (msg.length === 0) {
                            alert({ type: AlertType.Loading, message: "" });
                            alert({
                                type: AlertType.Success, message: texts.appointments.success,
                                buttonText: texts.dismiss, onPress: () => {
                                    navigation.navigate(Routes.Appointments);
                                }
                            });
                            return;
                        } else {
                            alert({ type: AlertType.Loading, message: "" });
                            alert({
                                type: AlertType.Error, message: msg,
                                buttonText: texts.dismiss, onPress: () => { }
                            });
                        }
                    } else if (!(date && startHour)) {
                        navigation.navigate(Routes.Availability, { establishmentId, serviceId, employeeId: selected as number });
                    }
                }
            }>
            <View style={styles.listContainer}>
                <Divider size={topPadding} horizontal={false} />
                {data && data.length > 0 && (
                    <FlatList
                        data={data.filter((e) => {
                            if (availableEmployees == undefined) return true;
                            return availableEmployees.includes(+e.id);
                        }) || []}
                        renderItem={
                            ({ item }: { item: EmployeeEntity }) =>
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
                                            <Text style={styles.description}>{`${buildCurrencyString(item.price)}`}</Text>
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

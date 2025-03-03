import React, { useEffect, useRef, useState } from "react";
import { View, Text, FlatList } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

import { getEstablishmentEmployees, getEstablishmentServices, getToken, setAppointment } from "../utils/ApiRequest";
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
import Input from "@components/Input";

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
    const name = useRef<string>("");
    const { alert, setAlertVisible } = useAlertStore();

    const { data } = useQuery({
        queryKey: [`establishment/${establishmentId}/employees`, establishmentId],
        queryFn: async () => {
            const employees = await getEstablishmentEmployees(establishmentId)
            if (employees) {
                let me: EmployeeEntity | undefined = undefined;
                let list: EmployeeEntity[] = [];
                for (let i = 0; i < employees.length; i++) {
                    const em = employees[i];
                    if (em.me && !me) {
                        me = em;
                        setSelected(me.id);
                    } else {
                        list.push(em);
                    }
                }
                if (me) {
                    list.unshift(me);
                }
                return employees;
            }
            return employees;
        },
        enabled: !!(establishmentId),
        networkMode: 'offlineFirst',
        staleTime: 60000
    });

    useQuery({
        queryKey: [`/establishment/${establishmentId}/services/list`, selected],
        queryFn: async () => {
            if (establishmentId)
                return await getEstablishmentServices(establishmentId, +selected);
        },
        enabled: (!!establishmentId),
        networkMode: 'offlineFirst',
        staleTime: 60000
    });

    useEffect(() => {
        if (data && data.length > 0) {
            if (data[0].me) {
                setSelected(data[0].id);
            }
            if (data.length === 1) {
                setSelected(data[0].id);
                navigation.navigate(Routes.ServiceSelection, { establishmentId, employeeId: +data[0].id });
            }
        }
    }, [data]);

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

                        alert({
                            type: AlertType.GetInfo,
                            message: texts.fillUserInfo,
                            onPress: async () => {
                                if ((name && name.current && name.current.length > 0)) {
                                    alert({ type: AlertType.Loading, message: "" });
                                    const msg = await setAppointment({
                                        id: 0,
                                        establishmentId,
                                        establishmentServiceId: serviceId,
                                        establishmentStaffId: +selected,
                                        date,
                                        time: startHour,
                                        nonRegisteredUser: name.current,
                                    });
                                    if (msg.length === 0) {
                                        setAlertVisible(false);
                                        alert({
                                            type: AlertType.Success, message: texts.appointments.success,
                                            buttonText: texts.dismiss, onPress: () => {
                                                navigation.navigate(Routes.Appointments);
                                            }
                                        });
                                        return;
                                    } else {
                                        setAlertVisible(false);
                                        alert({
                                            type: AlertType.Error, message: msg,
                                            buttonText: texts.dismiss, onPress: () => { }
                                        });
                                    }
                                }
                            },
                            inputs: [
                                <Input
                                    containerStyle={styles.alertInput}
                                    placeholder={texts.name}
                                    hideTitleIfNoValue
                                    title={texts.name}
                                    onInputChange={(text) => { name.current = text; }}
                                />
                            ],
                            buttonText: texts.save,
                            onPress2: () => { },
                            buttonText2: texts.dismiss
                        });
                    } else if (!(date && startHour)) {
                        navigation.navigate(Routes.ServiceSelection, { establishmentId, employeeId: selected as number });
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
                                            <View style={{ flexDirection: 'row' }}>
                                                {item.oldPrice && item.oldPrice > 0 && (
                                                    <Text style={[styles.description, { textDecorationLine: "line-through" }]}>{`${buildCurrencyString(item.oldPrice)} `}</Text>
                                                )}
                                                <Text style={styles.description}>{`${buildCurrencyString(item.price)}`}</Text>
                                            </View>
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

// Libraries
import React, { useRef, useState } from 'react';
import { View, Linking } from 'react-native';
import Fontisto from '@expo/vector-icons/Fontisto';

// Requests
import { getEmployees } from 'utils/ApiRequest';

// Components
import PageList, { PageListRef } from '@components/PageList';
import Pressable from '@components/Pressable';
import SlidingItem from '@components/SlidingItem';

// Styles
import { getStyles } from '@styles/Employees';
import { useTheme } from '@styles/ThemeContext';
import useAlertStore from 'storage/stores/AlertStore';
import { AlertType } from '@components/Alert';

// Texts
import texts from '@lang/en.json';
import { Params, Routes } from '@navigation/Router';
import { NavigationProp } from '@react-navigation/native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import useEstablishmentStore from 'storage/stores/EstablishmentStore';

const renderEmployee = ({ item, navigation }: { item: EmployeeInfo, navigation: NavigationProp<any, any> }) => {
    const styles = getStyles();
    const theme = useTheme();
    const { alert } = useAlertStore();

    const fireEmployee = async (id: number) => {
        // await fireEmployee(id);
        console.log('Fired employee with id:', id);
    }

    const call = async () => {
        Linking.openURL(`tel:${item.mobileNumber}`).catch((err) =>
            console.error('Error opening dialer:', err)
        );
    }

    return (
        <SlidingItem
            key={item.id}
            items={[
                <Pressable onPress={() => navigation.navigate(Routes.Schedules, { employeeId: item.id })} style={[styles.icon]} >
                    <Fontisto name="calendar" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                </Pressable>,
                <Pressable onPress={call} style={[styles.icon]} >
                    <Fontisto name="phone" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                </Pressable>,
                <Pressable onPress={async () => {
                    alert({
                        type: AlertType.Error,
                        message: texts.employee.fire,
                        buttonText: texts.yes,
                        onPress: async () => {
                            await fireEmployee(+item.id);
                        },
                        onPress2: () => { },
                        buttonText2: texts.no
                    });
                }} style={[styles.icon, styles.redIcon]} >
                    <Fontisto name="trash" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                </Pressable>
            ]}
        >
            <View style={styles.listItemContainer}>
            </View>
        </SlidingItem>
    );
}

export default function Employees({ navigation }: PropNavigation) {
    const { selectedEstablishment } = useEstablishmentStore();
    const styles = getStyles();
    const [resetList, setResetList] = useState(false);
    const pageListRef = useRef<PageListRef<EmployeeInfo>>(null);

    const loadEmployees = async (page?: IPage<EmployeeInfo>, params?: EmployeeFilter) => {
        if (!selectedEstablishment) return;
        params = { ...params, establishmentIds: [+selectedEstablishment.id] };
        return await getEmployees(page, params);
    }

    return (
        <View style={styles.container}>
            <View style={styles.listContainer}>
                <PageList<EmployeeInfo>
                    reset={resetList}
                    ref={pageListRef}
                    renderItem={({ item }) => renderEmployee({ item, navigation })}
                    requestFunction={loadEmployees}
                />
            </View>
        </View>
    );
}

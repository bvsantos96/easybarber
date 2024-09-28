import { NavigationProp, RouteProp, useRoute } from '@react-navigation/native';
import React, { useEffect, useState } from 'react';
import { Button, View, Text } from 'react-native';
import { EmployeeInfo } from '../declarations';
import { getStyles } from '../styles/EmployeeDetails';

export type Props = {
    navigation: NavigationProp<any, any>
}

type RouteParams = {
    employee: { employeeId: number };
};

export default function EmployeeDetails({ navigation }: Props) {
    const texts = require("@lang/en.json");
    const styles = getStyles();

    const route = useRoute<RouteProp<RouteParams, 'employee'>>();
    const { employeeId } = route.params;

    const [employee, setEmployee] = useState<EmployeeInfo>();

    useEffect(() => {
        const fetchEmployee = async () => {
            const employee = await getEmployee(employeeId);
            setEmployee(employee);
        }
        fetchEmployee();
    }, [employeeId]);

    return (
        <View style={styles.container}>
            <Text style={styles.title}>{employee?.name}</Text>
            <Button title="Go to Home" onPress={() => navigation.navigate(texts.tabs.home)} />
        </View>
    );
}

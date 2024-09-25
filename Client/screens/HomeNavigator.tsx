import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Home from './Home';
import EmployeeDetails from './EmployeeDetails';

export default function HomeNavigator() {
    const texts = require('@lang/en.json');
    const Stack = createNativeStackNavigator();
    return (
        <Stack.Navigator initialRouteName='Home'>
            <Stack.Screen name={texts.tabs.home} options={{ headerShown: false }} >
                {props => <Home {...props} />}
            </Stack.Screen>
            <Stack.Screen name={texts.tabs.employeeDetails} component={EmployeeDetails} />
        </Stack.Navigator>
    );
}

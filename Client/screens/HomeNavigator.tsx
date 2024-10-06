import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Entypo from '@expo/vector-icons/Entypo';

import Home from './Home';
import ServiceSelection from './ServiceSelection';
import EstablishmentDetails from './EstablishmentDetails';
import { getStyles } from '../styles/HomeNavigator';
import EmployeeSelection from './EmployeeSelection';


export const Header = ({ navigation, title }) => {
    const styles = getStyles();
    return (
        <View style={styles.header}>
            <View style={styles.headerContainer}>
                <TouchableOpacity style={styles.goBack} onPress={() => navigation.goBack()}>
                    <Entypo name="chevron-small-left" size={styles.goBackIcon.width} color="black" />
                </TouchableOpacity>
                <Text style={styles.headerTitle}>{title}</Text>
                <View style={styles.headerFiller} />
            </View>
        </View>
    )
};


export default function HomeNavigator() {
    const styles = getStyles();
    const texts = require('@lang/en.json');
    const Stack = createNativeStackNavigator();
    return (
        <View style={styles.container}>
            <Stack.Navigator initialRouteName={texts.tabs.back} >
                <Stack.Screen name={texts.tabs.back} options={{
                    headerShown: false
                }} >
                    {props => <Home {...props} />}
                </Stack.Screen>
                <Stack.Screen
                    options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={texts.tabs.establishmentDetails} />
                        ),
                    }}
                    name={texts.tabs.establishmentDetails} component={EstablishmentDetails} />
                <Stack.Screen
                    options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={texts.services.title} />
                        ),
                    }}
                    name={texts.services.title} component={ServiceSelection} />
                <Stack.Screen
                    options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={texts.employees.title} />
                        ),
                    }}
                    name={texts.employees.title} component={EmployeeSelection} />
            </Stack.Navigator>
        </View>
    );
}

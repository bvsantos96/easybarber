import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Entypo from '@expo/vector-icons/Entypo';

import texts from '@lang/en.json';
import Home from './Home';
import ServiceSelection from './ServiceSelection';
import EstablishmentDetails from './EstablishmentDetails';
import { getStyles } from '../styles/HomeNavigator';
import EmployeeSelection from './EmployeeSelection';
import Availability from './Availability';
import { NavigationProp } from '@react-navigation/native';
import Settings from './Settings';

interface HeaderProps {
    navigation: NavigationProp<any, any>
    title: string;
}

export const Header = ({ navigation, title }: HeaderProps) => {
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
    const Stack = createNativeStackNavigator();
    return (
        <View style={styles.container}>
            <Stack.Navigator initialRouteName={texts.tabs.back} >
                <Stack.Screen name={texts.tabs.back} options={{
                    headerShown: false
                }} component={Home} />
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
                <Stack.Screen
                    options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={texts.appointments.schedule} />
                        ),
                    }}
                    name={texts.appointments.schedule} component={Availability} />
                <Stack.Screen
                    options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={texts.settings.title} />
                        ),
                    }}
                    name={texts.settings.title} component={Settings} />
            </Stack.Navigator>
        </View>
    );
}

import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Entypo from '@expo/vector-icons/Entypo';

import Home from './Home';
import EstablishmentDetails from './EstablishmentDetails';
import { getStyles } from '../styles/HomeNavigator';


export default function HomeNavigator() {
    const texts = require('@lang/en.json');
    const Stack = createNativeStackNavigator();
    const styles = getStyles();
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
                            <View style={styles.header}>
                                <View style={styles.headerContainer}>
                                    <TouchableOpacity style={styles.goBack} onPress={() => navigation.goBack()}>
                                        <Entypo name="chevron-small-left" size={styles.goBackIcon.width} color="black" />
                                    </TouchableOpacity>
                                    <Text style={styles.headerTitle}>{texts.tabs.establishmentDetails}</Text>
                                    <View style={styles.headerFiller} />
                                </View>
                            </View>
                        ),
                    }}
                    name={texts.tabs.establishmentDetails} component={EstablishmentDetails} />
            </Stack.Navigator>
        </View>
    );
}

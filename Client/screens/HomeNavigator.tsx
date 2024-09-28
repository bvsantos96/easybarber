import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Home from './Home';
import EstablishmentDetails from './EstablishmentDetails';
import { View } from 'react-native';
import { getStyles } from '../styles/HomeNavigator';

export default function HomeNavigator() {
    const texts = require('@lang/en.json');
    const Stack = createNativeStackNavigator();
    const styles = getStyles();
    return (
        <View style={styles.container}>
            <Stack.Navigator initialRouteName={texts.tabs.back} >
                <Stack.Screen name={texts.tabs.back} options={{ headerShown: false }} >
                    {props => <Home {...props} />}
                </Stack.Screen>
                <Stack.Screen name={texts.tabs.establishmentDetails} component={EstablishmentDetails} />
            </Stack.Navigator>
        </View>
    );
}

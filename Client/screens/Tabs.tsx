import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Appointments from './Appointments';
import HomeIcon from "@assets/icons/home.svg";
import AppointmentsIcon from "@assets/icons/appointments.svg";
import { useTheme } from '../styles/ThemeContext';
import SafeFullScreen from '../components/SafeFullScreen';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import HomeNavigator from './HomeNavigator';

export default function Tabs() {
    const Tab = createBottomTabNavigator();
    const texts = require('../langs/en.json');
    const theme = useTheme();
    const inserts = useSafeAreaInsets();

    return (
        <Tab.Navigator
            initialRouteName={texts.tabs.home}
            screenOptions={{
                lazy: true,
                tabBarActiveTintColor: theme.colors.mainColor,
                tabBarInactiveTintColor: theme.colors.text.main,
                tabBarStyle: [{
                    height: theme.dimensions.tabHeight + inserts.bottom,
                }, theme.shadow],
            }} >
            <Tab.Screen name={texts.tabs.home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <HomeIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                    ) : (
                        <HomeIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                    )
                }} >
                {() => (<SafeFullScreen><HomeNavigator /></SafeFullScreen>)}
            </Tab.Screen>
            <Tab.Screen name={texts.tabs.appointments}
                options={{
                    tabBarIcon: ({ focused }) => !focused ? (
                        <AppointmentsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                    ) : (
                        <AppointmentsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                    ),
                    headerTitleAlign: 'center',
                    headerShadowVisible: true,
                    headerStyle: {
                        elevation: 8,
                        shadowColor: '#000',
                        shadowOffset: {
                            width: 5,
                            height: 5,
                        },
                        shadowOpacity: 0.1,
                        shadowRadius: 5,
                    }
                }
                } >
                {() => (<SafeFullScreen><Appointments /></SafeFullScreen>)}
            </Tab.Screen>
        </Tab.Navigator>
    );
}

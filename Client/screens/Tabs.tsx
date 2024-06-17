import React from 'react';
import Home from './Home';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Appointments from './Appointments';
import HomeIcon from "@assets/icons/home.svg";
import AppointmentsIcon from "@assets/icons/appointments.svg";
import AlertsIcon from "@assets/icons/alerts.svg";
import ChatsIcon from "@assets/icons/chats.svg";
import { useTheme } from '../styles/ThemeContext';
import SafeFullScreen from '../components/SafeFullScreen';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

export default function Tabs() {
    const Tab = createBottomTabNavigator();
    const texts = require('../langs/en.json');
    const theme = useTheme();
    const inserts = useSafeAreaInsets();

    return (
        <Tab.Navigator screenOptions={{
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
                {() => (<SafeFullScreen><Home /></SafeFullScreen>)}
            </Tab.Screen>
            <Tab.Screen name={texts.tabs.appointments}
                options={{
                    tabBarIcon: ({ focused }) => !focused ? (
                        <AppointmentsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                    ) : (
                        <AppointmentsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                    ),
                    headerTitleAlign: 'center'
                }
                } >
                {() => (<SafeFullScreen><Appointments /></SafeFullScreen>)}
            </Tab.Screen>
            <Tab.Screen name={texts.tabs.alerts} component={Home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <AlertsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                    ) : (
                        <AlertsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                    )
                }} />
            <Tab.Screen name={texts.tabs.chats} component={Home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <ChatsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                    ) : (
                        <ChatsIcon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                    )
                }} />
        </Tab.Navigator>
    );
}

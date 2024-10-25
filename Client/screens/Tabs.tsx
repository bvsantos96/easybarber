import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useTheme } from '../styles/ThemeContext';
import SafeFullScreen from '../components/SafeFullScreen';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import TabsNav from '@navigation/TabsNavigator';
import { Params } from '@navigation/Router';

export default function Tabs() {
    const Tab = createBottomTabNavigator<typeof Params>();
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
            {TabsNav && Object.keys(TabsNav).map((key) => {
                const _key = key as keyof typeof TabsNav;
                const tab = TabsNav[_key];
                if (!tab) return null;
                return (
                    <Tab.Screen
                        key={_key}
                        name={_key}
                        navigationKey={_key}
                        options={{
                            ...tab.hasHeader ? {
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
                            } : { headerShown: false },
                            tabBarIcon: ({ focused }) => !focused ? (
                                <tab.tabicon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                            ) : (
                                <tab.tabicon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                            ),
                            tabBarLabel: tab.title,
                        }} >
                        {() => (<SafeFullScreen><tab.component /></SafeFullScreen>)}
                    </Tab.Screen>
                )
            })}
        </Tab.Navigator>
    );
}

import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Entypo from '@expo/vector-icons/Entypo';

import { getStyles } from '../styles/HomeNavigator';
import { NavigationProp } from '@react-navigation/native';
import HomeNav from '@navigation/HomeNavigator';
import { Params, Routes } from '@navigation/Router';

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
    const Stack = createNativeStackNavigator<typeof Params>();

    return (
        <View style={styles.container}>
            <Stack.Navigator initialRouteName={Routes.Home} >
                {HomeNav && Object.keys(HomeNav).map((key) => {
                    const _key = key as keyof typeof Params;
                    const nav = HomeNav[_key];
                    if (!nav) return null;
                    return (
                        <Stack.Screen
                            key={_key}
                            name={_key}
                            options={nav.hasHeader ?
                                {
                                    header: ({ navigation }) => (
                                        <Header navigation={navigation} title={nav.title} />
                                    ),
                                }
                                :
                                {
                                    headerShown: false
                                }
                            }
                        >
                            {props => <nav.component {...props} />}
                        </Stack.Screen>
                    );
                })}
            </Stack.Navigator>
        </View>
    );
}

import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Entypo from '@expo/vector-icons/Entypo';

import { getStyles } from '../styles/HomeNavigator';
import { NavigationProp } from '@react-navigation/native';
import HomeNav from '@navigation/HomeNavigator';
import { Params, Routes } from '@navigation/Router';
import { Props as SecondHeaderProps } from '@components/FavoriteHeader';
import { SetSelectedRef } from './EstablishmentDetails';

type HeaderProps = {
    navigation: NavigationProp<any, any>;
    title: string;
    secondHeader?: React.FC<SecondHeaderProps>;
    secondHeaderFunction?: (selected: boolean) => Promise<boolean>;
    selected?: boolean;
}

export const Header = ({ navigation, title, secondHeader, secondHeaderFunction, selected }: HeaderProps) => {
    const styles = getStyles();
    return (
        <View style={styles.header}>
            <View style={styles.headerContainer}>
                <TouchableOpacity style={styles.goBack} onPress={() => navigation.goBack()}>
                    <Entypo name="chevron-small-left" size={styles.goBackIcon.width} color="black" />
                </TouchableOpacity>
                <Text style={styles.headerTitle}>{title}</Text>
                <View style={styles.headerFiller} />
                {secondHeader && React.createElement(secondHeader, {
                    selected: selected ?? false,
                    setSelected: async (_selected: boolean) => {
                        secondHeaderFunction && await secondHeaderFunction(_selected);
                    }
                })}
            </View>
        </View>
    )
};


export default function HomeNavigator() {
    const styles = getStyles();
    const Stack = createNativeStackNavigator<typeof Params>();
    const ref = React.useRef<SetSelectedRef>(null);
    const [favorite, setFavorite] = React.useState(false);

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
                                        <Header navigation={navigation} title={nav.title} secondHeader={nav.secondHeader} secondHeaderFunction={ref?.current?.setSelected} selected={favorite} />
                                    ),
                                }
                                :
                                {
                                    headerShown: false
                                }
                            }
                        >
                            {(props) => {
                                switch (_key) {
                                    case Routes.EstablishmentDetails:
                                        return <nav.component {...props} ref={ref} setFavorite={setFavorite} />;
                                    default:
                                        return <nav.component {...props} />;
                                }
                            }}
                        </Stack.Screen>
                    );
                })}
            </Stack.Navigator>
        </View>
    );
}

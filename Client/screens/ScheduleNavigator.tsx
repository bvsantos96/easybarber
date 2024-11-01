import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Entypo from '@expo/vector-icons/Entypo';

import { getStyles } from '../styles/HomeNavigator';
import { NavigationProp } from '@react-navigation/native';
import { Params, Routes } from '@navigation/Router';
import { Props as SecondHeaderProps } from '@components/FavoriteHeader';
import { SetSelectedRef } from './EstablishmentDetails';

type HeaderProps = {
    navigation: NavigationProp<any, any>;
    title: string;
    hasGoBack?: boolean;
    secondHeader?: React.FC<SecondHeaderProps>;
    secondHeaderFunction?: (selected: boolean) => Promise<boolean>;
    selected?: boolean;
}

export const Header = ({ navigation, title, hasGoBack = true, secondHeader, secondHeaderFunction, selected }: HeaderProps) => {
    const styles = getStyles();
    return (
        <View style={styles.header}>
            <View style={styles.headerContainer}>
                {hasGoBack ? (
                    <TouchableOpacity style={styles.goBack} onPress={() => {
                        navigation.goBack();
                    }}>
                        <Entypo name="chevron-small-left" size={styles.goBackIcon.width} color="black" />
                    </TouchableOpacity>
                ) : (
                    <View style={styles.headerFiller} />
                )}
                <Text style={styles.headerTitle}>{title}</Text>
                {secondHeader ? (
                    React.createElement(secondHeader, {
                        selected: selected ?? false,
                        setSelected: async (_selected: boolean) => {
                            secondHeaderFunction && await secondHeaderFunction(_selected);
                        }
                    })) : (
                    <View style={styles.headerFiller} />
                )}
            </View>
        </View>
    )
};

export default function ScheduleNavigator({ Nav }: { Nav: Partial<Record<keyof typeof Params, StackInfo>> }) {
    const styles = getStyles();
    const Stack = createNativeStackNavigator<typeof Params>();
    const ref = React.useRef<SetSelectedRef>(null);
    const [favorite, setFavorite] = React.useState(false);

    return (
        <View style={styles.container}>
            <Stack.Navigator  >
                {Nav && Object.keys(Nav).map((key) => {
                    const _key = key as keyof typeof Params;
                    const nav = Nav[_key];
                    if (!nav) return null;
                    return (
                        <Stack.Screen
                            key={_key}
                            name={_key}
                            options={nav.hasHeader ?
                                {
                                    header: ({ navigation }) => (
                                        <Header navigation={navigation} title={nav.title} hasGoBack={!nav.noGoBack} secondHeader={nav.secondHeader} secondHeaderFunction={ref?.current?.setSelected} selected={favorite} />
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

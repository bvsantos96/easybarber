import React, { useCallback, useEffect } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Entypo from '@expo/vector-icons/Entypo';

import { getStyles } from '../styles/HomeNavigator';
import { NavigationProp } from '@react-navigation/native';
import { Props as SecondHeaderProps } from '@components/FavoriteHeader';
import { debounce } from 'lodash';
import useHeaderStore from 'storage/stores/HeaderStore';

type HeaderProps = {
    navigation: NavigationProp<any, any>;
    title: string;
    hasGoBack?: boolean;
    secondHeader?: React.FC<SecondHeaderProps>;
    secondHeaderFunction?: (selected: boolean) => Promise<boolean>;
    selected?: boolean;
}

const Header = ({ navigation, title, hasGoBack = true, secondHeader, secondHeaderFunction, selected: _selected }: HeaderProps) => {
    const { onPress } = useHeaderStore();
    const styles = getStyles();
    const [selected, setSelected] = React.useState(_selected || false);

    useEffect(() => { (_selected !== undefined && selected !== _selected) && setSelected(_selected || false) }, [_selected]);

    const debouncedSecond = useCallback(
        debounce((__selected: boolean) => {
            secondHeaderFunction && secondHeaderFunction(__selected);
        }, 1500),
        [secondHeaderFunction]
    );

    const onPressSecondFunction = (__selected: boolean) => {
        setSelected(__selected);
        debouncedSecond(__selected);
    };

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
                        selected: selected,
                        setSelected: () => {
                            onPressSecondFunction?.(!selected);
                            onPress?.();
                        }
                    })) : (
                    <View style={styles.headerFiller} />
                )}
            </View>
        </View>
    )
};

export default Header;

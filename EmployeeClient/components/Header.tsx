import Entypo from '@expo/vector-icons/Entypo';
import React, { useCallback, useEffect } from 'react';
import { Text, TouchableOpacity, View } from 'react-native';

import { Props as SecondHeaderProps } from '@components/FavoriteHeader';
import { NavigationProp } from '@react-navigation/native';
import { throttle } from 'lodash';
import useHeaderStore from 'storage/stores/HeaderStore';
import { getStyles } from '../styles/HomeNavigator';

type HeaderProps = {
    navigation: NavigationProp<any, any>;
    title: string;
    hasGoBack?: boolean;
    firstHeader?: React.FC<any>;
    firstHeaderFunction?: () => void;
    secondHeader?: React.FC<SecondHeaderProps>;
    secondHeaderFunction?: (selected: boolean) => Promise<boolean>;
    selected?: boolean;
    hideSecondHeader?: boolean;
}

const Header = ({ navigation, title, hasGoBack = true, firstHeader, secondHeader, secondHeaderFunction, selected: _selected, hideSecondHeader = false }: HeaderProps) => {
    const { onPress } = useHeaderStore();
    const styles = getStyles();
    const [selected, setSelected] = React.useState(_selected || false);

    useEffect(() => { (_selected !== undefined && selected !== _selected) && setSelected(_selected || false) }, [_selected]);

    const debouncedSecond = useCallback(
        throttle(
            (__selected: boolean) => {
                secondHeaderFunction && secondHeaderFunction(__selected);
            },
            1500,
            { leading: true, trailing: false }
        ),
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
                    }
                    }>
                        <Entypo name="chevron-small-left" size={styles.goBackIcon.width} color="black" />
                    </TouchableOpacity>
                ) : firstHeader ? (
                    React.createElement(firstHeader, {})
                )
                    : (
                        <View style={styles.headerFiller} />
                    )}
                <Text style={styles.headerTitle}>{title}</Text>
                {(secondHeader && !hideSecondHeader) ? (
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

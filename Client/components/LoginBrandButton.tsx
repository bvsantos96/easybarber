import React from 'react';
import { Text, View } from 'react-native';
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/Button';

import GoogleIcon from '../assets/icons/google.svg';
import AppleIcon from '../assets/icons/apple.svg';

import texts from '../langs/en.json';
import { useTheme } from '../styles/ThemeContext';

export function GoogleLoginButton({onPress = () => { }}) {
    const theme = useTheme();
    const icon = <GoogleIcon
        width={33 * theme.dimensions.absoluteWidth} 
        height={33 * theme.dimensions.absoluteHeight} 
    />;
    return BrandButton(onPress, icon, texts.login.google) ;
}

export function AppleLoginButton({onPress = () => { }}) {
    const theme = useTheme();
    const icon = <AppleIcon 
        width={33 * theme.dimensions.absoluteWidth} 
        height={33 * theme.dimensions.absoluteHeight} 
    />;
    return BrandButton(onPress, icon, texts.login.apple) ;
}

function BrandButton( onPress = () => { }, icon = <></>, name = "" ) {
    const styles = getStyles();
    return (
        <Pressable style={styles.brandButton} shadow onPress={onPress}>
            <View style={styles.brandButtonIcon}>
                {icon}
            </View>
            <View style={styles.brandButtonTextContainer}>
                <View style={styles.brandButtonTitleContainer}>
                    <Text style={styles.brandButtonText}>{texts.login.loginWith}</Text>
                </View>
                <View style={styles.brandButtonNameContainer}>
                    <Text style={styles.brandText}>{name}</Text>
                </View>
            </View>
        </Pressable>
    );
}

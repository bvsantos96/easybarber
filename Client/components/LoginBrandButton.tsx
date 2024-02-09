import React from 'react';
import { Text, View } from 'react-native';
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/Button';
import { absoluteWidth, styles as mainStyles } from '../styles/Main';

import GoogleIcon from '../assets/icons/google.svg';
import AppleIcon from '../assets/icons/apple.svg';

import texts from '../langs/en.json';

export function GoogleLoginButton() {
    const styles = getStyles();
    return (
        <Pressable style={[mainStyles.row, styles.smallButton, mainStyles.hMargin2]} onPress={() => alert("Google Login")}>
            <View style={mainStyles.hMargin2}>
                <GoogleIcon style={styles.smallButtonIconImage} width={25 * absoluteWidth} height={25 * absoluteWidth} />
            </View>
            <View style={mainStyles.hMargin2}>
                <Text style={styles.smallTextButton}>{texts.login.loginWith}</Text>
                <Text style={styles.brandTextButton}>{texts.login.google}</Text>
            </View>
        </Pressable>
    );
}

export function AppleLoginButton() {
    const styles = getStyles(); 
    return (
        <Pressable style={[mainStyles.row, styles.smallButton, mainStyles.hMargin2]} onPress={() => alert("Apple Login")}>
            <View style={mainStyles.hMargin2}>
                <AppleIcon style={styles.smallButtonIconImage} width={25 * absoluteWidth} height={25 * absoluteWidth} />
            </View>
            <View style={mainStyles.hMargin2}>
                <Text style={styles.smallTextButton}>{texts.login.loginWith}</Text>
                <Text style={styles.brandTextButton}>{texts.login.apple}</Text>
            </View>
        </Pressable>
    );
}

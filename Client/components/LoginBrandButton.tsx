import React from 'react';
import { Text, View, Image } from 'react-native';
import Pressable from '../components/Pressable';
import { styles } from '../styles/Button';
import { styles as mainStyles } from '../styles/Main';

import texts from '../langs/en.json';

export function GoogleLoginButton() {
    return (
        <Pressable style={[mainStyles.row, styles.smallButton, mainStyles.hMargin2]} onPress={() => alert("Google Login")}>
            <View style={mainStyles.hMargin2}>
                <Image source={require('../assets/icons/google.png')} style={styles.smallButtonIconImage} />
            </View>
            <View style={mainStyles.hMargin2}>
                <Text style={styles.smallTextButton}>{texts.login.loginWith}</Text>
                <Text style={styles.brandTextButton}>{texts.login.google}</Text>
            </View>
        </Pressable>
    );
}

export function AppleLoginButton() {
    return (
        <Pressable style={[mainStyles.row, styles.smallButton, mainStyles.hMargin2]} onPress={() => alert("Apple Login")}>
            <View style={mainStyles.hMargin2}>
                <Image source={require('../assets/icons/apple.png')} style={styles.smallButtonIconImage} />
            </View>
            <View style={mainStyles.hMargin2}>
                <Text style={styles.smallTextButton}>{texts.login.loginWith}</Text>
                <Text style={styles.brandTextButton}>{texts.login.apple}</Text>
            </View>
        </Pressable>
    );
}

import React from 'react';
import { View } from 'react-native';
import Button from '../components/Button';
import { styles as screenStyles } from '../styles/Screens';
import { styles as mainStyles } from '../styles/Main';
import { textBlackColor } from '../styles/Main';
import { thinBorderBlack, whitheButtonColor } from '../styles/Button';
import { styles as buttonStyles } from '../styles/Button';
import { PropNavigation } from '../App';
import Logo from '@assets/images/logo.svg';

export default function AccountTypeSelection({ navigation }: PropNavigation) {
    const texts = require("@lang/en.json");
    return (
        <View style={[mainStyles.containerMax, mainStyles.alignCenter, mainStyles.justifyCenter]}>
            <Logo style={screenStyles.centeredLogo} />
            <Button title={texts.imUser} stylesInput={buttonStyles.button11} onPress={() => {alert("I'm a user"); navigation.navigate('Login');}} />
            <Button title={texts.imBarber} stylesInput={buttonStyles.button12} backgroundColor={whitheButtonColor} buttonTextColor={textBlackColor} borderColor={thinBorderBlack} onPress={() => {alert("I'm a Barber"); navigation.navigate('Login')}} />
        </View>
    )
};

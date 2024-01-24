import React from 'react';
import { View, Image } from 'react-native';
import Button from '../components/Button';
import { styles as screenStyles } from '../styles/Screens';
import { styles as mainStyles, styles } from '../styles/Main';
import { textBlackColor } from '../styles/Main';
import { thinBorderBlack, whitheButtonColor } from '../styles/Button';
import { styles as buttonStyles } from '../styles/Button';
import { PropNavigation } from '../App';

export default function AccountTypeSelection({ navigation }: PropNavigation) {
    return (
        <View style={[mainStyles.containerMax]}>
            <Image style={screenStyles.centeredLogo} source={require("@assets/images/logo.png")} />
            <Button title="I'm a User" stylesInput={buttonStyles.button11} onPress={() => {alert("I'm a user"); navigation.navigate('Login');}} />
            <Button title="I'm a Barber" stylesInput={buttonStyles.button12} backgroundColor={whitheButtonColor} buttonTextColor={textBlackColor} borderColor={thinBorderBlack} onPress={() => {alert("I'm a Barber"); navigation.navigate('Login')}} />
        </View>
    )
};

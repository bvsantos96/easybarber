import React from 'react';
import { View, Image } from 'react-native';
import Button from '../components/Button';
import { styles as screenStyles } from '../styles/Screens';
import { styles as mainStyles } from '../styles/Main';
import { textBlackColor } from '../styles/Main';
import { thinBorderBlack, whitheButtonColor } from '../styles/Button'
import Divider from '../components/Divider';
import { PropNavigation } from '../App';

export default function AccountTypeSelection({ navigation }: PropNavigation) {
    return (
        <View style={mainStyles.containerSpaceBetween}>
            <View />
            <Image style={screenStyles.centeredLogo} source={require("@assets/images/logo.png")} />
            <View style={[mainStyles.w100, mainStyles.paddingBottom10]}>
                <Button title="I'm a User" onPress={() => {alert("I'm a user"); navigation.navigate('Login');}} />
                <Divider />
                <Button title="I'm a Barber" backgroundColor={whitheButtonColor} buttonTextColor={textBlackColor} borderColor={thinBorderBlack} onPress={() => {alert("I'm a Barber"); navigation.navigate('Login')}} />
                <Divider />
            </View>
        </View>
    )
};

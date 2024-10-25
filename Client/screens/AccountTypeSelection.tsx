import React from 'react';
import Button from '../components/Button';
import { getStyles as getScreensStyles } from '../styles/Screens';
import { getStyles as getButtonStyles } from '../styles/Button';
import Logo from '@assets/images/logo.svg';

import { useTheme } from '../styles/ThemeContext';
import RootNav from '@navigation/HomeNavigator';
import { Routes } from '@navigation/Router';

export default function AccountTypeSelection({ navigation }: PropNavigation) {
    const screenStyles = getScreensStyles();
    const buttonStyles = getButtonStyles();
    const texts = require("@lang/en.json");
    const theme = useTheme();
    return (
        <>
            <Logo width={264.965 * theme.dimensions.absoluteMinDimension} height={264.965 * theme.dimensions.absoluteMinDimension} style={screenStyles.centeredLogo} />
            <Button title={texts.imUser} stylesInput={buttonStyles.button11} onPress={() => { navigation.navigate(Routes.Sign); }} />
            <Button title={texts.imBarber} stylesInput={buttonStyles.button12} backgroundColor={theme.colors.backgroundColor} buttonTextColor={theme.colors.text.main} borderColor={theme.colors.text.main} onPress={() => { navigation.navigate(Routes.Sign) }} />
        </>
    )
};

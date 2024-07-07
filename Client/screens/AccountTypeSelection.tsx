import React from 'react';
import Button from '../components/Button';
import { getStyles as getScreensStyles } from '../styles/Screens';
import { getStyles as getButtonStyles } from '../styles/Button';
import { PropNavigation } from '../App';
import Logo from '@assets/images/logo.svg';

import { useTheme } from '../styles/ThemeContext';
import { DEBUG_AUTO_LOGIN } from '../utils/EnvVariables';
import { ALERT_TYPE } from 'react-native-alert-notification';
import { Banner } from '../components/Alert';

export default function AccountTypeSelection({ navigation }: PropNavigation) {
    const screenStyles = getScreensStyles();
    const buttonStyles = getButtonStyles();
    const texts = require("@lang/en.json");
    const theme = useTheme();
    return (
        <>
            <Logo width={264.965 * theme.dimensions.absoluteMinDimension} height={264.965 * theme.dimensions.absoluteMinDimension} style={screenStyles.centeredLogo} />
            <Button title={texts.imUser} stylesInput={buttonStyles.button11} onPress={() => { if (!DEBUG_AUTO_LOGIN) Banner({ type: ALERT_TYPE.INFO, title: "", message: "I'm a user" }); navigation.navigate('Sign'); }} />
            <Button title={texts.imBarber} stylesInput={buttonStyles.button12} backgroundColor={theme.colors.backgroundColor} buttonTextColor={theme.colors.text.main} borderColor={theme.colors.text.main} onPress={() => { Banner({ type: ALERT_TYPE.INFO, message: "I'm a Barber" }); navigation.navigate('Sign') }} />
        </>
    )
};

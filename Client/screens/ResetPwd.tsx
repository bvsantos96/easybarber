import React, { useRef, useState } from 'react';
import { View, Text, Animated } from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';

import LockImage from '@assets/images/changeLock.svg';
import Button from '@components/Button';
import { getStyles } from '../styles/ResetPwd';
import { HidePasswordIcon, PasswordIcon, ShowPasswordIcon } from '@components/Icons';
import Input from '@components/Input';
import Divider from '@components/Divider';
import { resetPwdRQ } from 'utils/ApiRequest';
import { Params, Routes } from '@navigation/Router';
import { resetNavigation } from 'utils/Utils';

export type Route = {
    mobileInformation: string,
    confirmationCode: string
};

type Props = NativeStackScreenProps<typeof Params, 'ResetPwd'>;

export default function ResetPwd({ route, navigation }: Props) {
    const styles = getStyles();
    const texts = require('@lang/en.json');

    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState<string>('');

    const { mobileInformation, confirmationCode } = route.params;

    const tiltAnimation = useRef(new Animated.Value(0)).current;
    
    const resetPwd = async () => {
        if( password !== confirmPassword ){
            setErrorMessage(texts.pwdRecovery.pwdMustMatch);
            tiltScreen();
            return;
        }
        const result = await resetPwdRQ(mobileInformation, confirmationCode, password, confirmPassword);

        if (result) {
            resetNavigation(navigation, Routes.Sign);
        }
    };

    const tiltScreen = () => {
        Animated.sequence([
            Animated.timing(tiltAnimation, { toValue: 0.1, duration: 100, useNativeDriver: true }),
            Animated.timing(tiltAnimation, { toValue: -0.1, duration: 100, useNativeDriver: true }),
            Animated.timing(tiltAnimation, { toValue: 0.1, duration: 100, useNativeDriver: true }),
            Animated.timing(tiltAnimation, { toValue: 0, duration: 100, useNativeDriver: true })
        ]).start();
    };

    return (
        <Animated.View
            style={[
                styles.container,
                {
                    transform: [{
                        rotate: tiltAnimation.interpolate({
                            inputRange: [-1, 1],
                            outputRange: ['-5deg', '5deg']
                        })
                    }]
                }
            ]}
        >
            <View style={styles.container}>
                <View style={styles.eclipse} />
                <LockImage style={styles.lockImage} />
                <View style={styles.newPwdTextContainer}>
                    <Text style={styles.newPwdText}>{texts.pwdRecovery.setNewPed}</Text>
                </View>
                <View style={styles.passwordInputContainer}>
                    <Input
                        leftIcon={<PasswordIcon />}
                        placeholder={texts.password}
                        password={true}
                        onInputChange={setPassword}
                        rightIcon={[<ShowPasswordIcon />, <HidePasswordIcon />]}
                    />
                    <Divider size={19.25} />
                    <Input
                        leftIcon={<PasswordIcon />}
                        placeholder={texts.confirmPassword}
                        password={true}
                        onInputChange={setConfirmPassword}
                        rightIcon={[<ShowPasswordIcon />, <HidePasswordIcon />]}
                    />
                </View>
                {errorMessage ? (
                <Text style={styles.errorMessage}>{errorMessage}</Text>
                ) : null}
                <View style={styles.buttonContainer}>
                    <Button title={texts.pwdRecovery.forgotPwd} onPress={resetPwd} />
                </View>
            </View>
        </Animated.View>
    );
}

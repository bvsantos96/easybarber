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
import texts from '@lang/en.json';
import KeyboardAvoidingScrollView from '@components/KeyboardAvoidingScrollView';
import useAlertStore from 'storage/stores/AlertStore';
import { AlertType } from '@components/Alert';

export type Route = {
    mobileInformation: string,
    confirmationCode: string
};

type Props = NativeStackScreenProps<typeof Params, 'ResetPwd'>;

export default function ResetPwd({ route, navigation }: Props) {
    const { alert } = useAlertStore();
    const styles = getStyles();
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [keyboardHeight, setKeyboardHeight] = useState<number>(0);
    const translateYAnimation = useRef(new Animated.Value(0)).current;

    const { mobileInformation, confirmationCode } = route.params;

    const tiltAnimation = useRef(new Animated.Value(0)).current;

    const resetPwd = async () => {
        if (password !== confirmPassword) {
            setErrorMessage(texts.pwdRecovery.pwdMustMatch);
            tiltScreen();
            return;
        }
        const result = await resetPwdRQ(mobileInformation, confirmationCode, password, confirmPassword);

        if (result) {
            alert({ type: AlertType.Info, message: texts.pwdRecovery.pwdResetSuccess, onPress: () => { navigation.navigate(Routes.Sign) }, buttonText: texts.dismiss });
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

    const onKeyboardChange = (up: boolean) => {
        if (up) {
            Animated.timing(translateYAnimation, {
                toValue: 0,
                duration: 300,
                useNativeDriver: false,
            }).start();
            return;
        }

        Animated.timing(translateYAnimation, {
            toValue: 1,
            duration: 300,
            useNativeDriver: false,
        }).start();
    }

    return (
        <KeyboardAvoidingScrollView
            setKeyboardHeight={setKeyboardHeight}
            maxHeight={styles.container.height}
            keyboardHide={() => onKeyboardChange(false)}
            keyboardShow={() => onKeyboardChange(true)}
        >
            <Animated.View
                style={[
                    styles.container,
                    {
                        transform: [{
                            translateY: translateYAnimation.interpolate({
                                inputRange: [0, 1],
                                outputRange: [-keyboardHeight, 0],
                            })
                        },
                        {
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
                        <Text style={styles.newPwdText}>{texts.pwdRecovery.setNewPwd}</Text>
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
                        <Button title={texts.pwdRecovery.setPwd} onPress={resetPwd} />
                    </View>
                </View>
            </Animated.View>
        </KeyboardAvoidingScrollView>
    );
}

import React, { useEffect, useRef, useState } from 'react';
import { View, Text, Animated } from 'react-native';
import LockImage from '@assets/images/lock.svg';
import Button from '@components/Button';
import { getStyles } from '../styles/InsertPhone';
import PhoneInput from '@components/PhoneInput';
import { Country } from 'react-native-country-picker-modal';
import { getDefaultCountryAsync } from 'utils/Constants';
import { Routes } from '@navigation/Router';
import { getMobileCodeResetPwd } from 'utils/ApiRequest';
import KeyboardAvoidingScrollView from '@components/KeyboardAvoidingScrollView';
import { MobileConfirmationFunctions } from 'enums';

export default function ForgotPwd({ navigation }: PropNavigation) {
    const styles = getStyles();
    const texts = require('@lang/en.json');
    const [nation, setNation] = useState<Country | null | undefined>();
    const [phone, setPhone] = useState("");
    const [keyboardHeight, setKeyboardHeight] = useState<number>(0);
    const translateYAnimation = useRef(new Animated.Value(0)).current;

    useEffect(() => {
        const fetchDefaultCountry = async () => {
            try {
                const DEFAULT_COUNTRY = await getDefaultCountryAsync();
                setNation(DEFAULT_COUNTRY);
            } catch (error) {
                console.error(texts.errors.defaultCountry, error);
            }
        };

        fetchDefaultCountry();
    }, []);

    const forgotPwd = async () => {
        const blockuntil = await getMobileCodeResetPwd(nation ? nation.callingCode[0] : "", phone);
        console.log(blockuntil);
        if (!blockuntil) {
            return;
        }

        navigation.navigate(Routes.MobileConfirmation,
            {
                phoneNr: phone,
                countryCode: nation ? nation.callingCode[0] : "",
                nextScreen: "ResetPwd",
                resetNavigationBoolean: false,
                blockUntil: blockuntil === 0 ? undefined : blockuntil,
                resendFunction: MobileConfirmationFunctions.RESET_PASSWORD
            });
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
                        }]
                    }
                ]}
            >
                <View style={styles.eclipse} />
                <LockImage style={styles.lockImage} />
                <View style={styles.enterPhoneContainer}>
                    <Text style={styles.enterPhone}>{texts.pwdRecovery.enterPhone}</Text>
                </View>
                <View style={styles.mobileInputContainer}>
                    <PhoneInput
                        {...{
                            setPhone,
                            setNation,
                            nation
                        }}
                    />
                </View>
                <View style={styles.buttonContainer}>
                    <Button title={texts.pwdRecovery.forgotPwd} onPress={forgotPwd} />
                </View>
            </Animated.View>
        </KeyboardAvoidingScrollView >
    );
}

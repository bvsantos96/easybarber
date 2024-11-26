import React, { useEffect, useRef, useState } from 'react';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { View, Text, TouchableOpacity, TextInput, NativeSyntheticEvent, TextInputKeyPressEventData, Animated } from 'react-native';

import { getStyles } from '../styles/MobileConfirmation';
import KeyImage from '@assets/images/key.svg';
import ChatImage from '@assets/images/chat.svg';
import Divider from '@components/Divider';
import Button from '@components/Button';
import { confirmMobileCode, getMobileCode, getMobileCodeResetPwd } from 'utils/ApiRequest';
import { resetNavigation } from 'utils/Utils';
import { Params, Routes } from '@navigation/Router';
import KeyboardAvoidingScrollView from '@components/KeyboardAvoidingScrollView';
import texts from '@lang/en.json';
import { FunctionTypes } from 'enums';

export type Route = {
    blockUntil?: number,
    phoneNr: string,
    countryCode: string,
    nextScreen: typeof Routes.ResetPwd,
    resetNavigationBoolean: boolean,
    resendFunction: FunctionTypes
};

type Props = NativeStackScreenProps<typeof Params, 'MobileConfirmation'>;

export default function MobileConfirmation({ route, navigation }: Props) {
    const styles = getStyles();
    const { blockUntil, phoneNr, countryCode, nextScreen, resetNavigationBoolean, resendFunction } = route.params;
    const mobileInformation = countryCode + phoneNr;
    const [code, setCode] = useState<string[]>(['', '', '', '', '', '']);
    const [errorMessage, setErrorMessage] = useState<string>('');
    const inputRefs = useRef<(TextInput | null)[]>([]);
    const tiltAnimation = useRef(new Animated.Value(0)).current;
    const [keyboardHeight, setKeyboardHeight] = useState<number>(0);
    const translateYAnimation = useRef(new Animated.Value(0)).current;
    const [blockTime, setBlockTime] = useState<number>(blockUntil || 0);

    useEffect(() => {
        inputRefs.current[0]?.focus();
    }, []);

    const handleCodeChange = (text: string, index: number): void => {
        const numericText = text.replace(/[^0-9]/g, '');

        if (numericText.length <= 1) {
            const newCode = [...code];
            newCode[index] = numericText;
            setCode(newCode);

            if (numericText !== '' && index < 5) {
                inputRefs.current[index + 1]?.focus();
            }
        } else {
            const pastedCode = numericText.slice(0, 6).split('');
            const newCode = [...pastedCode, ...Array(6 - pastedCode.length).fill('')];
            setCode(newCode);

            const nextIndex = Math.min(pastedCode.length, 5);
            inputRefs.current[nextIndex]?.focus();
        }
    };

    const handleKeyPress = (event: NativeSyntheticEvent<TextInputKeyPressEventData>, index: number): void => {
        if (event.nativeEvent.key === 'Backspace' && index > 0 && code[index] === '') {
            inputRefs.current[index - 1]?.focus();
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

    const handleConfirmCode = async () => {
        const confirmationCode = code.join('');
        const response = await confirmMobileCode(mobileInformation, confirmationCode);

        if (response) {
            if (resetNavigationBoolean) {
                resetNavigation(navigation, nextScreen);
            }
            else {
                navigation.navigate(nextScreen, { mobileInformation: mobileInformation, confirmationCode: confirmationCode });
            }
        } else {
            setErrorMessage(texts.code.verificationFailed);
            tiltScreen();
        }
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

    useEffect(() => {
        if (blockTime > 0) {
            const timer = setInterval(() => {
                setBlockTime((prev) => prev - 1);
            }, 1000);

            return () => clearInterval(timer);
        }
    }, [blockTime]);

    const _resendFunction = async () => {
        switch (resendFunction) {
            case FunctionTypes.CONFIRMATION_CDOE:
                setBlockTime(await getMobileCode(countryCode, phoneNr) || 0);
            case FunctionTypes.RESET_PASSWORD:
                setBlockTime(await getMobileCodeResetPwd(countryCode, phoneNr) || 0);
        }
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
                <View style={styles.eclipse} />
                <ChatImage style={styles.chatImage} />
                <KeyImage style={styles.keyImage} />
                <View style={styles.insertCodeContainer}>
                    <Text style={styles.insertCode}>{texts.code.enterCode} +{mobileInformation}</Text>
                </View>
                <View style={styles.codeInputContainer}>
                    {[0, 1, 2, 3, 4, 5].map((index) => (
                        <TextInput
                            key={index}
                            style={styles.codeInput}
                            keyboardType="numeric"
                            maxLength={6 - index}
                            value={code[index]}
                            onChangeText={(text) => handleCodeChange(text, index)}
                            onKeyPress={(event) => handleKeyPress(event, index)}
                            ref={(ref) => (inputRefs.current[index] = ref)}
                        />
                    ))}
                </View>
                {errorMessage ? (
                    <Text style={styles.errorMessage}>{errorMessage}</Text>
                ) : null}
                <TouchableOpacity style={styles.resendCodeContainer} onPress={_resendFunction} disabled={blockTime === 0} >
                    <View style={styles.row}>
                        <Text style={styles.resendCodeText}>{texts.code.codeNotReceived}</Text>
                        <Divider horizontal size={3} />
                        <Text style={styles.resendCodeRedText}>{texts.code.resendCode}</Text>
                    </View>
                    {blockTime > 0 && <Text style={styles.resendCodeText}>{texts.code.resendCodeIn} {blockTime}</Text>}
                </TouchableOpacity>
                <View style={styles.buttonContainer}>
                    <Button title={texts.code.verify} onPress={handleConfirmCode} />
                </View>
            </Animated.View>
        </KeyboardAvoidingScrollView>
    );
}

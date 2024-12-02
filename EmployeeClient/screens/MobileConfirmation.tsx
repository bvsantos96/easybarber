import React, { useEffect, useRef, useState } from 'react';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { View, Text, TouchableOpacity, TextInput, NativeSyntheticEvent, TextInputKeyPressEventData, Animated } from 'react-native';

import { getStyles } from '../styles/MobileConfirmation';
import KeyImage from '@assets/images/key.svg';
import ChatImage from '@assets/images/chat.svg';
import Divider from '@components/Divider';
import Button from '@components/Button';
import { MobileConfirmationFunctions } from 'enums';
import { confirmMobileCode, doRegister, getMobileCode, getMobileCodeResetPwd } from 'utils/ApiRequest';
import { getTimeDifferenceInMinutesAndSeconds, resetNavigation } from 'utils/Utils';
import { Params, Routes } from '@navigation/Router';
import KeyboardAwareView from '@components/KeyboardAwareView';
import texts from '@lang/en.json';
import useMobileConfirmationStore from 'storage/stores/MobileConfirmationStore';

export type Route = {
    phoneNr: string,
    countryCode: string,
    nextScreen: typeof Routes.ResetPwd,
    resetNavigationBoolean: boolean,
    functionData?: Object,
    functionName?: MobileConfirmationFunctions,
    resendFunction: MobileConfirmationFunctions
};

type Props = NativeStackScreenProps<typeof Params, 'MobileConfirmation'>;

export default function MobileConfirmation({ route, navigation }: Props) {
    const { blockTime: blockUntil } = useMobileConfirmationStore();
    const styles = getStyles();
    const { phoneNr, countryCode, nextScreen, resetNavigationBoolean, functionData, functionName, resendFunction } = route.params;
    const mobileInformation = countryCode + phoneNr;
    const [code, setCode] = useState<string[]>(['', '', '', '', '', '']);
    const [errorMessage, setErrorMessage] = useState<string>('');
    const inputRefs = useRef<(TextInput | null)[]>([]);
    const tiltAnimation = useRef(new Animated.Value(0)).current;
    const [keyboardHeight, setKeyboardHeight] = useState<number>(0);
    const translateYAnimation = useRef(new Animated.Value(0)).current;
    const [blockTime, setBlockTime] = useState<string | undefined>(undefined);
    const [blockUntilTimetamp, setBlockUntilTimestamp] = useState<number | undefined | null>(blockUntil);


    useEffect(() => {
        if (blockUntil !== null) {
            setBlockUntilTimestamp(blockUntil)
        }
    }, [blockUntil]);

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
            Animated.timing(tiltAnimation, { toValue: 0.1, duration: 100, useNativeDriver: false }),
            Animated.timing(tiltAnimation, { toValue: -0.1, duration: 100, useNativeDriver: false }),
            Animated.timing(tiltAnimation, { toValue: 0.1, duration: 100, useNativeDriver: false }),
            Animated.timing(tiltAnimation, { toValue: 0, duration: 100, useNativeDriver: false })
        ]).start();
    };

    const updateBlockTimeFromTimestamp = async (time?: number | undefined | null) => {
        if (time === undefined || time === null) {
            setBlockTime(undefined);
            return;
        }
        const now = new Date();
        const limit = new Date(time);
        if (limit < now) {
            setBlockTime(undefined);
            return;
        }
        setBlockTime(getTimeDifferenceInMinutesAndSeconds(now, limit));
    }

    const func = async (name = functionName) => {
        let time;
        switch (name) {
            case MobileConfirmationFunctions.REGISTER:
                const data = functionData as RegisterInfo;
                return await doRegister(data.countryCode, data.phone, data.password, data.confirmPassword, data.name, code.join(''));
            case MobileConfirmationFunctions.CONFIRMATION_CODE:
                setBlockTime("...");
                time = await getMobileCode(countryCode, phoneNr);
                setBlockUntilTimestamp(time);
                return time;
            case MobileConfirmationFunctions.RESET_PASSWORD:
                setBlockTime("...");
                time = await getMobileCodeResetPwd(countryCode, phoneNr);
                setBlockUntilTimestamp(time);
                return time;
            default:
                return true;
        }
    }

    const handleConfirmCode = async () => {
        const confirmationCode = code.join('');
        const response = await confirmMobileCode(mobileInformation, confirmationCode);

        if (response) {
            if (functionName && !(await func())) {
                navigation.goBack();
                return;
            }
            if (resetNavigationBoolean) {
                if (navigation.getState().index > 1) {
                    navigation.pop(2);
                    return;
                }
                resetNavigation(navigation, nextScreen);
                return;
            }
            else {
                setCode(['', '', '', '', '', '']);
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
        updateBlockTimeFromTimestamp(blockUntilTimetamp);
    }, [blockUntilTimetamp]);

    useEffect(() => {
        if (blockTime !== undefined && blockTime !== "...") {
            const timer = setInterval(() => {
                updateBlockTimeFromTimestamp(blockUntilTimetamp);
            }, 1000);

            return () => clearInterval(timer);
        }
    }, [blockTime]);

    return (
        <KeyboardAwareView
            onKeyboardHide={() => onKeyboardChange(false)}
            onKeyboardShow={() => onKeyboardChange(true)}
            setKeyboardHeight={setKeyboardHeight}
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
                <TouchableOpacity style={styles.resendCodeContainer} onPress={() => { func(resendFunction) }} disabled={!!blockTime && blockTime !== ""} >
                    {(!!blockTime && blockTime !== "") ? (
                        <Text style={styles.resendCodeText}>{texts.code.resendCodeIn} {blockTime}</Text>
                    ) : (
                        <View style={styles.row}>
                            <Text style={styles.resendCodeText}>{texts.code.codeNotReceived}</Text>
                            <Divider horizontal size={3} />
                            <Text style={styles.resendCodeRedText}>{texts.code.resendCode}</Text>
                        </View>
                    )}
                </TouchableOpacity>
                <View style={styles.buttonContainer}>
                    <Button title={texts.code.verify} onPress={handleConfirmCode} />
                </View>
            </Animated.View>
        </KeyboardAwareView>
    );
}

import React, { useState } from 'react';
import { View, Text } from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';

import LockImage from '@assets/images/changeLock.svg';
import Button from '@components/Button';
import { getStyles } from '../styles/ResetPwd';
import { HidePasswordIcon, PasswordIcon, ShowPasswordIcon } from '@components/Icons';
import Input from '@components/Input';
import Divider from '@components/Divider';
import { resetPwdRQ } from 'utils/ApiRequest';
import { Params } from '@navigation/Router';

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

    const { mobileInformation, confirmationCode } = route.params;

    const resetPwd = async () => {
        const result = await resetPwdRQ(mobileInformation, confirmationCode, password, confirmPassword);

        if (result) {
            alert("success");
        }
    };

    return (
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
            <View style={styles.buttonContainer}>
                <Button title={texts.pwdRecovery.forgotPwd} onPress={resetPwd} />
            </View>
        </View>
    );
}

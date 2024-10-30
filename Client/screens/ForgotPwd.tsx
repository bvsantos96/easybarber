import React, { useEffect, useState } from 'react';
import { View, Text } from 'react-native';
import LockImage from '@assets/images/lock.svg';
import Button from '@components/Button';
import { getStyles } from '../styles/InsertPhone';
import PhoneInput from '@components/PhoneInput';
import { Country } from 'react-native-country-picker-modal';
import { getDefaultCountryAsync } from 'utils/Constants';
import { Routes } from '@navigation/Router';
import { getMobileCodeResetPwd } from 'utils/ApiRequest';

export default function ForgotPwd({ navigation }: PropNavigation) {
    const styles = getStyles();
    const texts = require('@lang/en.json');
    const [nation, setNation] = useState<Country | null | undefined>();
    const [phone, setPhone] = useState("");

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
        const mobileInformation = (nation ? nation.callingCode[0] : "") + phone;
        const success = await getMobileCodeResetPwd(nation ? nation.callingCode[0] : "", phone);

        if(success){
            navigation.navigate(Routes.MobileConfirmation, { mobileInformation: mobileInformation, nextScreen: "ResetPwd", resetNavigationBoolean: false });
        }
    };

    return (
        <View style={styles.container}>
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
        </View>
    );
}

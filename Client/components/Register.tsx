import React from 'react';
import { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';

import Input from '../components/Input';
import Title from '../components/Title';
import { PasswordIcon, NameIcon, ShowPasswordIcon, HidePasswordIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';

import { getStyles } from '../styles/Sign';
import { SignInProps } from '../screens/SignIn';
import { doRegister, getMobileCode, validateRegister } from '../utils/ApiRequest';
import { Country } from 'react-native-country-picker-modal';
import PhoneInput from './PhoneInput';
import { getDefaultCountryAsync } from '../utils/Constants';
import texts from "../langs/en.json";
import { Routes } from '@navigation/Router';
import KeyboardAvoidingScrollView from './KeyboardAvoidingScrollView';
import { AlertType } from './Alert';
import useAlertStore from 'storage/stores/AlertStore';
import { MobileConfirmationFunctions } from 'enums';

export default function Register({ navigation, toggleNewUser, expand, collapse }: SignInProps) {
    const { alert } = useAlertStore();
    const styles = getStyles();
    const [name, setName] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [nation, setNation] = useState<Country | null | undefined>();

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

    const register = async () => {
        const result = await validateRegister(nation ? nation.callingCode[0] : "", phone, password, confirmPassword, name);
        if (result.success === false) {
            alert({ type: AlertType.Error, message: result.message });
            return;
        }

        const countryCode = nation ? nation.callingCode[0] : "";
        const registerInfo: RegisterInfo = {
            name: name,
            phone: phone,
            password: password,
            confirmPassword: confirmPassword,
            countryCode: countryCode
        }

        const blockuntil = await getMobileCode(countryCode, phone);
        if (!blockuntil) {
            return;
        }

        navigation.navigate(Routes.MobileConfirmation,
            {
                phoneNr: phone,
                countryCode: countryCode,
                nextScreen: Routes.Tabs,
                resetNavigationBoolean: true,
                functionName: MobileConfirmationFunctions.REGISTER,
                functionData: registerInfo,
                blockUntil: blockuntil === 0 ? undefined : blockuntil,
                resendFunction: MobileConfirmationFunctions.CONFIRMATION_CODE
            });
    }

    return (
        <>
            <KeyboardAvoidingScrollView
                keyboardShow={expand}
                keyboardHide={collapse}
                maxHeight={400}
                fixedTopComponent={
                    <>
                        <Divider size={40} />
                        <Title line={[{ text: texts.register.title, highlight: false }]} />
                    </>
                }
                fixedBottomComponent={
                    <View style={styles.buttonContainer}>
                        <Divider size={25} />
                        <Button title={texts.register.button} onPress={register} />
                        <Divider size={20} />
                        <TouchableOpacity style={styles.alreadyRegisteredContainer} onPress={toggleNewUser}>
                            <Text style={styles.newUserText}>{texts.register.newUser}</Text>
                            <Divider horizontal size={20} />
                            <Text style={styles.newUserRedText}>{texts.login.button}</Text>
                        </TouchableOpacity>
                    </View>
                }
            >
                <Divider size={30} />
                <Input
                    leftIcon={<NameIcon />}
                    placeholder={texts.name}
                    type="text"
                    onInputChange={setName}
                />
                <Divider size={20} />
                <PhoneInput
                    {...{
                        setPhone,
                        setNation,
                        nation
                    }}
                />
                <Divider size={20} />
                <Input
                    leftIcon={<PasswordIcon />}
                    placeholder={texts.password}
                    password={true}
                    onInputChange={setPassword}
                    rightIcon={[<ShowPasswordIcon />, <HidePasswordIcon />]}
                />
                <Divider size={20} />
                <Input
                    leftIcon={<PasswordIcon />} placeholder={texts.confirmPassword}
                    password={true}
                    onInputChange={setConfirmPassword}
                    rightIcon={[<ShowPasswordIcon />, <HidePasswordIcon />]}
                />
            </KeyboardAvoidingScrollView>
        </>
    )
}

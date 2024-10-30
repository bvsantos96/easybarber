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
import { getMobileCode } from '../utils/ApiRequest';
import { Country } from 'react-native-country-picker-modal';
import PhoneInput from './PhoneInput';
import { getDefaultCountryAsync } from '../utils/Constants';
import texts from "../langs/en.json";
import { Routes } from '@navigation/Router';
import KeyboardAvoidingScrollView from './KeyboardAvoidingScrollView';

export default function Register({ navigation, toggleNewUser, expand, collapse }: SignInProps) {
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
        const mobileInformation = (nation ? nation.callingCode[0] : "") + phone;
        const result = await getMobileCode(nation ? nation.callingCode[0] : "", mobileInformation);
        navigation.navigate(Routes.MobileConfirmation, { mobileInformation: mobileInformation, nextScreen: Routes.Tabs, resetNavigationBoolean: true });
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
                    leftIcon={<PasswordIcon />}
                    placeholder={texts.confirmPassword}
                    password={true}
                    onInputChange={setConfirmPassword}
                    rightIcon={[<ShowPasswordIcon />, <HidePasswordIcon />]}
                />
            </KeyboardAvoidingScrollView>
        </>
    )
}

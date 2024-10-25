import React from 'react';
import { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';

import { Country } from 'react-native-country-picker-modal';

import Input from '../components/Input';
import Title from '../components/Title';
import { HidePasswordIcon, PasswordIcon, ShowPasswordIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';
import { AppleLoginButton, GoogleLoginButton } from '../components/LoginBrandButton';

import { doLogin } from '../utils/ApiRequest';

import { getStyles } from '../styles/Sign';
import { SignInProps } from '../screens/SignIn';
import PhoneInput from './PhoneInput';
import { getDefaultCountryAsync } from '../utils/Constants';
import { DEBUG_AUTO_LOGIN, DEBUG_AUTO_LOGIN_PASSWORD, DEBUG_AUTO_LOGIN_PHONE } from '../utils/EnvVariables';
import { AlertType } from './Alert';
import texts from "../langs/en.json";
import useAlertStore from 'storage/stores/AlertStore';
import { Routes } from '@navigation/Router';
import { resetNavigation } from 'utils/Utils';

export default function Login({ navigation, toggleNewUser }: SignInProps) {
    const styles = getStyles();
    const [phone, setPhone] = useState("");
    const [nation, setNation] = useState<Country | null>();
    const [password, setPassword] = useState("");

    const { alert } = useAlertStore();

    useEffect(() => {
        const fakeLogin = async () => {
            const result: IResult<IAPIResponse> = await doLogin("351", DEBUG_AUTO_LOGIN_PHONE, DEBUG_AUTO_LOGIN_PASSWORD);
            if (result.success)
                resetNavigation(navigation, Routes.Tabs);
            else {
                console.error(result.message);
            }
        }

        if (DEBUG_AUTO_LOGIN) {
            fakeLogin();
            return;
        }

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

    const login = async () => {
        if (!nation) {
            alert({ type: AlertType.Info, message: texts.apiMessages.invalidCountry });
            return;
        }
        const result: IResult<IAPIResponse> = await doLogin(nation.callingCode[0], phone, password);
        if (result.success)
            resetNavigation(navigation, Routes.Tabs);
        else {
            alert({ type: AlertType.Error, message: `${texts.apiMessages.login.failed}\n${result.message}` });
        }
    }

    return (
        <>
            <View style={styles.titleContainer} >
                <Divider size={11} />
                <Title line={[{ text: texts.login.title, highlight: false }]} />
            </View>
            <View style={styles.inputsContainer}>
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
            </View>
            <View style={styles.forgotPassContainer}>
                <Text style={styles.forgotPass} onPress={() => navigation.navigate(Routes.ForgotPwd)} >{texts.forgotPassword}</Text>
            </View>
            <View style={styles.buttonContainer}>
                <Button title={texts.login.button} onPress={login} />
            </View>
            <TouchableOpacity style={styles.newUserContainer} onPress={toggleNewUser}>
                <Text style={styles.newUserText}>{texts.login.newUser}</Text>
                <Divider horizontal size={13} />
                <Text style={styles.newUserRedText}>{texts.register.button}</Text>
            </TouchableOpacity>
            <View style={styles.appleButtonContainer}>
                <AppleLoginButton />
            </View>
            <View style={styles.googleButtonContainer}>
                <GoogleLoginButton />
            </View>
        </>
    )
}

import React, { useEffect, useState } from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { Country } from 'react-native-country-picker-modal';

import Button from '../components/Button';
import Divider from '../components/Divider';
import { HidePasswordIcon, PasswordIcon, ShowPasswordIcon } from '../components/Icons';
import Input from '../components/Input';
import Title from '../components/Title';

import { doLogin } from '../utils/ApiRequest';

import { Routes } from '@navigation/Router';
import { useTheme } from '@styles/ThemeContext';
import useAlertStore from 'storage/stores/AlertStore';
import { resetNavigation } from 'utils/Utils';
import texts from "../langs/en.json";
import { SignInProps } from '../screens/SignIn';
import { getStyles } from '../styles/Sign';
import { getDefaultCountryAsync } from '../utils/Constants';
import { DEBUG_AUTO_LOGIN, DEBUG_AUTO_LOGIN_PASSWORD, DEBUG_AUTO_LOGIN_PHONE } from '../utils/EnvVariables';
import { AlertType } from './Alert';
import KeyboardAvoidingScrollView from './KeyboardAvoidingScrollView';
import PhoneInput from './PhoneInput';

export default function Login({ navigation, toggleNewUser, expand, collapse }: SignInProps) {
    const theme = useTheme();
    const styles = getStyles();
    const [phone, setPhone] = useState("");
    const [nation, setNation] = useState<Country | null>();
    const [password, setPassword] = useState("");

    const { alert } = useAlertStore();

    useEffect(() => {

    }, [nation]);

    useEffect(() => {
        const fetchDefaultCountry = async () => {
            try {
                const DEFAULT_COUNTRY = await getDefaultCountryAsync();
                setNation(DEFAULT_COUNTRY);
                if (DEBUG_AUTO_LOGIN) {
                    const result: IResult<IAPIResponse> = await doLogin(DEFAULT_COUNTRY?.callingCode[0] || "+351", DEBUG_AUTO_LOGIN_PHONE, DEBUG_AUTO_LOGIN_PASSWORD);
                    if (result.success)
                        resetNavigation(navigation, Routes.Tabs);
                    else {
                        console.error(result.message);
                    }
                }

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
        if (result.success) {
            if (navigation.getState().index > 0) {
                navigation.goBack();
            } else {
                resetNavigation(navigation, Routes.Tabs);
            }
        } else {
            alert({ type: AlertType.Error, message: `${texts.apiMessages.login.failed}\n${result.message}` });
        }
    }

    return (
        <>
            <KeyboardAvoidingScrollView
                keyboardShow={expand}
                keyboardHide={collapse}
                maxHeight={350}
                fixedTopComponent={
                    <View style={styles.titleContainer} >
                        <Divider size={40} />
                        <Title line={[{ text: texts.login.title, highlight: false }]} />
                    </View>
                }
                fixedBottomComponent={
                    <View style={styles.buttonContainer}>
                        <Button title={texts.login.button} onPress={login} borderColor={theme.colors.text.lightWhite} />
                    </View>

                }
            >
                <View style={styles.inputsContainer}>
                    <Divider size={40} />
                    <PhoneInput
                        username={true}
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
                </View>
                <View style={styles.forgotPassContainer}>
                    <Text style={styles.forgotPass} onPress={() => navigation.navigate(Routes.ForgotPwd)} >{texts.forgotPassword}</Text>
                </View>
                <Divider size={25} />
            </KeyboardAvoidingScrollView>
            <Divider size={25} />
            <TouchableOpacity style={styles.newUserContainer} onPress={toggleNewUser}>
                <Text style={styles.newUserText}>{texts.login.newUser}</Text>
                <Divider horizontal size={13} />
                <Text style={styles.newUserRedText}>{texts.register.button}</Text>
            </TouchableOpacity>
        </>
    )
}

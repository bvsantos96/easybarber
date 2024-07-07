import React from 'react';
import { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Input from '../components/Input';
import Title from '../components/Title';
import { PasswordIcon, NameIcon, ShowPasswordIcon, HidePasswordIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';

import { getStyles } from '../styles/Sign';
import { resetNavigation } from '../App';
import { Props } from '../screens/SignIn';
import { doRegister } from '../utils/ApiRequest';
import { Country } from 'react-native-country-picker-modal';
import PhoneInput from './PhoneInput';
import { IResult } from '../declarations';
import { getDefaultCountryAsync } from '../utils/Constants';
import { Alert } from './Alert';
import { ALERT_TYPE } from 'react-native-alert-notification';
import texts from "../langs/en.json";

export default function Register({ navigation, toggleNewUser }: Props) {
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
        const result: IResult<any> = await doRegister(nation ? nation.callingCode[0] : "", phone, password, confirmPassword, name);
        if (result.success)
            resetNavigation(navigation, 'Tabs');
        else
            Alert({ type: ALERT_TYPE.WARNING, title: texts.apiMessages.register.failed, message: result.message });
    }

    return (
        <>
            <View style={styles.titleContainer} >
                <Title line={[{ text: texts.register.title, highlight: false }]} />
                <Divider size={23} />
                <Input
                    leftIcon={<NameIcon />}
                    placeholder={texts.name}
                    type="text"
                    onInputChange={setName}
                />
                <Divider size={19.25} />
                <PhoneInput
                    {...{
                        setPhone,
                        setNation,
                        nation
                    }}
                />
                <Divider size={19.25} />
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
                <Divider size={36.25} />
                <Button title={texts.register.button} onPress={register} />
                <Divider size={35.25} />
                <TouchableOpacity style={styles.alreadyRegisteredContainer} onPress={toggleNewUser}>
                    <Text style={styles.newUserText}>{texts.register.newUser}</Text>
                    <Divider horizontal size={13} />
                    <Text style={styles.newUserRedText}>{texts.login.button}</Text>
                </TouchableOpacity>
            </View>
        </>
    )
}

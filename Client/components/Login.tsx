import { useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Input from '../components/Input';
import Title from '../components/Title';
import { PhoneIcon, PasswordIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';
import { AppleLoginButton, GoogleLoginButton } from '../components/LoginBrandButton';

import { doLogin, Result } from '../utils/ApiRequest';

import{ getStyles } from '../styles/Sign';
import { resetNavigation } from '../App';
import { Props } from '../screens/SignIn';

export default function Login({ navigation, toggleNewUser }: Props) {
    const styles =  getStyles();
    const texts = require("@lang/en.json");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");

    const login = async() => {
        const result: Result = await doLogin(phone, password);
        if(result.success)
            resetNavigation(navigation, 'Tabs');
        else
            alert(result.message);
    }

    return (
        <>
            <View style={styles.titleContainer} >
                <Divider size={11} />
                <Title line={[{ text: texts.login.title, highlight: false }]} />
            </View>
            <View style={styles.inputsContainer}>
                <Input
                    icon={<PhoneIcon />}
                    placeholder={texts.phoneNumber}
                    type="tel"
                    onInputChange={setPhone}
                />
                <Divider size={20} />
                <Input
                    icon={<PasswordIcon />}
                    placeholder={texts.password}
                    password={true}
                    onInputChange={setPassword}
                />
            </View>
            <View style={styles.forgotPassContainer}>
                <Text style={styles.forgotPass} onPress={() => alert("Goto forget password page")}>{texts.forgotPassword}</Text>
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

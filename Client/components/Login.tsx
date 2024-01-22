import { useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Input from '../components/Input';
import Title from '../components/Title';
import { PhoneIcon, PasswordIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';
import { AppleLoginButton, GoogleLoginButton } from '../components/LoginBrandButton';

import { styles } from '../styles/Main';
import { PropNavigation, resetNavigation } from '../App';

export default function Login({ navigation }: PropNavigation) {
    const texts = require("@lang/en.json");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    return (
        <>
            <Title line={[{ text: texts.login.title, highlight: false }]} />
            <View style={{ width: '100%', alignItems: 'center' }}>
                <Input
                    icon={<PhoneIcon />}
                    placeholder={texts.phoneNumber}
                    type="tel"
                    onInputChange={setPhone}
                />
                <Divider height={20} />
                <Input
                    icon={<PasswordIcon />}
                    placeholder={texts.password}
                    password={true}
                    onInputChange={setPassword}
                />
                <Divider height={20} />
                <View style={[styles.w100, styles.alignRight, styles.paddingRight10]}>
                    <Text style={[styles.alignRight, styles.normalText, styles.lightTextColor]} onPress={() => alert("Goto forget password page")}>{texts.forgotPassword}</Text>
                </View>
                <Divider height={20} />
                <Button title={texts.login.button} onPress={() => { alert("Login"); resetNavigation(navigation, 'Tabs') }} />
            </View>
            <TouchableOpacity style={styles.w100} onPress={() => navigation.navigate('Register')}>
                <View style={[styles.w100c, styles.row, styles.alignCenter, styles.justifyCenter]}>
                    <Text style={[styles.hPadding2, styles.normalText, styles.lightTextColor]}>{texts.login.newUser}</Text>
                    <Text style={[styles.redBold, styles.hPadding2, styles.normalText, styles.fontSize18]}>{texts.register.button}</Text>
                </View>
            </TouchableOpacity>
            <View style={[styles.row, styles.w100, styles.alignCenter, styles.justifyCenter, styles.padding15px]}>
                <AppleLoginButton />
                <GoogleLoginButton />
            </View>
        </>
    )
}

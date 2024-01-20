import { useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Input from '../components/Input';
import Title from '../components/Title';
import { PhoneIcon, PasswordIcon, NameIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';

import { styles } from '../styles/Main';

export default function Register() {
    const texts = require("@lang/en.json");
    const [name, setName] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    return (
        <>
            <Title line={[{ text: texts.register.title, highlight: false }]} />
            <View style={{ width: '100%', alignItems: 'center' }}>
                <Input
                    icon={<NameIcon />}
                    placeholder={texts.name}
                    type="text"
                    onInputChange={setName}
                />
                <Divider height={20} />
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
                <Input
                    icon={<PasswordIcon />}
                    placeholder={texts.confirmPassword}
                    password={true}
                    onInputChange={setConfirmPassword}
                />
                <Divider height={40} />
                <Button title={texts.register.button} onPress={() => alert("Login")} />
            </View>
            <TouchableOpacity style={styles.w100} onPress={() => alert("Goto register page")}>
                <View style={[styles.w100c, styles.row, styles.alignCenter, styles.justifyCenter]}>
                    <Text style={[styles.hPadding2, styles.normalText, styles.lightTextColor]}>{texts.register.newUser}</Text>
                    <Text style={[styles.redBold, styles.hPadding2, styles.normalText, styles.fontSize18]}>{texts.login.button}</Text>
                </View>
            </TouchableOpacity>
        </>
    )
}

import { useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Input from '../components/Input';
import Title from '../components/Title';
import { PasswordIcon, NameIcon } from '../components/Icons';
import Divider from '../components/Divider';
import Button from '../components/Button';

import { getStyles } from '../styles/Sign';
import { resetNavigation } from '../App';
import { Props } from '../screens/SignIn';
import { Result, doRegister } from '../utils/ApiRequest';
import { Country } from 'react-native-country-picker-modal';
import PhoneInput from './PhoneInput';

export default function Register({ navigation, toggleNewUser }: Props) {
    const styles = getStyles();
    const texts = require("../langs/en.json");
    const [name, setName] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [nation, setNation] = useState<Country | null | undefined>();

    const register = async () => {
        const result: Result = await doRegister(phone, password, confirmPassword, name);
        if (result.success)
            resetNavigation(navigation, 'Tabs');
        else
            alert(result.message);
    }

    return (
        <>
            <View style={styles.titleContainer} >
                <Title line={[{ text: texts.register.title, highlight: false }]} />
                <Divider size={23} />
                <Input
                    icon={<NameIcon />}
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
                    icon={<PasswordIcon />}
                    placeholder={texts.password}
                    password={true}
                    onInputChange={setPassword}
                />
                <Divider size={19.25} />
                <Input
                    icon={<PasswordIcon />}
                    placeholder={texts.confirmPassword}
                    password={true}
                    onInputChange={setConfirmPassword}
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

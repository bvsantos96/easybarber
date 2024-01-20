import {useState} from 'react';
import {View, ScrollView, Text, Image} from 'react-native';
import Input from '@components/Input';
import Title from '@components/Title';
import {PhoneIcon, PasswordIcon} from '@components/Icons';
import Divider from '@components/Divider';

import {styles, minDimention} from '@styles/main';

export default function Login() {
  const texts = require("@lang/en.json");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  return (
    <ScrollView contentContainerStyle={[styles.container, styles.backgroundMainColor, styles.noOverflow]} keyboardShouldPersistTaps="handled">
        <Image  source={require("@assets/images/logoSmall.png")} 
                style={{ width: minDimention * 0.5, height: minDimention * 0.5, flex: 1 }} 
                resizeMode="contain" 
        />
        <View style={[styles.loginContainer]}>
          <Divider />
          <Title line={[{text: texts.login.title}]}/>
          <View style={{width: '100%', alignItems: 'center'}}>
            <Input 
              icon={<PhoneIcon />}
              placeholder={texts.phoneNumber}
              type="tel" 
              onInputChange={setPhone}
            />
            <Divider />
            <Input 
              icon={<PasswordIcon />}
              placeholder={texts.password}
              type="password" 
              onInputChange={setPassword}
            />
          </View>
          <View style={[styles.w100, styles.alignRight, styles.paddingRight10]}>
            <Text style={styles.alignRight} onPress={()=>alert("Forget password page")}>{texts.forgotPassword}</Text>
          </View>
        </View>
    </ScrollView>
  );
}

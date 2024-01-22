import React, { useRef, FC } from 'react';
import {View, Image} from 'react-native';
import Button from '../components/Button';
import {styles as screenStyles} from '../styles/Screens';
import {styles as mainStyles} from '../styles/Main';
import { textBlackColor } from '../styles/Main';
import {thinBorderBlack, whitheButtonColor} from '../styles/Button'
import Divider from '../components/Divider';

interface InputProps {
  icon: () => React.ReactNode;
  placeholder?: string;
  onInputChange?: (text: string) => void;
  type?: string;
}

const AccountTypeSelection: FC<InputProps> = () => {
  return (
    <View style={mainStyles.containerSpaceBetween}>
      <View/>
      <Image style={screenStyles.centeredLogo} source={require("@assets/images/logo.png")}/>
      <View style={[mainStyles.w100, mainStyles.paddingBottom10]} >
        <Button title="I'm a User" onPress={()=>alert("test")}/>
        <Divider/>
        <Button title="I'm a Barber" backgroundColor={whitheButtonColor} buttonTextColor={textBlackColor} borderColor={thinBorderBlack} onPress={()=>alert("test")}/>
        <Divider/>
      </View>
    </View>
  );
};

export default AccountTypeSelection;

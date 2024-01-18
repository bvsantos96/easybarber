import React, { useRef, FC } from 'react';
import { View, TextInput, TouchableOpacity, StyleProp, ViewStyle, TextInputProps } from 'react-native';
import Button from '@components/Button';
import {styles} from '@styles/main';

interface InputProps {
  icon: () => React.ReactNode;
  placeholder?: string;
  onInputChange?: (text: string) => void;
  type?: string;
}

const AccountTypeSelection: FC<InputProps> = () => {
  return (
    <View style={styles.container}>
      <Button title="I'm a user" onPress={()=>alert("test")}/>
      <Button title="I'm a barber" onPress={()=>alert("test")}/>
    </View>
  );
};

export default AccountTypeSelection;

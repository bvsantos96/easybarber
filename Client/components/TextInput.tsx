import React, { useRef, FC } from 'react';
import { View, TextInput, TouchableOpacity, StyleProp, ViewStyle, TextInputProps } from 'react-native';
import { styles } from '@styles/input';

interface InputProps {
  icon: () => React.ReactNode;
  placeholder?: string;
  onInputChange?: (text: string) => void;
  type?: string;
}

const Input: FC<InputProps> = ({ icon, placeholder = "", onInputChange = () => { alert("No onInputChange passed in props"); }, type = "text" }) => {
  const textInputRef = useRef<TextInput>(null);

  const handleViewPress = () => {
    if (textInputRef.current) {
      textInputRef.current.focus();
    }
  };

  const handleChangeText = (text: string) => {
    if (onInputChange) {
      onInputChange(text);
    }
  };

  return (
    <TouchableOpacity style={styles.container} onPress={handleViewPress}>
      <View
        style={styles.inputView as StyleProp<ViewStyle>}
      >
        <View style={styles.iconView}>
          {icon()}
        </View>
        <TextInput
          ref={textInputRef}
          style={styles.textInput}
          placeholder={placeholder}
          onChangeText={handleChangeText}
          secureTextEntry={type === "password"}
          keyboardType={type === "password" ? "default" : type as TextInputProps['keyboardType']}
        />
      </View>
    </TouchableOpacity>
  );
};

export default Input;

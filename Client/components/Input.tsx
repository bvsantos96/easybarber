import React, { useRef } from 'react';
import { View, TextInput, TouchableOpacity } from 'react-native';
import {styles} from '../styles/input';
import { lightTextColor } from '../styles/main';

// types can be found here: https://reactnative.dev/docs/textinput#autocomplete
const Input = ({icon, placeholder = "", onInputChange=(e: string)=>{alert(`No onInputChange(${e}) passed in props`)}, type="text"}) => {
  const textInputRef = useRef(null);

  const handleViewPress = () => {
    if(textInputRef.current)
      textInputRef.current.focus();
  };

  const handleChangeText = (text: string) => {
    if (onInputChange) {
      onInputChange(text);
    }
  };

  return (
    <TouchableOpacity style={styles.container} onPress={handleViewPress}>
      <View
        style={styles.inputView}
      >
        <View style={styles.iconView}>
        {icon}
        </View>
        <TextInput
          ref={textInputRef}
          style={styles.textInput}
          placeholder={placeholder}
          placeholderTextColor={lightTextColor}
          onChangeText={handleChangeText}
          secureTextEntry={type === "password"}
          inputMode={ type === "password" ? "text" : type }
        />
      </View>
    </TouchableOpacity>
  );
};

export default Input;

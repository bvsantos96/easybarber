import React, { useRef } from 'react';
import { View, TextInput, TouchableOpacity } from 'react-native';
import {styles} from '@styles/input';

const Input = ({icon, placeholder = "", onInputChange=()=>{alert("No onInputChange passed in props")}, type="text"}) => {
  const textInputRef = useRef(null);

  const handleViewPress = () => {
    if(textInputRef.current)
      textInputRef.current.focus();
  };

  const handleChangeText = (text) => {
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
        {icon()}
        </View>
        <TextInput
          ref={textInputRef}
          style={styles.textInput}
          placeholder={placeholder}
          onChangeText={handleChangeText}
          secureTextEntry={type === "password"}
          keyboardType={type === "password"? "default" : type}
        />
      </View>
    </TouchableOpacity>
  );
};

export default Input;

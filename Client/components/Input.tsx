import React, { useEffect, useState } from 'react';
import { View, TextInput, InputModeOptions } from 'react-native';
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/Input';
import { useTheme } from '../styles/ThemeContext';

type InputProps = {
    leftIcon?: JSX.Element,
    placeholder?: string,
    onInputChange?: (e: string) => void,
    type?: InputModeOptions,
    password?: boolean,
    rightIcon?: JSX.Element[],
}

// types can be found here: https://reactnative.dev/docs/textinput#autocomplete
const Input = ({
    leftIcon = <></>,
    placeholder = "",
    onInputChange = (e: string) => { alert(`No onInputChange(${e}) passed in props`) },
    type = "text",
    password = false,
    rightIcon= []
}: InputProps) => {
    const theme = useTheme();
    const styles = getStyles();
    const textInputRef = React.useRef<TextInput>(null);
    const [showPassword, setShowPassword] = useState(!password);

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    const handleChangeText = (text: string) => {
        if (onInputChange) {
            onInputChange(text);
        }
    };

    const handleShowPasswordPress= () => {
        setShowPassword(!showPassword);
    };

    return (
        <Pressable style={styles.container} onPress={handleViewPress}>
            <View style={styles.inputView}>
                <View style={styles.iconView}>
                    {leftIcon}
                </View>
                <TextInput
                    ref={textInputRef}
                    style={rightIcon?styles.textInputWithShowPasswordIcon:styles.textInput}
                    placeholder={placeholder}
                    placeholderTextColor={theme.colors.text.lightBlack}
                    onChangeText={handleChangeText}
                    secureTextEntry={!showPassword}
                    clearTextOnFocus={false}
                    inputMode={type}
                />
                {rightIcon.length >= 0 &&(
                    <Pressable style={styles.showPasswordIcon} onPress={handleShowPasswordPress}>
                        {rightIcon[+showPassword]}
                    </Pressable>
                )}
            </View>
        </Pressable>
    );
};

export default Input;

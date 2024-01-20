import React from 'react';
import { View, TextInput, TouchableOpacity, InputModeOptions } from 'react-native';
import { styles } from '../styles/Input';
import { lightTextColor } from '../styles/Main';

type InputProps = {
    icon?: JSX.Element,
    placeholder?: string,
    onInputChange?: (e: string) => void,
    type?: InputModeOptions,
    password?: boolean
}

// types can be found here: https://reactnative.dev/docs/textinput#autocomplete
const Input = ({
    icon = <></>,
    placeholder = "",
    onInputChange = (e: string) => { alert(`No onInputChange(${e}) passed in props`) },
    type = "text",
    password = false
}: InputProps) => {
    const textInputRef = React.useRef<TextInput>(null);

    const handleViewPress = () => {
        textInputRef.current?.focus();
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
                    secureTextEntry={password}
                    inputMode={type}
                />
            </View>
        </TouchableOpacity>
    );
};

export default Input;

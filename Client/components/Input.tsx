import React, { useRef, useState } from 'react';
import { Pressable, View, TextInput, InputModeOptions, ReturnKeyTypeOptions, NativeSyntheticEvent, TextInputSubmitEditingEventData, TextInputFocusEventData } from 'react-native';
import { getStyles } from '../styles/Input';
import { useTheme } from '../styles/ThemeContext';

type InputProps = {
    onBlur?: | ((e: NativeSyntheticEvent<TextInputFocusEventData>) => void) | undefined;
    onFocus?: | ((e: NativeSyntheticEvent<TextInputFocusEventData>) => void) | undefined;
    leftIcon?: JSX.Element,
    placeholder?: string,
    onInputChange?: (e: string) => void,
    type?: InputModeOptions,
    autoComplete?:
    | 'additional-name'
    | 'address-line1'
    | 'address-line2'
    | 'birthdate-day'
    | 'birthdate-full'
    | 'birthdate-month'
    | 'birthdate-year'
    | 'cc-csc'
    | 'cc-exp'
    | 'cc-exp-day'
    | 'cc-exp-month'
    | 'cc-exp-year'
    | 'cc-number'
    | 'cc-name'
    | 'cc-given-name'
    | 'cc-middle-name'
    | 'cc-family-name'
    | 'cc-type'
    | 'country'
    | 'current-password'
    | 'email'
    | 'family-name'
    | 'gender'
    | 'given-name'
    | 'honorific-prefix'
    | 'honorific-suffix'
    | 'name'
    | 'name-family'
    | 'name-given'
    | 'name-middle'
    | 'name-middle-initial'
    | 'name-prefix'
    | 'name-suffix'
    | 'new-password'
    | 'nickname'
    | 'one-time-code'
    | 'organization'
    | 'organization-title'
    | 'password'
    | 'password-new'
    | 'postal-address'
    | 'postal-address-country'
    | 'postal-address-extended'
    | 'postal-address-extended-postal-code'
    | 'postal-address-locality'
    | 'postal-address-region'
    | 'postal-code'
    | 'street-address'
    | 'sms-otp'
    | 'tel'
    | 'tel-country-code'
    | 'tel-national'
    | 'tel-device'
    | 'url'
    | 'username'
    | 'username-new'
    | 'off'
    | undefined;
    password?: boolean,
    username?: boolean,
    rightIcon?: JSX.Element[],
    onSubmitEditing?: | ((e: NativeSyntheticEvent<TextInputSubmitEditingEventData>) => void) | undefined;
    returnKeyType?: ReturnKeyTypeOptions | undefined;
}

// types can be found here: https://reactnative.dev/docs/textinput#autocomplete

const Input = React.forwardRef<TextInput, InputProps>(({
    onFocus,
    onBlur,
    onSubmitEditing,
    returnKeyType,
    leftIcon,
    placeholder = "",
    onInputChange = (e: string) => { alert(`No onInputChange(${e}) passed in props`) },
    type = "text",
    autoComplete,
    username = false,
    password = false,
    rightIcon = []
}, ref) => {
    const theme = useTheme();
    const styles = getStyles();
    const [showPassword, setShowPassword] = useState(!password);
    const textInputRef = useRef<TextInput>(null);

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    const handleChangeText = (text: string) => {
        if (onInputChange) {
            onInputChange(text);
        }
    };

    const handleShowPasswordPress = () => {
        setShowPassword(!showPassword);
    };

    return (
        <Pressable
            ref={ref}
            style={styles.container} onPress={handleViewPress}>
            <View style={styles.inputView}>
                {leftIcon && <View style={styles.iconView}>
                    {leftIcon}
                </View>}
                <TextInput
                    ref={textInputRef}
                    onFocus={onFocus}
                    style={rightIcon ? styles.textInputWithShowPasswordIcon : styles.textInput}
                    placeholder={placeholder}
                    placeholderTextColor={theme.colors.text.lightBlack}
                    onBlur={onBlur}
                    onChangeText={handleChangeText}
                    secureTextEntry={!showPassword}
                    clearTextOnFocus={false}
                    {...(password ? { autoCompleteType: 'password' } : { autoComplete: autoComplete })}
                    textContentType={password ? 'password' : username ? 'username' : 'none'}
                    inputMode={type}
                    onSubmitEditing={onSubmitEditing}
                    returnKeyType={returnKeyType ? returnKeyType : 'default'}
                />
                {rightIcon && rightIcon.length >= 0 && (
                    <Pressable style={styles.showPasswordIcon} onPress={handleShowPasswordPress}>
                        {rightIcon[+showPassword]}
                    </Pressable>
                )}
            </View>
        </Pressable>
    );
});

export default Input;

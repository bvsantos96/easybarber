import React, { useRef, useState } from 'react';
import { Text, Pressable, View, TextInput, InputModeOptions, ReturnKeyTypeOptions, NativeSyntheticEvent, TextInputSubmitEditingEventData, TextInputFocusEventData, ViewStyle } from 'react-native';
import { getStyles } from '../styles/Input';
import { useTheme } from '../styles/ThemeContext';
import Divider from './Divider';

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
    round?: boolean;
    containerStyle?: ViewStyle;
    placeholderTextColor?: string;
    defaultValue?: string;
    title?: string;
    hideTitleIfNoValue?: boolean;
    preventPaste?: boolean;
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
    rightIcon = [],
    round = true,
    containerStyle,
    placeholderTextColor,
    defaultValue,
    title,
    hideTitleIfNoValue = false,
    preventPaste = false
}, ref) => {
    const theme = useTheme();
    const styles = getStyles();
    const [showPassword, setShowPassword] = useState(!password);
    const textInputRef = useRef<TextInput>(null);
    const [showTitle, setShowTitle] = useState(title && (defaultValue || !hideTitleIfNoValue));

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    const handleChangeText = (text: string) => {
        if (title) {
            if (text.length > 0) {
                setShowTitle(true);
            } else {
                setShowTitle(!hideTitleIfNoValue);
            }
        }
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
            <View style={[styles.inputView, !round && styles.inputSmallBorderRadius, containerStyle && { ...containerStyle }]}>
                {title && showTitle && <Text style={styles.title}>{title}</Text>}
                {leftIcon ? (
                    <View style={styles.iconView}>
                        {leftIcon}
                    </View>
                ) : (
                    <Divider horizontal size={styles.iconView.margin + styles.iconView.marginRight} />
                )}
                <TextInput
                    ref={textInputRef}
                    contextMenuHidden={preventPaste}
                    onFocus={onFocus}
                    style={rightIcon ? styles.textInputWithShowPasswordIcon : styles.textInput}
                    placeholder={placeholder}
                    placeholderTextColor={placeholderTextColor || theme.colors.text.lightBlack}
                    onBlur={onBlur}
                    onChangeText={handleChangeText}
                    secureTextEntry={!showPassword}
                    clearTextOnFocus={false}
                    {...(password ? { autoCompleteType: 'password' } : { autoComplete: autoComplete })}
                    textContentType={password ? 'password' : username ? 'username' : 'none'}
                    inputMode={type}
                    defaultValue={defaultValue}
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


import React, { useRef, useState } from 'react';
import { Text } from 'react-native';
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/Button';
import { useTheme } from '../styles/ThemeContext';
import { getDisabledColor } from 'utils/Utils';

export default function Button({
    backgroundColor = "",
    buttonTextColor = "",
    borderColor = "",
    borderRadius = 50,
    stylesInput = {},
    onPress = () => { alert("onPress button function not passed in props") },
    title = "No button tittle",
    disabled = false,
    preventMultiplePress = false
}) {
    const theme = useTheme();
    const _backgroundColor = backgroundColor === "" ? theme.colors.mainColor : backgroundColor;
    const _borderColor = borderColor === "" ? theme.colors.mainColor : borderColor;
    const _buttonTextColor = buttonTextColor === "" ? theme.colors.backgroundColor : buttonTextColor;
    const styles = getStyles();
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);

    const handlePress = () => {
        if (preventMultiplePress) {
            if (isButtonDisabled) return;
            setIsButtonDisabled(true);
            onPress();

            setTimeout(() => {
                setIsButtonDisabled(false);
            }, 2000);
        } else {
            onPress();
        }
    };

    const disabledTextColor = useRef(getDisabledColor(_buttonTextColor));
    return (
        <Pressable
            style={[
                styles.button,
                {
                    backgroundColor: _backgroundColor,
                    borderWidth: 1,
                    borderColor: _borderColor,
                    borderRadius: borderRadius
                },
                stylesInput,
            ]}
            disabled={disabled}
            onPress={() => {
                if (!disabled) {
                    handlePress();
                }
            }}>
            <Text style={[styles.textButton, { color: disabled ? disabledTextColor.current : _buttonTextColor }]}>{title}</Text>
        </Pressable>
    );
}

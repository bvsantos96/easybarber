import React from 'react';
import { Text } from 'react-native';
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/Button';
import { useTheme } from '../styles/ThemeContext';

export default function Button({ backgroundColor = "", buttonTextColor = "", borderColor = "", borderRadius = 50, stylesInput = {}, onPress = () => { alert("onPress button function not passed in props") }, title = "No button tittle" }) {
    const theme = useTheme();
    const _backgroundColor = backgroundColor === "" ? theme.colors.mainColor : backgroundColor;
    const _borderColor = borderColor === "" ? theme.colors.mainColor : borderColor;
    const _buttonTextColor = buttonTextColor === "" ? theme.colors.backgroundColor : borderColor;
    const styles = getStyles();
    return (
        <Pressable style={[styles.button, { backgroundColor: _backgroundColor, borderWidth: 1, borderColor: _borderColor, borderRadius: borderRadius }, stylesInput]} onPress={onPress}>
            <Text style={[styles.textButton, { color: _buttonTextColor }]}>{title}</Text>
        </Pressable>
    );
}

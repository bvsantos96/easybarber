import React, { ReactNode } from 'react';
import { Pressable as DefaultPressable, StyleProp, ViewStyle } from 'react-native';
import { useTheme } from '../styles/ThemeContext';

interface MyPressableProps {
    style?: StyleProp<ViewStyle>; 
    onPress: () => void;
    children: ReactNode | ReactNode[];
    shadow?: boolean;
}

export default function Pressable({ style = {}, onPress = () => { }, children = <></>, shadow = false }: MyPressableProps) {
    const theme = useTheme();
    return (
        <DefaultPressable style={({ pressed }) => [style, { opacity: pressed ? 0.5 : 1 }, shadow ? theme.shadow : undefined]} onPress={onPress}>
            {Array.isArray(children) ? children : [children]}
        </DefaultPressable>
    );
}


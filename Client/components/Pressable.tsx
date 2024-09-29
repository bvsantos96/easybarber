import React, { ReactNode } from 'react';
import { Pressable as DefaultPressable, StyleProp, ViewStyle, LayoutChangeEvent } from 'react-native';
import { useTheme } from '../styles/ThemeContext';

interface MyPressableProps {
    style?: StyleProp<ViewStyle>;
    onPress: () => void;
    children: ReactNode | ReactNode[];
    shadow?: boolean;
    disabled?: boolean;
    onLayout?: (event: LayoutChangeEvent) => void;
}

export default function Pressable({
    style = {},
    onPress = () => { },
    children = <></>,
    shadow = false,
    disabled = false,
    onLayout,
}: MyPressableProps) {
    const theme = useTheme();
    return (
        <DefaultPressable
            disabled={disabled}
            style={({ pressed }) => [
                style,
                { opacity: (disabled || pressed) ? 0.5 : 1 },
                shadow ? theme.shadow : undefined,
            ]}
            onPress={onPress}
            onLayout={onLayout}
        >
            {children}
        </DefaultPressable>
    );
}

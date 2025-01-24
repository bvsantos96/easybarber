import React, { ReactNode } from 'react';
import { Pressable as DefaultPressable, StyleProp, ViewStyle, LayoutChangeEvent } from 'react-native';
import { useTheme } from '../styles/ThemeContext';
import { LinearGradient } from 'expo-linear-gradient';

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
                { opacity: (disabled || pressed) ? 0.5 : 1 },
            ]}
            onPress={onPress}
            onLayout={onLayout}
        >
            <LinearGradient
                start={[0, 1]}
                end={[1, 0]}
                colors={theme.colors.gradientColors}
                style={[style, shadow ? theme.shadow : undefined]}
            >
                {children}
            </LinearGradient>
        </DefaultPressable>
    );
}

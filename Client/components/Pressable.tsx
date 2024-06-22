import React, { ReactNode } from 'react';
import { Pressable as DefaultPressable, StyleProp, ViewStyle, LayoutChangeEvent } from 'react-native';
import { useTheme } from '../styles/ThemeContext';

interface MyPressableProps {
    style?: StyleProp<ViewStyle>;
    onPress: () => void;
    children: ReactNode | ReactNode[];
    shadow?: boolean;
    onLayout?: (event: LayoutChangeEvent) => void;
}

export default function Pressable({
    style = {},
    onPress = () => { },
    children = <></>,
    shadow = false,
    onLayout,
}: MyPressableProps) {
    const theme = useTheme();
    return (
        <DefaultPressable
            style={({ pressed }) => [
                style,
                { opacity: pressed ? 0.5 : 1 },
                shadow ? theme.shadow : undefined,
            ]}
            onPress={onPress}
            onLayout={onLayout}
        >
            {children}
        </DefaultPressable>
    );
}

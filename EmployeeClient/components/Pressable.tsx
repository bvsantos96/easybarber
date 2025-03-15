import { LinearGradient } from 'expo-linear-gradient';
import React, { ReactNode, useRef } from 'react';
import { Pressable as DefaultPressable, LayoutChangeEvent, StyleProp, ViewStyle } from 'react-native';
import { getDisabledColor, getStyleValue } from 'utils/Utils';
import { useTheme } from '../styles/ThemeContext';

interface MyPressableProps {
    style?: StyleProp<ViewStyle>;
    onPress: () => void;
    children: ReactNode | ReactNode[];
    shadow?: boolean;
    disabled?: boolean;
    onLayout?: (event: LayoutChangeEvent) => void;
    useGradient?: boolean;
}

export default function Pressable({
    style = {},
    onPress = () => { },
    children = <></>,
    shadow = false,
    disabled = false,
    onLayout,
    useGradient = false
}: MyPressableProps) {
    const theme = useTheme();
    const disabledBackgroundColor = useRef(getDisabledColor(getStyleValue("backgroundColor", style)));
    const disabledBorderColor = useRef(getDisabledColor(getStyleValue("borderColor", style)));
    return (
        <DefaultPressable
            disabled={disabled}
            style={({ pressed }) => [
                { opacity: pressed ? 0.5 : 1 },
                !useGradient && style,
                (disabled) && { backgroundColor: disabledBackgroundColor.current, borderColor: disabledBorderColor.current },
                (!useGradient && shadow) ? theme.shadow : undefined,
            ]}
            onPress={onPress}
            onLayout={onLayout}
        >
            {!useGradient ?
                (
                    children
                )
                :
                (
                    <LinearGradient
                        start={[0, 1]}
                        end={[1, 0]}
                        colors={theme.colors.gradientColors}
                        style={style}
                    >
                        {children}
                    </LinearGradient>
                )
            }
        </DefaultPressable>
    );
}

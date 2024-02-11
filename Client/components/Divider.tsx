import React from 'react';
import { View } from 'react-native';
import { useTheme } from '../styles/ThemeContext';

type DividerProps = {
    size?: number,
    color?: string,
    horizontal?: boolean
}

const Divider = ({ size = 10, color = undefined, horizontal = false }: DividerProps) => {
    const theme = useTheme();
    const sizeStyle= horizontal ?
        { width: size * theme.dimensions.absoluteWidth, height: 1 } : 
        { width: 1, height: size * theme.dimensions.absoluteHeight };
    return (
        <View style={[
            sizeStyle,
            { alignSelf: 'stretch', backgroundColor: color ?? "transparent" }
        ]} />
    );
};

export default Divider;

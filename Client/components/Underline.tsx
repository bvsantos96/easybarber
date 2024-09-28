import React from 'react';
import { View } from 'react-native';
import { useTheme } from '../styles/ThemeContext';

export const Underline = () => {
    const theme = useTheme();
    return (
        <View style={[{ width: 45 * theme.dimensions.absoluteWidth, height: 2.5 * theme.dimensions.absoluteHeight, borderRadius: 5 }, { backgroundColor: theme.colors.mainColor }]} />
    );
}

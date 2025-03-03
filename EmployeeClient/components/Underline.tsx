import React from 'react';
import { View } from 'react-native';
import { useTheme } from '../styles/ThemeContext';

export const Underline = () => {
    const theme = useTheme();
    return (
        <View style={{ flexDirection: 'row' }}>
            <View style={[{ flex: 0.85, height: 2.5 * theme.dimensions.absoluteHeight, borderRadius: 5 }, { backgroundColor: theme.colors.mainColor }]} />
        </View>
    );
}

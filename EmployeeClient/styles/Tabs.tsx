import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        bottomButton: {
            backgroundColor: theme.colors.mainColor,
            color: theme.colors.backgroundColor,
            minWidth: 25 * theme.dimensions.absoluteWidth,
            borderRadius: 25 * theme.dimensions.absoluteHeight,
            width: 50 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
            justifyContent: 'center',
        },
    });
}

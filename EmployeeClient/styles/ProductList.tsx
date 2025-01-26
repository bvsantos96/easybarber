import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        listContainer: {
            position: 'absolute',
            left: 19 * theme.dimensions.absoluteWidth,
            top: 40 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            height: 520 * theme.dimensions.absoluteHeight,
        },
    })
};

import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
    return StyleSheet.create({
        calendar: {
            position: "absolute",
            top: 40 * theme.dimensions.absoluteHeight,
            left: padding,
            width: theme.dimensions.width - padding * 2,
            maxHeight: 240 * theme.dimensions.absoluteHeight,
        },
        slotsContainer: {
            position: 'absolute',
            top: 340 * theme.dimensions.absoluteHeight,
            left: 2 * padding,
            width: theme.dimensions.width - (padding * 4),
            maxHeight: 170 * theme.dimensions.absoluteHeight,
        },
        slotsTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._16,
            fontWeight: 600,
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.02,
        },
    });
}

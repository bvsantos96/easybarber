import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: theme.dimensions.input.width,
            height: theme.dimensions.input.height,
        },
        weekdaysContainer: {
            alignSelf: 'center',
            width: 366 * theme.dimensions.absoluteWidth,
            height: 79 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            justifyContent: 'space-between',
            marginTop: 10 * theme.dimensions.absoluteHeight,
        },
        weekdayContainer: {
            width: 48 * theme.dimensions.absoluteWidth,
            height: 79 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.imageBackground,
            borderRadius: 10 * theme.dimensions.absoluteWidth,
            alignItems: 'center',
            justifyContent: 'center',
        },
        weekdaySelected: {
            backgroundColor: theme.colors.mainColor,
        },
        weekdayText: {
            color: theme.colors.mainColor,
            fontSize: theme.fonts.size._15,
            fontFamily: 'Mazzard',
            fontWeight: "900",
        },
        weekdayTextSelected: {
            color: theme.colors.text.alt,
        },
    });
}

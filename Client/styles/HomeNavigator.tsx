import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            minHeight: theme.dimensions.height,
        },
        goBack: {
            borderRadius: (35 / 2) * theme.dimensions.absoluteWidth,
            minWidth: 35 * theme.dimensions.absoluteWidth,
            minHeight: 35 * theme.dimensions.absoluteWidth,
            justifyContent: 'center',
            alignItems: 'center',
            borderWidth: 1,
        },
        goBackIcon: {
            width: 24,
        },
        header: {
            justifyContent: 'center',
            alignItems: 'center',
            height: 112 * theme.dimensions.absoluteHeight,
            ...theme.shadow as any,
            backgroundColor: theme.colors.backgroundColor,
        },
        headerContainer: {
            position: 'absolute',
            top: 60 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'space-between',
            width: theme.dimensions.width - (44 * theme.dimensions.absoluteWidth),
            left: 22 * theme.dimensions.absoluteWidth,
        },
        headerTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._16,
            fontWeight: 600,
            lineHeight: 27 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
        },
        headerFiller: {
            minWidth: 35 * theme.dimensions.absoluteWidth,
            minHeight: 35 * theme.dimensions.absoluteHeight,
        }
    });
}

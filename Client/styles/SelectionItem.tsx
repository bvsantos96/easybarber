import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: '100%',
            height: 93 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
            backgroundColor: theme.colors.backgroundColor,
            marginVertical: 10 * theme.dimensions.absoluteHeight,
        },
        image: {
            position: 'absolute',
            top: 0,
            left: 0,
            width: 117 * theme.dimensions.absoluteHeight,
            height: 93 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
        },
        radioContainerSelectod: {
            borderColor: theme.colors.mainColor,
        },
        radioContainer: {
            position: 'absolute',
            right: 28 * theme.dimensions.absoluteWidth,
            top: 34 * theme.dimensions.absoluteHeight,
            width: 26 * theme.dimensions.absoluteHeight,
            height: 26 * theme.dimensions.absoluteHeight,
            borderRadius: 13 * theme.dimensions.absoluteHeight,
            borderWidth: 1,
            justifyContent: 'center',
            alignItems: 'center',
        },
        radio: {
            width: 24 * theme.dimensions.absoluteHeight,
            height: 24 * theme.dimensions.absoluteHeight,
            color: theme.colors.mainColor,
        },
    })
}

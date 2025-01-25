import { StyleSheet } from 'react-native';
import { useTheme } from '@styles/ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: 150,
            height: 100 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
            backgroundColor: theme.colors.backgroundColor,
            marginVertical: 10 * theme.dimensions.absoluteHeight,
            alignSelf: 'center',
        },
        image: {
            position: 'absolute',
            top: 0,
            left: 0,
            width: 117 * theme.dimensions.absoluteHeight,
            height: 100 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
        }
    })
}

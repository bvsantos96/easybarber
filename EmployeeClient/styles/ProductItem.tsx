import { StyleSheet } from 'react-native';
import { useTheme } from '@styles/ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: 160*theme.dimensions.absoluteWidth,
            height: 215 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
            backgroundColor: 'white',
            marginVertical: 10 * theme.dimensions.absoluteHeight,
            alignSelf: 'center',
        },
        image: {
            position: 'absolute',
            top: 35 * theme.dimensions.absoluteHeight,
            left: 16 * theme.dimensions.absoluteWidth,
            width: 129 * theme.dimensions.absoluteHeight,
            height: 65 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
        }
    })
}

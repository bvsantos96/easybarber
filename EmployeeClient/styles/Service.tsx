import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
    return StyleSheet.create({
        container: {
            width: "100%",
            height: "100%",
        },
        imageContainer: {
            position: 'absolute',
            width: 356 * theme.dimensions.absoluteWidth,
            height: 190 * theme.dimensions.absoluteHeight,
            top: padding,
            left: padding,
            borderRadius: padding,
        },
        imageStyle: {
            position: 'absolute',
            width: '100%',
            height: '100%',
            left: 0,
            borderRadius: 11,
        },
    });
}

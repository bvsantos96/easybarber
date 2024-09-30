import { StyleSheet } from "react-native";
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
    return StyleSheet.create({
        container: {
            flex: 1,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
            textAlign: 'center',
            alignSelf: 'center',
            alignItems: 'center',
            alignContent: 'center',
        },
        button: {
            position: 'absolute',
            bottom: 145 * theme.dimensions.absoluteHeight,
            left: padding,
            width: theme.dimensions.width - 2 * padding,
        },
        selectTextContainer: {
            position: 'absolute',
            top: 20 * theme.dimensions.absoluteHeight,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._14,
            fontWeight: 400,
            lineHeight: 21 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
            alignSelf: 'center',
            justifyContent: 'center',
        },
    });
}

import { StyleSheet } from "react-native";
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            width: "100%",
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
            textAlign: 'center',
            alignSelf: 'center',
            alignItems: 'center',
            alignContent: 'center',
        },
        button: {
            position: 'absolute',
            bottom: 45 * theme.dimensions.absoluteHeight,
            width: "88%",
            alignItems: 'center',
        },
        selectTextContainer: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._14,
            fontWeight: 400,
            lineHeight: 21 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
            alignSelf: 'center',
            justifyContent: 'center',
        },
        textContainer: {
            width: "88%",
            alignItems: 'center',
        },
    });
}

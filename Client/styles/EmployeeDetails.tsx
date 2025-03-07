import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();

    return StyleSheet.create({
        container: {
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
            textAlign: 'center',
            alignSelf: 'center',
            alignItems: 'center',
            alignContent: 'center',
        },
        title: {
            fontSize: theme.fonts.size._20,
            fontFamily: 'Poppins',
            fontWeight: 'bold',
            lineHeight: 25 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.17 * theme.dimensions.absoluteWidth,
            color: theme.colors.text.black,
        }
    });
}

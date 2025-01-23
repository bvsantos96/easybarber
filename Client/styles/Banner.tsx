import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        banner: {
            position: 'absolute',
            top: theme.dimensions.statusBarHeight + 10 * theme.dimensions.absoluteHeight,
            left: 2 * theme.dimensions.absoluteWidth,
            right: 2 * theme.dimensions.absoluteWidth,
            borderRadius: 8,
            padding: 16,
            zIndex: 1000,
            elevation: 5,
            shadowColor: '#000',
            shadowOffset: {
                width: 0,
                height: 2,
            },
            shadowOpacity: 0.25,
            shadowRadius: 3.84,
        },
        text: {
            color: '#FFFFFF',
            textAlign: 'center',
            fontSize: 16,
        },
    });
};

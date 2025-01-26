import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        movingContainer: {
            elevation: 2,
            zIndex: 2,
        },
        backContainer: {
            position: "absolute",
            backgroundColor: theme.colors.text.lightGray,
            justifyContent: "flex-end",
            alignItems: "center",
            width: "100%",
            height: "100%",
        },
        itemContainer: {
            borderRadius: 11 * theme.dimensions.absoluteWidth,
        },
        shadow: {
            ...theme.shadow as any,
        },
    });
}

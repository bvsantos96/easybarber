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
        icon: {
            zIndex: 3,
            elevation: 2,
            borderWidth: 1,
            borderRadius: 50 * theme.dimensions.absoluteWidth,
            padding: 10 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteWidth,
            width: 50 * theme.dimensions.absoluteWidth,
            alignItems: "center",
            justifyContent: "center",
            fontSize: 24 * theme.dimensions.absoluteWidth,
        },
        redIcon: {
            backgroundColor: theme.colors.mainColor,
            borderColor: theme.colors.mainColor,
        },
        items: {
            flexDirection: "row",
            right: 5 * theme.dimensions.absoluteWidth,
            gap: 5 * theme.dimensions.absoluteWidth,
        }
    });
}

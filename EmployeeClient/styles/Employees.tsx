import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
        },
        listContainer: {
            position: "absolute",
            top: 70 * theme.dimensions.absoluteHeight,
            height: theme.dimensions.height - 142 * theme.dimensions.absoluteHeight - theme.dimensions.tabHeight,
            width: theme.dimensions.width,
            bottom: 0,
            alignItems: "center",
            justifyContent: "center",
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
        listItemContainer: {
            width: theme.dimensions.absoluteWidth * 350,
            height: theme.dimensions.absoluteHeight * 65,
            backgroundColor: "teal"
        },
    });
}

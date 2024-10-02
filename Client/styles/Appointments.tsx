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
            left: 25 * theme.dimensions.absoluteWidth,
            height: theme.dimensions.height - 142 * theme.dimensions.absoluteHeight - theme.dimensions.tabHeight,
            width: theme.dimensions.width - 50 * theme.dimensions.absoluteWidth,
            bottom: 0,
            alignItems: "center",
        },
        itemContainer: {
            width: "99%",
            height: 110 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 11 * theme.dimensions.absoluteWidth,
            marginLeft: 1 * theme.dimensions.absoluteWidth,
            marginTop: 18 * theme.dimensions.absoluteHeight,
        },
        shadow: {
            ...theme.shadow as any,
        },
        imageContainer: {
            position: "absolute",
            top: 10 * theme.dimensions.absoluteHeight,
            left: 10 * theme.dimensions.absoluteWidth,
            bottom: 10 * theme.dimensions.absoluteHeight,
        },
        imageStyle: {
            width: 90 * theme.dimensions.absoluteWidth,
            height: 90 * theme.dimensions.absoluteHeight,
            borderRadius: 11 * theme.dimensions.absoluteWidth,
        },
        textContainer: {
            position: "absolute",
            top: 15 * theme.dimensions.absoluteHeight,
            left: 115 * theme.dimensions.absoluteWidth,
            right: 32 * theme.dimensions.absoluteWidth,
            bottom: 30 * theme.dimensions.absoluteHeight,
        },
        subTitleContainer: {
            marginLeft: 3 * theme.dimensions.absoluteWidth,
        },
        title: {
            color: theme.colors.text.main,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._15,
            fontWeight: '600',
            lineHeight: theme.fonts.size._24,
            letterSpacing: 0,
            textAlign: 'left',
        },
        locationContainer: {
            backgroundColor: theme.colors.borderAlt,
            alignItems: "center",
            justifyContent: "center",
            borderRadius: 50 * theme.dimensions.absoluteWidth,
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,
            marginRight: 7 * theme.dimensions.absoluteWidth,
        },
        infoContainer: {
            position: "absolute",
            top: 47 * theme.dimensions.absoluteHeight,
            left: 0,
            flexDirection: "row",
            alignItems: "center",
            paddingTop: 5 * theme.dimensions.absoluteHeight,
            paddingBottom: 5 * theme.dimensions.absoluteHeight,
        },
        locationIcon: {
            width: 15 * theme.dimensions.absoluteWidth,
            height: 15 * theme.dimensions.absoluteHeight,
        },
        locationText: {
            color: theme.colors.text.lightBlack,
            fontFamily: 'Mazzard',
            fontSize: theme.fonts.size._13,
            fontWeight: '400',
            lineHeight: theme.fonts.size._21,
            letterSpacing: 1,
        },
        titleContainer: {
            position: "absolute",
            top: 20 * theme.dimensions.absoluteHeight,
            left: 31 * theme.dimensions.absoluteWidth,
            height: 49 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            justifyContent: "flex-start",
            alignItems: "flex-start",
        },
        titleText: {
            color: theme.colors.text.main,
            fontFamily: 'Mazzard',
            fontSize: theme.fonts.size._17,
            fontWeight: '600',
            lineHeight: theme.fonts.size._34,
            letterSpacing: 1,
            textAlign: 'left',
        },
    });
}

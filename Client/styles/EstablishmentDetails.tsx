import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
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
            fontWeight: '600',
            lineHeight: 25 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.17 * theme.dimensions.absoluteWidth,
            color: theme.colors.text.black,
        },
        imageStyle: {
            position: 'absolute',
            width: 356 * theme.dimensions.absoluteWidth,
            height: 200 * theme.dimensions.absoluteHeight,
            top: padding,
            left: padding,
            borderRadius: padding,
        },
        nameContainer: {
            position: 'absolute',
            top: 225 * theme.dimensions.absoluteHeight,
            left: padding,
        },
        name: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._18,
            fontWeight: 600,
            lineHeight: 30 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.16500000655651093 * theme.dimensions.absoluteWidth,
            textAlign: 'left',
        },
        address: {
            position: 'absolute',
            top: 22 * theme.dimensions.absoluteHeight,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._12,
            fontWeight: 300,
            lineHeight: 30 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.16500000655651093 * theme.dimensions.absoluteWidth,
            textAlign: 'left',
            textDecorationLine: 'underline',
            color: theme.colors.mainColor
        },
        serviceTitleContainer: {
            position: 'absolute',
            top: 290 * theme.dimensions.absoluteHeight,
            left: padding,
        },
        serviceTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._16,
            fontWeight: 600,
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.16500000655651093 * theme.dimensions.absoluteWidth,
            textAlign: "left",
        },
        servicesContainer: {
            position: 'absolute',
            top: 330 * theme.dimensions.absoluteHeight,
            left: padding,
            width: "100%",
            flexDirection: 'row',
            overflow: 'hidden',
        },
        aboutTitleContainer: {
            position: 'absolute',
            top: 415 * theme.dimensions.absoluteHeight,
            left: padding
        },
        aboutTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._16,
            fontWeight: 600,
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.16500000655651093 * theme.dimensions.absoluteWidth,
            textAlign: 'left',
        },
        aboutText: {
            position: 'absolute',
            top: 450 * theme.dimensions.absoluteHeight,
            left: padding,
            maxWidth: theme.dimensions.width - 2 * padding,
            maxHeight: 95 * theme.dimensions.absoluteHeight,
        },
        button: {
            position: 'absolute',
            bottom: 235 * theme.dimensions.absoluteHeight,
            left: padding,
            width: theme.dimensions.width - 2 * padding,
        },
        categoryIcon: {
            width: 20 * theme.dimensions.absoluteWidth,
            height: 20 * theme.dimensions.absoluteHeight,
        },
        alignCenter: {
            textAlign: 'center',
            alignSelf: 'center',
            alignItems: 'center',
            alignContent: 'center',
        }
    });
}

import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            position: 'absolute',
            top: 0,
            left: 0,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
        },
        modalOverlay: {
            position: 'absolute',
            top: 0,
            left: 0,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: 'rgba(0,0,0,0.7)',
            justifyContent: 'center',
            alignItems: 'center',
            elevation: 100,
            zIndex: 100
        },
        alertBox: {
            justifyContent: 'center',
            alignItems: 'center',
            width: 318.64 * theme.dimensions.absoluteWidth,
            maxHeight: theme.dimensions.height * 0.8,
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: (318.64 * theme.dimensions.absoluteWidth) * 0.045
        },
        alertView: {
            justifyContent: 'center',
            alignItems: 'center',
            width: "90%",
        },
        message: {
            color: theme.colors.text.black,
            fontSize: theme.fonts.size._18,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 25.2 * theme.dimensions.absoluteHeight,
            textAlign: 'center'
        },
        message2: {
            fontSize: theme.fonts.size._14,
        },
        buttonWrapperRight: {
            flex: 1,
            marginLeft: 2.5,
        },
        buttonWrapperLeft: {
            flex: 1,
            marginRight: 2.5,
        },
        buttonContainer: {
            height: 48.75 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            justifyContent: 'space-between',
        },
        button: {
            width: "100%",
            height: "100%",
        },
        button2: {
            backgroundColor: theme.colors.button.alt,
            color: theme.colors.mainColor
        },
        image: {
            width: 182.7 * theme.dimensions.absoluteWidth,
            height: 133.54 * theme.dimensions.absoluteHeight,
            zIndex: 9999999
        },
        votingContainer: {
            width: "80%",
            alignItems: 'center'
        },
        center: {
            justifyContent: 'center',
            alignItems: 'center'
        },
    })
};

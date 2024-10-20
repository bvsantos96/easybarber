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
            position: 'absolute',
            width: 318.64 * theme.dimensions.absoluteWidth,
            height: 406.19 * theme.dimensions.absoluteHeight,
            top: 200.09 * theme.dimensions.absoluteHeight,
            left: 35.68 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: (318.64 * theme.dimensions.absoluteWidth) * 0.045
        },
        messageContainer: {
            position: 'absolute',
            top: 210.86 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
            width: 318.72 * theme.dimensions.absoluteWidth
        },
        message: {
            color: theme.colors.text.black,
            fontSize: theme.fonts.size._18,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 25.2 * theme.dimensions.absoluteHeight,
            textAlign: 'center'
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
            position: 'absolute',
            top: 309.62 * theme.dimensions.absoluteHeight,
            left: 35.25 * theme.dimensions.absoluteWidth,
            width: 249.5 * theme.dimensions.absoluteWidth,
            height: 58.75 * theme.dimensions.absoluteHeight,
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
            position: 'absolute',
            width: 182.97 * theme.dimensions.absoluteWidth,
            height: 133.54 * theme.dimensions.absoluteHeight,
            left: 69.00 * theme.dimensions.absoluteWidth,
            top: 39.08 * theme.dimensions.absoluteHeight,
            zIndex: 9999999
        }
    })
};

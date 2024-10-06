import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            width: '80%',
            height: '100%',
            justifyContent: 'center',
            alignItems: 'center',
        },
        movingBox: {
            position: "absolute",
            borderRadius: 50,
            zIndex: 2,
            top: 0,
            left: 0,
            width: "53%",
            height: "100%",
            backgroundColor: theme.colors.mainColor,
        },
        box: {
            position: "absolute",
            zIndex: 1,
            top: 0,
            borderRadius: 50,
            width: '100%',
            height: "100%",
            backgroundColor: "rgba(223, 34, 56, 0.22)",
        },
        textContainer: {
            zIndex: 3,
            width: "100%",
            height: "100%",
            flexDirection: "row",
            justifyContent: "center",
            alignItems: "center",
        },
        textLeftContainer: {
            height: "100%",
            width: "50%",
            justifyContent: "center",
            alignItems: "center",
        },
        textRightContainer: {
            width: "50%",
            height: "100%",
            justifyContent: "center",
            alignItems: "center",
        },
        textSelected: {
            fontFamily: 'Open Sans',
            fontSize: theme.fonts.size._15,
            fontWeight: 700,
            lineHeight: 21.79 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
            color: theme.colors.button.alt,
        },
        textUnselected: {
            fontFamily: "Open Sans",
            fontSize: theme.fonts.size._15,
            fontWeight: 600,
            lineHeight: 21.79 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
        }
    });
}

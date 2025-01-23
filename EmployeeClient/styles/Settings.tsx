import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            width: theme.dimensions.width,
            backgroundColor: theme.colors.backgroundColor,
        },
        logOutContainer: {
            position: "absolute",
            left: 147 * theme.dimensions.absoluteWidth,
            bottom: 45 * theme.dimensions.absoluteHeight,
            flexDirection: "row",
            alignItems: "center",
            justifyContent: "space-between",
        },
        logOutText: {
            fontFamily: "Poppins",
            fontSize: theme.fonts.size._17,
            fontWeight: 400,
            lineHeight: 24.86 * theme.dimensions.absoluteHeight,
            textAlign: "left",
            color: theme.colors.mainColor
        },
        logOutIcon: {
            width: 29 * theme.dimensions.absoluteWidth,
            height: 29 * theme.dimensions.absoluteHeight,
            color: theme.colors.mainColor
        },
        listItemContainer: {
            flexDirection: "row",
            alignSelf: "center",
            width: theme.dimensions.width * 0.9,
            height: 76 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.backgroundColor,
            alignItems: "center",
            borderBottomWidth: 1,
            borderBottomColor: theme.colors.text.lightGray,
        },
        listItemText: {
            fontFamily: "Poppins",
            fontSize: theme.fonts.size._14,
            fontWeight: 600,
            lineHeight: 20 * theme.dimensions.absoluteHeight,
            textAlign: "left",
            color: theme.colors.text.black,
        },
        iconContainer: {
            width: 40 * theme.dimensions.absoluteWidth,
            height: 40 * theme.dimensions.absoluteHeight,
            justifyContent: "center",
            alignItems: "center",
            borderRadius: 10 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.iconBackground
        },
        arrow: {
            marginLeft: "auto",
            justifyContent: "center",
            alignItems: "center",
        }
    });
}

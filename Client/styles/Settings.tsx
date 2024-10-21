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
            bottom: 167 * theme.dimensions.absoluteHeight,
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
        }
    });
}

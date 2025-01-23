import { StyleSheet } from 'react-native';
import { useTheme } from "./ThemeContext"

export const getStyles = () => {
    const theme = useTheme()
    return StyleSheet.create({
        container: {
            width: theme.dimensions.width,
            flexGrow: 1,
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: theme.colors.backgroundColor,
        },
        listContainer: {
            width: 360 * theme.dimensions.absoluteWidth,
            justifyContent: 'center',
            alignItems: 'center',
            flexGrow: 1,
        },
    });
}

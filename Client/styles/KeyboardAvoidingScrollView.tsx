import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        scrollContainer: {
            flexGrow: 1,
        },
        avoidingKeyboard: {
            width: theme.dimensions.width,
            alignItems: "center"
        },
        fixBottom: {
            position: "absolute",
            bottom: 0,
        },
    })
};

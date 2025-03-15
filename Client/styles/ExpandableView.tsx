import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';
export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        titleContainer: {
            flexDirection: 'row',
            width: '100%',
        },
        titleText: {
            alignSelf: 'flex-start',
            justifyContent: 'flex-start',
            color: theme.colors.text.main,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._18,
            fontWeight: 'bold',
            lineHeight: 27 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.17 * theme.dimensions.absoluteWidth,
        },
        expandContainer: {
            marginLeft: 'auto'
        },
        expandText: {
            color: theme.colors.text.main,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._18,
            fontWeight: 'normal',
            lineHeight: 27 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.17 * theme.dimensions.absoluteWidth,
        },
    });
}

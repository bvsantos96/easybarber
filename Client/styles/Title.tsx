import { Platform, StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        titleLine: {
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
        },
        text: {
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._25,
            fontFamily: 'Mazzard',
            fontWeight: '600',
            lineHeight: 33 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.25,
        },
        hText: {
            color: theme.colors.backgroundColor,
            fontSize: theme.fonts.size._25,
            fontFamily: 'Mazzard',
            fontWeight: '600',
            lineHeight: 33 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.25,
            backgroundColor: theme.colors.mainColor,
            borderRadius: 15, 
            overflow: Platform.OS === "ios" ? "hidden" : "visible",
            marginLeft: 0,
            marginRight: 5,
            paddingHorizontal: 10,
        },
        subtitle: {
            color: theme.colors.text.secondary,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Mazzard',
            fontWeight: '400',
            lineHeight: 23 * theme.dimensions.absoluteHeight,
        },
    })
};

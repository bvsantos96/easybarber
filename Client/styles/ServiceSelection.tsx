import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
    return StyleSheet.create({
        container: {
            flex: 1,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
        },
        button: {
            position: 'absolute',
            bottom: 145 * theme.dimensions.absoluteHeight,
            left: padding,
            width: theme.dimensions.width - 2 * padding,
        },
        selectTextContainer: {
            position: 'absolute',
            top: 20 * theme.dimensions.absoluteHeight,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._14,
            fontWeight: 400,
            lineHeight: 21 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
            alignSelf: 'center',
            justifyContent: 'center',
        },
        textContainer: {
            position: 'absolute',
            top: 10 * theme.dimensions.absoluteHeight,
            left: 141 * theme.dimensions.absoluteWidth,
            width: 130 * theme.dimensions.absoluteWidth,
        },
        titleContainer: {
            position: 'absolute',
            left: 141 * theme.dimensions.absoluteWidth,
            width: 130 * theme.dimensions.absoluteWidth,
            height: 93 * theme.dimensions.absoluteHeight,
        },
        title: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._16,
            fontWeight: 900,
            lineHeight: 21 * theme.dimensions.absoluteHeight,
        },
        singleTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._18,
            fontWeight: 900,
            lineHeight: 21 * theme.dimensions.absoluteHeight,
        },
        description: {
            top: 2 * theme.dimensions.absoluteHeight,
            fontFamily: 'Nunito',
            fontSize: theme.fonts.size._12,
            fontWeight: 300,
            lineHeight: 16.37 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.16500000655651093 * theme.dimensions.absoluteWidth,
            textAlign: 'left',
        },
        price: {
            position: 'absolute',
            top: 51 * theme.dimensions.absoluteHeight,
            fontFamily: 'Nunito',
            fontSize: theme.fonts.size._16,
            fontWeight: 900,
            lineHeight: 21.82 * theme.dimensions.absoluteHeight,
            letterSpacing: -0.16500000655651093 * theme.dimensions.absoluteWidth,
            textAlign: 'left',
        },
        listContainer: {
            position: 'absolute',
            top: 50 * theme.dimensions.absoluteHeight,
            width: 335 * theme.dimensions.absoluteWidth,
            height: 470 * theme.dimensions.absoluteHeight,
            left: 29 * theme.dimensions.absoluteWidth,
        },
    })
};

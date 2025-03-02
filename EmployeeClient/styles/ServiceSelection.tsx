import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
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
        titleAndSubTitleContainer: {
            position: 'absolute',
            top: 0 * theme.dimensions.absoluteHeight,
            left: 0 * theme.dimensions.absoluteWidth,
            minHeight: 55 * theme.dimensions.absoluteHeight,
        },
        title: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._14,
            fontWeight: 900,
        },
        singleTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._17,
            fontWeight: 900,
        },
        description: {
            top: 2 * theme.dimensions.absoluteHeight,
            fontFamily: 'Nunito',
            fontSize: theme.fonts.size._12,
            fontWeight: 300,
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
            top: 40 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            height: theme.dimensions.height - theme.dimensions.headerHeight - 110 * theme.dimensions.absoluteHeight
        },
        listContentContainer: {
            paddingBottom: theme.dimensions.input.height
        }
    })
};

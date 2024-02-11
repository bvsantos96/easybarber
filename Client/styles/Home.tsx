import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        alignCenter: {
            alignItems: 'center',
            textAlign: 'center',
        },
        topCategoriesContainer: {
            width: "100%",
            alignItems: 'center',
        },
        topCategoriesList: {
            width: "100%",
            flexDirection: 'row',
            justifyContent: 'space-between',
            overflow: 'hidden'
        },
        topCategoriesHeights: {
            maxHeight: 120 * theme.dimensions.absoluteHeight,
        },
        nearByBarbersContainer: {
            width: "100%",
        },
        nearByBarbersContainerHeights: {
            minHeight: 328 * theme.dimensions.absoluteHeight,
            maxHeight: 455 * theme.dimensions.absoluteHeight,
        },
        homeListContainer: {
            width: "100%",
        },
    })
};

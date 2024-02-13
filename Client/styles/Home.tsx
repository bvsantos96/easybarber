import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        alignCenter: {
            alignItems: 'center',
            textAlign: 'center',
        },
        categoryContainer: {
            height: 128 * theme.dimensions.absoluteHeight,
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
            minHeight: theme.dimensions.height - (338 - theme.dimensions.statusBarHeight) * theme.dimensions.absoluteHeight,
            maxHeight: theme.dimensions.height - (225 - theme.dimensions.statusBarHeight) * theme.dimensions.absoluteHeight,
        },
        homeListContainer: {
            width: "100%",
        },
    })
};

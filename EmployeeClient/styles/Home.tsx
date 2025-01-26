import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';
import { getStyles as TopBarStyles } from './TopBar';

export const getStyles = () => {
    const theme = useTheme();
    const styles = TopBarStyles();
    const topBarHeight = styles.container.height;
    return StyleSheet.create({
        alignCenter: {
            alignItems: 'center',
            textAlign: 'center',
        },
        categoryContainer: {
            height: 82 * theme.dimensions.absoluteHeight,
        },
        topCategoriesContainer: {
            width: "100%",
            alignItems: 'center',
        },
        topCategoriesList: {
            width: "100%",
            flexDirection: 'row',
            justifyContent: 'space-between',
            overflow: 'hidden',
        },
        topCategoriesHeights: {
            maxHeight: 100 * theme.dimensions.absoluteHeight,
        },
        nearByBarbersContainer: {
            width: 360 * theme.dimensions.absoluteWidth,
            alignItems: 'center',
            flexGrow: 1,
        },
        nearByBarbersContainerHeights: {
            minHeight: theme.dimensions.height - topBarHeight - 200 * theme.dimensions.absoluteHeight - theme.dimensions.tabHeight,
            maxHeight: theme.dimensions.height - topBarHeight - 80 * theme.dimensions.absoluteHeight - theme.dimensions.tabHeight,
        },
        homeListContainer: {
            flexGrow: 1,
            width: "100%",
        },
        listBottom: {
            paddingBottom: 90 * theme.dimensions.absoluteHeight,
        },
        noSlotsContainer: {
            height: 140 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignSelf: 'center',
            alignItems: 'center',
        },
    })
};

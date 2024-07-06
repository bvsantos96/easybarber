import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const heightCalc = (px: number): number => {
        return px * theme.dimensions.absoluteHeight + theme.dimensions.statusBarHeight;
    }
    const topBarHeight = heightCalc(170);
    return StyleSheet.create({
        container: {
            position: 'absolute',
            top: 0,
            width: theme.dimensions.width,
            height: topBarHeight,
            backgroundColor: theme.colors.mainColor,
            borderBottomLeftRadius: 31,
            borderBottomRightRadius: 31,
        },
        elementsContainer: {
            position: 'absolute',
            top: heightCalc(5),
            left: 20 * theme.dimensions.absoluteWidth,
            bottom: 23 * theme.dimensions.absoluteHeight,
            width: 350 * theme.dimensions.absoluteWidth,
        },
        topElements: {
            position: 'absolute',
            top: heightCalc(0),
            width: "100%",
            height: 50 * theme.dimensions.absoluteHeight,
            justifyContent: "center",
        },
        bellContainer: {
            position: 'absolute',
            right: 66 * theme.dimensions.absoluteWidth,
        },
        bell: {
            width: 24 * theme.dimensions.absoluteWidth,
            height: 24 * theme.dimensions.absoluteHeight,
        },
        nameText: {
            color: theme.colors.text.alt,
            fontSize: theme.fonts.size._19,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 21,
        },
        textColor: {
            color: theme.colors.button.alt,
        },
        profileImageContainer: {
            position: 'absolute',
            right: 0,
            height: 50 * theme.dimensions.absoluteWidth,
            width: 50 * theme.dimensions.absoluteWidth,
            borderWidth: 1,
            borderRadius: 50 * theme.dimensions.absoluteWidth / 2,
            borderColor: theme.colors.button.alt,
            alignItems: 'center',
            justifyContent: 'center',
        },
        profileImage: {
            width: 40 * theme.dimensions.absoluteWidth,
            height: 40 * theme.dimensions.absoluteWidth,
        },
        noBackgroundColor: {
            backgroundColor: theme.colors.mainColor,
        },
        searchContainer: {
            position: 'absolute',
            bottom: 0 * theme.dimensions.absoluteHeight,
            width: "100%",
            height: 50 * theme.dimensions.absoluteHeight,
        },
        filterView: {
            position: 'absolute',
            right: 0,
            height: '100%',
            width: 50 * theme.dimensions.absoluteWidth,
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: theme.colors.button.alt,
            borderRadius: 8,
        },
        filter: {
            width: 31 * theme.dimensions.absoluteWidth,
            height: 31 * theme.dimensions.absoluteWidth,
        },
        searchBarContainer: {
            width: 290 * theme.dimensions.absoluteWidth,
        },
        searchBarContainerExpanded: {
            width: "100%",
        },
        searchBarInput: {
            height: 50 * theme.dimensions.absoluteHeight,
            borderWidth: 1,
            borderColor: theme.colors.button.alt,
            borderRadius: 12,
            borderStyle: 'solid',
        },
        textInput: {
            position: 'absolute',
            left: 43 * theme.dimensions.absoluteWidth,
            width: 296 * theme.dimensions.absoluteWidth - 44 * theme.dimensions.absoluteWidth,
            height: "100%",
            borderWidth: 0,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._15,
            fontFamily: 'Mazzard',
            fontWeight: '400',
        },
        iconView: {
            position: 'absolute',
            top: 12 * theme.dimensions.absoluteWidth,
            left: 12 * theme.dimensions.absoluteWidth,
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.mainColor,
        },
        icon: {
            position: 'absolute',
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,
        },
        homeContainer: {
            position: 'absolute',
            top: topBarHeight,
            left: 24 * theme.dimensions.absoluteWidth,
            width: 342 * theme.dimensions.absoluteWidth,
            height: theme.dimensions.height - topBarHeight - theme.dimensions.statusBarHeight,
            alignItems: 'center',
        },
        topCategoriesContainerExpanded: {
            position: 'absolute',
            top: 28 * theme.dimensions.absoluteHeight,
            height: 159 * theme.dimensions.absoluteHeight,
        },
        topCategoriesContainer: {
            position: 'absolute',
            top: 28 * theme.dimensions.absoluteHeight,
            height: 30 * theme.dimensions.absoluteHeight,
        },
        categoriesContainer: {
            display: 'none'
        },
        categoriesContainerExpanded: {
            position: 'absolute',
            top: 46 * theme.dimensions.absoluteHeight,
            height: 31 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width - 48 * theme.dimensions.absoluteWidth,
            left: 24 * theme.dimensions.absoluteWidth,
            flexDirection: 'row',
            justifyContent: 'space-between',
        },
        category: {
            width: 60 * theme.dimensions.absoluteWidth,
        },
        categoryIconContainer: {
            width: 60 * theme.dimensions.absoluteWidth,
            height: 60 * theme.dimensions.absoluteWidth,
            borderRadius: 30 * theme.dimensions.absoluteWidth,
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: theme.colors.text.black,
        },
        categorySelected: {
            backgroundColor: theme.colors.button.border,
        },
        categoryIcon: {
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,

        },
        categoryText: {
            color: theme.colors.text.black,
            fontSize: theme.fonts.size._13,
            fontFamily: 'Mazzard',
            fontWeight: '500',
            textAlign: 'center',
        },
        viewAllContainer: {
            position: 'absolute',
            right: 33 * theme.dimensions.absoluteWidth,
        },
        homeTitleContainer: {
            position: 'absolute',
            left: 24 * theme.dimensions.absoluteWidth,
        },
        homeListContainerExpanded: {
            position: 'absolute',
            top: 19 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            height: theme.dimensions.height - topBarHeight - 105 * theme.dimensions.absoluteHeight,
            marginTop: 5 * theme.dimensions.absoluteHeight,
        },
        homeListContainer: {
            position: 'absolute',
            top: 19 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            height: theme.dimensions.height - topBarHeight - 213 * theme.dimensions.absoluteHeight,
            marginTop: 5 * theme.dimensions.absoluteHeight,
        },
        flexDirection: {
            flexDirection: 'row',
        },
        optionsList: {
            position: 'absolute',
            elevation: 100,
            zIndex: 100,
            backgroundColor: theme.colors.backgroundColor,
        },
    })
};

import { StyleSheet } from 'react-native';
import { width, buttonTextColor, lightColor, inputHeight, inputTextColor, absoluteHeight, statusBarHeight, height, textBlackColor } from './Main';
import { useTheme } from './ThemeContext';

const topMargin = statusBarHeight == 0 ? 44 : statusBarHeight;

const heightCalc = (px: number): number => {
    return px * absoluteHeight + topMargin;
}

export const profileIconSize = 35;
export const topBarHeight = () => {
    return heightCalc(150);
};

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            position: 'absolute',
            top: 0,
            width: theme.dimensions.width,
            height: heightCalc(150),
            backgroundColor: theme.colors.mainColor,
            borderBottomLeftRadius: 31,
            borderBottomRightRadius: 31,
        },
        hamburguer: {
            position: 'absolute',
            top: heightCalc(10),
            left: 20 * theme.dimensions.absoluteWidth,
            width: 27,
            height: 27,
        },
        bellContainer: {
            position: 'absolute',
            top: heightCalc(16),
            right: 88 * theme.dimensions.absoluteWidth,
        },
        bell: {
            width: 24 * theme.dimensions.absoluteWidth,
            height: 24 * theme.dimensions.absoluteHeight,
        },
        nameText: {
            position: 'absolute',
            left: 62 * theme.dimensions.absoluteWidth,
            top: heightCalc(16),
            color: 'white',
            fontSize: 19,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 21,
        },
        textColor: {
            color: buttonTextColor,
        },
        profileImageContainer: {
            position: 'absolute',
            right: 17 * theme.dimensions.absoluteWidth,
            top: heightCalc(6),
            padding: 5,
            borderWidth: 1,
            borderRadius: 50 * theme.dimensions.absoluteWidth / 2,
            borderColor: lightColor,
        },
        profileImage: {
            width: 50 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteWidth,
        },
        noBackgroundColor: {
            backgroundColor: theme.colors.mainColor,
        },
        filterView: {
            position: 'absolute',
            top: heightCalc(77),
            right: 17 * theme.dimensions.absoluteWidth,
            width: 48 * theme.dimensions.absoluteWidth,
            height: 49 * theme.dimensions.absoluteWidth,
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: buttonTextColor,
            borderRadius: 8,
        },
        filter: {
            width: 31,
            height: 32,
        },
        searchBarInput: {
            position: 'absolute',
            bottom: 23 * theme.dimensions.absoluteHeight,
            left: 21 * theme.dimensions.absoluteWidth,
            width: 296 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            borderWidth: 1,
            borderColor: buttonTextColor,
            borderRadius: 12,
            borderStyle: 'solid',
        },
        textInput: {
            position: 'absolute',
            left: 43 * theme.dimensions.absoluteWidth,
            width: 296 * theme.dimensions.absoluteWidth - 44 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            borderWidth: 0,
            color: inputTextColor,
            fontSize: 15,
            fontFamily: 'Mazzard',
            fontWeight: '400',
        },
        iconView: {
            position: 'absolute',
            top: 12 * theme.dimensions.absoluteWidth,
            left: 12 * theme.dimensions.absoluteWidth,
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteWidth,
            borderRadius: inputHeight * 0.4,
            backgroundColor: theme.colors.mainColor,
        },
        icon: {
            position: 'absolute',
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,
        },
        homeContainer: {
            position: 'absolute',
            top: topBarHeight(),
            width: width,
            height: height - topBarHeight(),
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
            width: width - 48 * theme.dimensions.absoluteWidth,
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
            backgroundColor: textBlackColor,
        },
        categoryIcon: {
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,

        },
        categoryText: {
            color: textBlackColor,
            fontSize: 13,
            fontFamily: 'Mazzard',
            fontWeight: '500',
            textAlign: 'center',
            marginTop: 10 * theme.dimensions.absoluteHeight,
        },
        viewAllContainer: {
            position: 'absolute',
            right: 33 * theme.dimensions.absoluteWidth,
        },
        homeTitleContainer: {
            position: 'absolute',
            left: 24 * theme.dimensions.absoluteWidth,
        },
        nearByContainer: {
            width: width,
        },
        nearByBarbersContainerExpanded: {
            position: 'absolute',
            top: 70 * theme.dimensions.absoluteHeight,
            height: 505 * theme.dimensions.absoluteHeight,
        },
        homeListContainerExpanded: {
            position: 'absolute',
            top: 19 * theme.dimensions.absoluteHeight,
            width: width,
            height: height - topBarHeight() - 105 * theme.dimensions.absoluteHeight,
            marginTop: 5 * theme.dimensions.absoluteHeight,
        },
        nearByBarbersContainer: {
            position: 'absolute',
            top: 180 * theme.dimensions.absoluteHeight,
            height: 403 * theme.dimensions.absoluteHeight,
        },
        homeListContainer: {
            position: 'absolute',
            top: 19 * theme.dimensions.absoluteHeight,
            width: width,
            height: height - topBarHeight() - 213 * theme.dimensions.absoluteHeight,
            marginTop: 5 * theme.dimensions.absoluteHeight,
        },
    })
};

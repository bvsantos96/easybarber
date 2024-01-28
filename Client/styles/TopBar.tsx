import { StyleSheet } from 'react-native';
import { width, mainColor, buttonTextColor, lightColor, inputHeight, inputTextColor, absoluteWidth, absoluteHeight, statusBarHeight, height, textBlackColor } from './Main';

const topMargin = statusBarHeight == 0 ? 44 : statusBarHeight;

const heightCalc = (px: number): number => {
    return px * absoluteHeight + topMargin;
}

export const profileIconSize = 35;
export const topBarHeight = () => {
    return heightCalc(150);
};

export const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        top: 0,
        width: width,
        height: heightCalc(150),
        backgroundColor: mainColor,
        borderBottomLeftRadius: 31,
        borderBottomRightRadius: 31,
    },
    hamburguer: {
        position: 'absolute',
        top: heightCalc(16),
        left: 20 * absoluteWidth,
        width: 27,
        height: 27,
    },
    bellContainer: {
        position: 'absolute',
        top: heightCalc(19),
        right: 88 * absoluteWidth,
    },
    bell: {
        width: 24 * absoluteWidth,
        height: 24 * absoluteHeight,
    },
    nameText: {
        position: 'absolute',
        left: 62 * absoluteWidth,
        top: heightCalc(19),
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
        right: 17 * absoluteWidth,
        top: heightCalc(0.69),
        padding: 5,
        borderWidth: 1,
        borderRadius: 50 * absoluteWidth / 2,
        borderColor: lightColor,
    },
    profileImage: {
        width: 50 * absoluteWidth,
        height: 50 * absoluteWidth,
    },
    noBackgroundColor: {
        backgroundColor: mainColor,
    },
    filterView: {
        position: 'absolute',
        top: heightCalc(77),
        right: 17 * absoluteWidth,
        width: 48 * absoluteWidth,
        height: 49 * absoluteWidth,
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
        bottom: 23 * absoluteHeight,
        left: 21 * absoluteWidth,
        width: 296 * absoluteWidth,
        height: 50 * absoluteHeight,
        borderWidth: 1,
        borderColor: buttonTextColor,
        borderRadius: 12,
        borderStyle: 'solid',
    },
    textInput: {
        position: 'absolute',
        left: 43 * absoluteWidth,
        width: 296 * absoluteWidth - 44 * absoluteWidth,
        height: 50 * absoluteHeight,
        borderWidth: 0,
        color: inputTextColor,
        fontSize: 15,
        fontFamily: 'Mazzard',
        fontWeight: '400',
    },
    iconView: {
        position: 'absolute',
        top: 12 * absoluteWidth,
        left: 12 * absoluteWidth,
        width: 25 * absoluteWidth,
        height: 25 * absoluteWidth,
        borderRadius: inputHeight * 0.4,
        backgroundColor: mainColor,
    },
    icon: {
        position: 'absolute',
        width: 25 * absoluteWidth,
        height: 25 * absoluteHeight,
    },
    homeContainer: {
        position: 'absolute',
        top: topBarHeight(),
        width: width,
        height: height - topBarHeight(),
    },
    topCategoriesContainerExpanded: {
        position: 'absolute',
        top: 28 * absoluteHeight,
        height: 159 * absoluteHeight,
    },
    topCategoriesContainer: {
        position: 'absolute',
        top: 28 * absoluteHeight,
        height: 30 * absoluteHeight,
    },
    categoriesContainer: {
        display: 'none'
    },
    categoriesContainerExpanded: {
        position: 'absolute',
        top: 46 * absoluteHeight,
        height: 31 * absoluteHeight,
        width: width - 48 * absoluteWidth,
        left: 24 * absoluteWidth,
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    category: {
        width: 60 * absoluteWidth,
    },
    categoryIconContainer: {
        width: 60 * absoluteWidth,
        height: 60 * absoluteWidth,
        borderRadius: 30 * absoluteWidth,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: textBlackColor,
    },
    categoryIcon: {
        width: 25 * absoluteWidth,
        height: 25 * absoluteHeight,

    },
    categoryText: {
        color: textBlackColor,
        fontSize: 13,
        fontFamily: 'Mazzard',
        fontWeight: '500',
        textAlign: 'center',
    },
    viewAllContainer: {
        position: 'absolute',
        right: 33 * absoluteWidth,
    },
    homeTitleContainer: {
        position: 'absolute',
        left: 24 * absoluteWidth,
    },
    nearByContainer: {
        width: width,
    },
    nearByBarbersContainerExpanded: {
        position: 'absolute',
        top: 70 * absoluteHeight,
        height: 505 * absoluteHeight,
    },
    homeListContainerExpanded: {
        position: 'absolute',
        top: 19 * absoluteHeight,
        width: width,
        height: height - topBarHeight() - 105 * absoluteHeight,
        paddingBottom: 30 * absoluteHeight,
    },
    nearByBarbersContainer: {
        position: 'absolute',
        top: 180 * absoluteHeight,
        height: 403 * absoluteHeight,
    },
    homeListContainer: {
        position: 'absolute',
        top: 19 * absoluteHeight,
        width: width,
        height: height - topBarHeight() - 213 * absoluteHeight,
        paddingBottom: 30 * absoluteHeight,
    },
});

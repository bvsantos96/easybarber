import { StyleSheet } from 'react-native';
import { width, mainColor, buttonTextColor, lightColor, inputHeight, inputTextColor, absoluteWidth, absoluteHeight, statusBarHeight } from './Main';

export const profileIconSize = 35;
const topMargin = statusBarHeight == 0 ? 44 : statusBarHeight;
const height = (px : number): number => {
    return  px * absoluteHeight + topMargin;
}

export const styles = StyleSheet.create({
    container: {
        zIndex: 1,
        position: 'absolute',
        top: 0,
        width: width,
        height: height(150),
        paddingTop: topMargin,
        backgroundColor: mainColor,
        borderBottomLeftRadius: 31,
        borderBottomRightRadius: 31,
    },
    hamburguer: {
        position: 'absolute',
        top: height(16),
        left: 20 * absoluteWidth,
        width: 27,
        height: 27,
    },
    bellContainer: {
        position: 'absolute',
        top: height(19),
        right: 88 * absoluteWidth,
    },
    bell: {
        width: 24 *  absoluteWidth,
        height: 24  * absoluteHeight,
    },
    nameText: {
        position: 'absolute',
        left: 62 *  absoluteWidth,
        top: height(19),
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
        top: height(0.69),
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
        top: height(77),
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
        bottom: 23 *  absoluteHeight,
        left: 21 * absoluteWidth,
        width: 296 *  absoluteWidth,
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
        width: 25 *  absoluteWidth,
        height: 25  * absoluteHeight,
    },

});

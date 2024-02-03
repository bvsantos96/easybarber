import Constants from 'expo-constants';
import { StyleSheet, Dimensions, DimensionValue } from 'react-native';

const window = Dimensions.get("window");
export const statusBarHeight = Constants.statusBarHeight;
export const inputHeight = window.height * 0.07;
export const inputWidth = window.width * 0.85;
export const mainColor = "#DF2238";
export const textColorLight = "#666666";
export const secondColor = "rgba(109, 4, 4, 0.10)";
export const textBlackColor = "#263238";
export const inputTextColor = "black";
export const backgroundColor = "white";
export const width = window.width;
export const height = window.height;
export const absoluteHeight = height / 844;
export const absoluteWidth = width / 390;
export const minDimention = Math.min(width, height);
export const lightTextColor = 'rgba(0, 0, 0, 0.66)';
export const statusBarOnHome = 'light';
export const buttonTextColor = 'white';
export const lightColor = '#FFFFFF69'

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: backgroundColor,
    },
    containerSpaceBetween: {
        flex: 1,
        justifyContent: 'space-between',
        alignItems: 'center',
        backgroundColor: backgroundColor,
    },
    containerCenter: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: backgroundColor,
        height: height as DimensionValue
    },
    containerMax: {
        flex: 1,
        width: width as DimensionValue,
        height: height as DimensionValue,
        backgroundColor: backgroundColor
    },
    spaceBetween: {
        justifyContent: 'space-between',
    },
    backgroundMainColor: {
        backgroundColor: mainColor,
    },
    hMargin5: {
        marginHorizontal: 5
    },
    hMargin3: {
        marginHorizontal: 3
    },
    w50c: {
        minWidth: '50%',
        alignItems: 'center',
    },
    w100: {
        minWidth: '100%',
    },
    w100Center: {
        minWidth: '100%',
        alignItems: 'center',
    },
    fixBottom: {
        position: 'absolute',
        bottom: 0
    },
    paddingBottom10: {
        paddingBottom: '50%'
    },
    noOverflow: {
        overflow: 'hidden'
    },
    loginContainer: {
        width: width,
        height: height * 0.7,
        justifyContent: 'space-between',
        alignItems: 'center',
        backgroundColor: backgroundColor,
        borderTopLeftRadius: 31,
        borderTopRightRadius: 31,
    },
    paddingRight10: {
        paddingRight: '10%',
    },
    alignLeft: {
        alignItems: 'flex-start',
        textAlign: 'left',
    },
    alignRight: {
        alignItems: 'flex-end',
        textAlign: 'right',
    },
    alignCenter: {
        alignItems: 'center',
        textAlign: 'center',
    },
    justifyCenter: {
        justifyContent: 'center',
    },
    row: {
        flexDirection: 'row',
    },
    hMargin2: {
        marginHorizontal: "2%"
    },
    hPadding2: {
        paddingHorizontal: "2%"
    },
    redBold: {
        color: mainColor,
        fontWeight: '900',
    },
    padding15px: {
        padding: 15,
    },
    normalText: {
        fontSize: 16,
        fontFamily: 'Mazzard',
        fontWeight: '500',
    },
    fontSize19: {
        fontSize: 19,
    },
    fontSize18: {
        fontSize: 18,
    },
    fontSize17: {
        fontSize: 17,
    },
    lightTextColor: {
        color: lightTextColor,
    },
    rMargin10: {
        marginRight: 10,
    },
    rMargin7: {
        marginRight: 7,
    },
    rMargin5: {
        marginRight: 5,
    },
    lMargin10: {
        marginLeft: 10,
    },
    lMargin7: {
        marginLeft: 7,
    },
    lMargin5: {
        marginLeft: 5,
    },
    zIndex10: {
        zIndex: 10,
    },
    fontPoppins: {
        fontFamily: 'Poppins',
    },
    fontWeight400: {
        fontWeight: '400',
    },
    fontWeight600: {
        fontWeight: '600',
    },
    fontWeight700: {
        fontWeight: '700',
    },
    fonstSize18: {
        fontSize: 18,
    },
    colorDarkTitle: {
        color: textBlackColor,
    },
    overlay: {
        position: 'absolute',
        width: width,
        height: height,
        backgroundColor: 'rgba(0,0,0,0.5)',
        zIndex: 10,
    },
    shadow: {
        elevation: 8,
        shadowColor: '#000',
        shadowOffset: {
            width: 0,
            height: 2,
        },
        shadowOpacity: 0.2,
        shadowRadius: 4,
    }
});

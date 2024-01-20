import { StyleSheet, Dimensions } from 'react-native';

const window = Dimensions.get("window");
export const inputHeight = window.height * 0.07;
export const inputWidth = '85%';
export const mainColor = "#DF2238";
export const secondColor = "rgba(109, 4, 4, 0.10)";
export const inputTextColor = "black";
export const backgroundColor = "white";
export const width = window.width;
export const height = window.height;
export const minDimention = Math.min(width, height);

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'space-between',
        alignItems: 'center',
        backgroundColor: backgroundColor,
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
    w100: {
        minWidth: '100%',
    },
    w100c: {
        minWidth: '100%',
        alignItems: 'center',
    },
    noOverflow: {
        overflow: 'none'
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
    alignRight: {
        alignItems: 'right',
        textAlign: 'right',
    },
    alignCenter: {
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
    },
});

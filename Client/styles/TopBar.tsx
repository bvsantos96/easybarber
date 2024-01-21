import { StyleSheet } from 'react-native';
import { height, width, mainColor, buttonTextColor, lightColor, inputHeight, inputWidth } from './Main';

export const profileIconSize = 35;

export const styles = StyleSheet.create({
    container: {
        justifyContent: 'space-between',
        position: 'absolute',
        top: 0,
        width: width,
        height: height * 0.25,
        backgroundColor: mainColor,
        borderBottomLeftRadius: 31,
        borderBottomRightRadius: 31,
        padding: 25
    },
    iconView: {
        width: inputHeight * 0.8,
        height: inputHeight * 0.8,
        borderRadius: inputHeight * 0.4,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: mainColor,
    },
    icon: {
        width: 27,
        height: 27,
    },
    textColor: {
        color: buttonTextColor,
    },
    profileImageContainer: {
        padding: 5,
        borderWidth: 1,
        borderRadius: 50,
        borderColor: lightColor,
    },
    profileImage: {
        width: 35,
        height: 35,
    },
    noBackgroundColor: {
        backgroundColor: mainColor,
    },
    searchBarInput: {
        width: '100%',
        height: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: buttonTextColor,
        borderRadius: 12,
        borderStyle: 'solid',
    },
    filterView: {
        alignItems: 'center',
        justifyContent: 'center',
        width: inputHeight * 0.8,
        height: inputHeight * 0.8,
        backgroundColor: buttonTextColor,
        borderRadius: 8,
    },
    filter: {
        width: 31,
        height: 31,
    },
    inputWidth: {
        width: inputWidth - inputHeight * 0.8,
    },
});

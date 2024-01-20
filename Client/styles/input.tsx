import { StyleSheet } from 'react-native';
import { inputHeight, inputWidth, secondColor, inputTextColor } from './Main';

export const iconSize = 23;

const _80IH = 0.8 * inputHeight;
const _40IH = 0.5 * _80IH;

export const styles = StyleSheet.create({
    container: {
        width: inputWidth,
        height: inputHeight,
    },
    inputView: {
        width: '100%',
        height: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: 'rgba(0, 0, 0, 0.29)',
        borderRadius: 50,
        borderStyle: 'solid',
    },
    iconView: {
        width: _80IH,
        height: _80IH,
        borderRadius: _40IH,
        backgroundColor: secondColor,
        alignItems: 'center',
        justifyContent: 'center',
        margin: 5,
        marginRight: 10,
    },
    textInput: {
        width: '100%',
        height: 40,
        borderWidth: 0,
        color: inputTextColor,
        fontSize: 15,
        fontFamily: 'Mazzard',
        fontWeight: '400',
    },
    icon: {
        width: iconSize,
        height: iconSize,
        resizeMode: 'contain',
    }
});

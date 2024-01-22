import { StyleSheet } from 'react-native';
import {inputHeight, inputWidth, secondColor, inputTextColor} from './Main';

export const iconSize = 23;

const _80IH = 0.8 * parseFloat(inputHeight);
const _40IH = 0.5 * _80IH;

export const styles = StyleSheet.create({
    container : {
        width: inputWidth,
        height: inputHeight,
    },
    inputView : {
        width: '100%',
        height: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        border: '1px rgba(0, 0, 0, 0.29) solid',
        borderRadius: 50,
    },
    iconView : {
        width: _80IH,
        height: _80IH, 
        borderRadius: _40IH, 
        backgroundColor: secondColor, 
        alignItems: 'center',
        justifyContent: 'center',
        margin: 5,
    },
    textInput : {
        width: '100%',
        height: 40,
        borderWidth: 0, 
        color: inputTextColor, 
        fontSize: 15, 
        fontFamily: 'Mazzard', 
        fontWeight: '400', 
        wordWrap: 'break-word',
    },
    icon: {
        width: iconSize,
        height: iconSize,
        resizeMode: 'contain',
    }
});

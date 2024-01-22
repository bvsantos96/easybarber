import { StyleSheet } from 'react-native';
import {inputHeight, inputWidth} from './Main';

export const whitheButtonColor = "#FFF";
export const thinBorderBlack = "#263238";

export const styles = StyleSheet.create({
    button: {
        width: inputWidth,
        height: inputHeight,
        borderRadius: 50,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    textButton: {
        textAlign: 'center',
        fontSize: 18,
        fontFamily: 'Mazzard',
        fontWeight: '600',
        wordWrap: 'break-word',
    },
});

import { StyleSheet } from 'react-native';
import {inputHeight, inputWidth, mainColor} from './main';

export const styles = StyleSheet.create({
    button: {
        width: inputWidth,
        height: inputHeight,
        backgroundColor: mainColor,
        borderRadius: 50,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    textButton: {
        textAlign: 'center',
        color: 'white',
        fontSize: 18,
        fontFamily: 'Mazzard',
        fontWeight: '600',
        wordWrap: 'break-word',
    },
});

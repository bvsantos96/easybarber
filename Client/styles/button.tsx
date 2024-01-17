import { StyleSheet } from 'react-native';
import {inputHeight, inputWidth} from './main';

export const styles = StyleSheet.create({
    button: {
        width: inputWidth,
        height: inputHeight,
        backgroundColor: '#DF2238',
        boxShadow: '20px 20px 50px rgba(0, 0, 0, 0.12)',
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

import { StyleSheet } from 'react-native';
import {inputHeight, inputWidth} from './main';

export const iconSize = 23;

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
        boxShadow: '20px 20px 50px rgba(0, 0, 0, 0.12)',
        border: '1px rgba(0, 0, 0, 0.29) solid',
        borderRadius: 50,
    },
    iconView : {
        width: 44,
        height: 44,
        borderRadius: 22, // Half of the width/height to create a circular shape
        backgroundColor: 'rgba(109, 4, 4, 0.10)', 
        alignItems: 'center',
        justifyContent: 'center',
        boxShadow: '20px 20px 50px rgba(0, 0, 0, 0.12)',
        margin: 5,
    },
    textInput : {
        width: '100%',
        height: 40,
        borderWidth: 0, 
        color: 'black', 
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

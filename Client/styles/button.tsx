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
    },
    smallButton: {
        minHeight: '100%',
        borderRadius: 9,
        borderWidth: 1,
        borderStyle: 'solid',
        borderColor: 'rgba(0, 0, 0, 0.08)',
        paddingTop:11,
        paddingBottom:11,
        paddingLeft: 20,
        paddingRight: 35,
    },
    smallButtonIconImage: {
        width: 25,
        height: 25,
        resizeMode: 'cover',
        marginRight: 10,
    },
    smallTextButton: {
        color: 'black',
        fontSize: 9,
        fontFamily: 'Poppins',
        fontWeight: '400', 
    },
    brandTextButton: {
        color: 'black',
        fontSize: 13,
        fontFamily: 'Poppins',
        fontWeight: '400', 
    },
});

import { StyleSheet } from 'react-native';
import { inputHeight, inputWidth, mainColor } from './main';

export const styles = StyleSheet.create({
    titleLine: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    text: {
        color: 'black', 
        fontSize: 25, 
        fontFamily: 'Poppins', 
        fontWeight: '600', 
        lineHeight: 33, 
        letterSpacing: 0.25, 
    },
    hText: {
        color: 'white', 
        fontSize: 25, 
        fontFamily: 'Poppins', 
        fontWeight: '600', 
        lineHeight: 33, 
        letterSpacing: 0.25, 
        backgroundColor: mainColor,
        borderRadius: 50, // Add rounded border to the highlighted text
        marginLeft: -5,
        paddingHorizontal: 10,
    },
    subtitle: {
       color: 'rgba(0, 0, 0, 0.40)', 
        fontSize: 16, 
        fontFamily: 'Poppins', 
        fontWeight: '400', 
        lineHeight: 23, 
    },
});

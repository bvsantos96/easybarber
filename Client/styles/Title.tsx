import { Platform, StyleSheet } from 'react-native';
import { mainColor } from './Main';

export const styles = StyleSheet.create({
    titleLine: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    text: {
        color: 'black',
        fontSize: 25,
        fontFamily: 'Mazzard',
        fontWeight: '600',
        lineHeight: 33,
        letterSpacing: 0.25,
    },
    hText: {
        color: 'white',
        fontSize: 25,
        fontFamily: 'Mazzard',
        fontWeight: '600',
        lineHeight: 33,
        letterSpacing: 0.25,
        backgroundColor: mainColor,
        borderRadius: 15, // Add rounded border to the highlighted text
        overflow: Platform.OS === "ios" ? "hidden" : "visible",
        marginLeft: 0,
        marginRight: 5,
        paddingHorizontal: 10,
    },
    subtitle: {
        color: 'rgba(0, 0, 0, 0.40)',
        fontSize: 16,
        fontFamily: 'Mazzard',
        fontWeight: '400',
        lineHeight: 23,
    },
});

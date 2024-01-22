import { StyleSheet } from 'react-native';
import { width, height, backgroundColor, inputTextColor, lightTextColor, mainColor } from './Main';

export const styles = StyleSheet.create({
    container: {
        marginVertical: 10,
        width: width * 0.8,
        height: height * 0.16,
        backgroundColor: backgroundColor,
        boxShadow: '2px 2px 19px rgba(0, 0, 0, 0.16)',
        borderRadius: 11,
        flexDirection: 'row',
    },
    imageStyle: {
        width: '40%',
        height: '100%',
        borderRadius: 11,
    },
    textContainer: {
        width: '60%',
        height: '100%',
        flexDirection: 'column',
        justifyContent: 'space-around',
    },
    title: {
        color: inputTextColor,
        fontSize: 15,
        fontFamily: 'Mazzard',
        fontWeight: '600',
    },
    locationContainer: {
        flexDirection: 'row',
        alignItems: 'flex-start',
    },
    locationText: {
        color: '#666666',
        fontSize: 11,
        fontFamily: 'Mazzard',
        fontWeight: '400',
    },
    locationIcon: {
        width: 13,
        height: 14,
        marginRight: 5,
    },
    description: {
        color: lightTextColor,
        fontSize: 10,
        fontFamily: 'Mazzard',
        fontWeight: '400',
        lineHeight: 18,
    },
    ratingContainer: {
        flexDirection: 'row',
        position: 'absolute',
        top: 10,
        left: 10,
        borderRadius: 50,
        backgroundColor: mainColor,
        padding: 2,
    },
    ratingIcon: {
        width: 11,
        height: 11,
        borderRadius: 22,
        backgroundColor: backgroundColor,
        padding: 2,
        margin: 1,
    },
    ratingText: {
        fontSize: 13.05, 
        fontFamily: 'Mazzard', 
        fontWeight: '500',
    },
    numberOfVotes: {
        fontSize: 8.12, 
        fontFamily: 'Mazzard', 
        fontWeight: '500',
    },
});

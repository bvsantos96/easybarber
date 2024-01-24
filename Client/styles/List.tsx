import { StyleSheet } from 'react-native';
import { width, height, backgroundColor, inputTextColor, lightTextColor, mainColor, secondColor } from './Main';

export const styles = StyleSheet.create({
    container: {
        marginVertical: 10,
        width: width * 0.85,
        height: height * 0.16,
        backgroundColor: backgroundColor,
        shadowColor: '#000',
        shadowOffset: { width: 2, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 5,
        elevation: 5, // For Android
        borderRadius: 11,
        flexDirection: 'row',
    },
    imageStyle: {
        width: width * 0.4,
        height : height * 0.16,
        borderRadius: 11,
    },
    textContainer: {
        width: width * 0.425,
        height: height * 0.16,
        flexDirection: 'column',
        justifyContent: 'space-around',
    },
    title: {
        color: inputTextColor,
        fontSize: 18,
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
        color: backgroundColor,
        fontSize: 13.05, 
        fontFamily: 'Mazzard', 
        fontWeight: '500',
    },
    numberOfVotes: {
        color: backgroundColor,
        fontSize: 8.12, 
        fontFamily: 'Mazzard', 
        fontWeight: '500',
    },
});

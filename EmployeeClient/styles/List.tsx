import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const itemHeight = 128 * theme.dimensions.absoluteHeight;
    return StyleSheet.create({
        shadow: {
            ...theme.shadow as any,
        },
        itemContainer: {
            width: '100%',
            height: itemHeight,
            alignItems: 'center',
        },
        container: {
            width: '99%',
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 11,
            flexDirection: 'row',
        },
        imageContainer: {
            width: 168 * theme.dimensions.absoluteWidth,
            height: itemHeight,
            maxHeight: itemHeight,
        },
        imageStyle: {
            position: 'absolute',
            width: '100%',
            height: '100%',
            left: 0,
            borderRadius: 11,
        },
        textContainer: {
            position: 'absolute',
            left: 183 * theme.dimensions.absoluteWidth,
            width: 143 * theme.dimensions.absoluteWidth,
        },
        title: {
            position: 'relative',
            top: 14 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.main,
            fontSize: 18,
            fontFamily: 'Poppins',
            fontWeight: '600',
        },
        locationContainer: {
            position: 'relative',
            top: 20 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            alignItems: 'flex-start',
        },
        locationText: {
            color: theme.colors.text.lightBlack,
            fontSize: 11,
            fontFamily: 'Nunito',
            fontWeight: '400',
        },
        locationIcon: {
            width: 13 * theme.dimensions.absoluteMinDimension,
            height: 13 * theme.dimensions.absoluteMinDimension,
            marginRight: 5,
        },
        appointmentTimeIcon: {
            width: 13 * theme.dimensions.absoluteMinDimension,
            height: 14 * theme.dimensions.absoluteMinDimension,
            marginRight: 5,
        },
        description: {
            position: 'relative',
            top: 20 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.lightBlack,
            fontSize: 10,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 18,
            height: 50 * theme.dimensions.absoluteHeight,
        },
        ratingContainerNoText: {
            width: 25 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignItems: 'center',
        },
        ratingContainerRight: {
            position: 'absolute',
            borderRadius: 50,
            top: 5 * theme.dimensions.absoluteHeight,
            right: 5 * theme.dimensions.absoluteWidth,
            width: 67 * theme.dimensions.absoluteWidth,
            height: 23 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.mainColor,
            zIndex: 10,
            justifyContent: 'center',
            alignItems: 'center',
        },

        ratingContainer: {
            position: 'absolute',
            borderRadius: 50,
            top: 5 * theme.dimensions.absoluteHeight,
            left: 5 * theme.dimensions.absoluteWidth,
            width: 67 * theme.dimensions.absoluteWidth,
            height: 23 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.mainColor,
            zIndex: 10,
            justifyContent: 'center',
            alignItems: 'center',
        },
        ratingIconContainer: {
            position: 'absolute',
            left: 2 * theme.dimensions.absoluteWidth,
            width: 20 * theme.dimensions.absoluteWidth,
            height: 20 * theme.dimensions.absoluteHeight,
            borderRadius: 22,
            backgroundColor: theme.colors.backgroundColor,
            zIndex: 20,
        },
        ratingIcon: {
            width: 11 * theme.dimensions.absoluteWidth,
            height: 11 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.backgroundColor,
        },
        ratingText: {
            position: 'absolute',
            left: 25 * theme.dimensions.absoluteWidth,
            color: theme.colors.backgroundColor,
            fontSize: 13.05,
            fontFamily: 'Mazzard',
            fontWeight: '500',
            justifyContent: 'center',
            alignItems: 'center',
        },
        numberOfVotes: {
            position: 'absolute',
            left: 46 * theme.dimensions.absoluteWidth,
            color: theme.colors.backgroundColor,
            fontSize: 8.12,
            fontFamily: 'Mazzard',
            fontWeight: '500',
        },
        center: {
            alignItems: 'center',
            justifyContent: 'center',
        },
    })
};

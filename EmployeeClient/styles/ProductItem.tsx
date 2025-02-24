import { StyleSheet } from 'react-native';
import { useTheme } from '@styles/ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: 160 * theme.dimensions.absoluteWidth,
            height: 215 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
            backgroundColor: 'white',
            marginVertical: 10 * theme.dimensions.absoluteHeight,
            alignSelf: 'center',
            alignItems: 'center'
        },
        image: {
            top: 35 * theme.dimensions.absoluteHeight,
            width: 129 * theme.dimensions.absoluteHeight,
            height: 65 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
            resizeMode: 'contain' //TODO: resizeMode is deprecated
        },
        textContainer: {
            position: 'absolute',
            top: 115 * theme.dimensions.absoluteHeight,
            left: 15 * theme.dimensions.absoluteWidth,
            width: 130 * theme.dimensions.absoluteWidth,
            height: 90 * theme.dimensions.absoluteHeight
        },
        brandText: {
            position: 'absolute',
            width: 130 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        nameText: {
            position: 'absolute',
            width: 130 * theme.dimensions.absoluteWidth,
            height: 32 * theme.dimensions.absoluteHeight,
            top: 30 * theme.dimensions.absoluteHeight,
            fontFamily: 'Poppins',
            fontWeight: '400',
            fontSize: 10 * theme.dimensions.absoluteHeight,
            lineHeight: 16 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        priceText: {
            position: 'absolute',
            width: 130 * theme.dimensions.absoluteWidth,
            height: 21 * theme.dimensions.absoluteHeight,
            top: 70 * theme.dimensions.absoluteHeight,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 14 * theme.dimensions.absoluteHeight,
            lineHeight: 21 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        editIcon: {
            position: 'absolute',
            top: 4 * theme.dimensions.absoluteHeight,
            left: 125 * theme.dimensions.absoluteWidth,
            width: 30 * theme.dimensions.absoluteWidth,
            height: 30 * theme.dimensions.absoluteWidth,
        }
    })
}

import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0
        },
        image: {
            top: 12.58 * theme.dimensions.absoluteHeight,
            alignSelf: 'center',
            width: 210 * theme.dimensions.absoluteHeight,
            height: 156 * theme.dimensions.absoluteHeight,
            borderRadius: 15,
            resizeMode: 'contain' //TODO: resizeMode is deprecated
        },
        brandText: {
            position: 'absolute',
            width: 55 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 162 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        brandInput:{
            position: 'absolute',
            top: 200 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 52 * theme.dimensions.absoluteHeight,
            width: 327 * theme.dimensions.absoluteWidth
        },
        nameText: {
            position: 'absolute',
            width: 55 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 255 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        nameInput:{
            position: 'absolute',
            top: 293 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 52 * theme.dimensions.absoluteHeight,
            width: 327 * theme.dimensions.absoluteWidth
        },
        descriptionText: {
            position: 'absolute',
            width: 92 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 353 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        descriptionInput:{
            position: 'absolute',
            top: 390 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 100 * theme.dimensions.absoluteHeight,
            width: 327 * theme.dimensions.absoluteWidth
        },
        priceText: {
            position: 'absolute',
            width: 49 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 492 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        priceInput:{
            position: 'absolute',
            top: 523 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            width: 120 * theme.dimensions.absoluteWidth
        },
        quantityText: {
            position: 'absolute',
            width: 120 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 492 * theme.dimensions.absoluteHeight,
            left: 222 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        quantityInput:{
            position: 'absolute',
            top: 523 * theme.dimensions.absoluteHeight,
            left: 220 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            width: 120 * theme.dimensions.absoluteWidth
        },
        buttonContainer:{
            position: 'absolute',
            top: 600 * theme.dimensions.absoluteHeight,
            left: 31 * theme.dimensions.absoluteWidth,
            width: 320 * theme.dimensions.absoluteWidth,
            height: 60 * theme.dimensions.absoluteHeight
        }
    })
};

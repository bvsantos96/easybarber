import { useTheme } from '@styles/ThemeContext';
import { StyleSheet } from 'react-native';

export const getStyles = () => {
    const theme = useTheme();
    const containerWidth = 150 * theme.dimensions.absoluteWidth;
    return StyleSheet.create({
        container: {
            width: containerWidth,
            borderRadius: 15,
            backgroundColor: 'white',
            marginVertical: 10 * theme.dimensions.absoluteHeight,
            alignItems: 'center'
        },
        image: {
            width: containerWidth - 35 * theme.dimensions.absoluteWidth,
            height: 85 * theme.dimensions.absoluteHeight,
            objectFit: 'cover',
            borderRadius: 10,
        },
        textContainer: {
            width: containerWidth - 35 * theme.dimensions.absoluteWidth,
            alignItems: 'flex-start',
        },
        brandText: {
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        nameText: {
            fontFamily: 'Poppins',
            fontWeight: '400',
            fontSize: 10 * theme.dimensions.absoluteHeight,
            lineHeight: 16 * theme.dimensions.absoluteHeight,
            minHeight: 32 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left',
        },
        priceText: {
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

import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
            overflow: 'hidden',
            alignItems: 'center'
        },
        buttonContainer: {
            position: 'absolute',
            top: 590 * theme.dimensions.absoluteHeight,
            width: 319 * theme.dimensions.absoluteWidth,
            height: 58.75 * theme.dimensions.absoluteHeight,
            alignItems: 'center',

        },
        eclipse: {
            position: 'absolute',
            width: 243.2 * theme.dimensions.absoluteWidth,
            height: 243.2 * theme.dimensions.absoluteHeight,
            left: 73.4 * theme.dimensions.absoluteWidth,
            top: 69.68 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.imageBackground,
            borderRadius: (243.2 * theme.dimensions.absoluteWidth) / 2
        },
        lockImage: {
            position: 'absolute',
            width: 103.33 * theme.dimensions.absoluteWidth,
            height: 130 * theme.dimensions.absoluteHeight,
            left: 143.33 * theme.dimensions.absoluteWidth,
            top: 128.08 * theme.dimensions.absoluteHeight
        },
        mobileInputContainer: {
            position: 'absolute',
            top: 457.94 * theme.dimensions.absoluteHeight,
            left: 35.5 * theme.dimensions.absoluteWidth,
            width: 319 * theme.dimensions.absoluteWidth,
            height: 58.75 * theme.dimensions.absoluteHeight
        },
        enterPhone: {
            color: theme.colors.text.darkBlueGray,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Poppins',
            fontWeight: '700',
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            textAlign: 'center'
        },
        enterPhoneContainer: {
            position: 'absolute',
            top: 362.44 * theme.dimensions.absoluteHeight,
            left: 38.26 * theme.dimensions.absoluteWidth,
            width: 313.45 * theme.dimensions.absoluteWidth,
            height: 56 * theme.dimensions.absoluteHeight
        }
    })
};

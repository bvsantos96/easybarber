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
        chatImage: {
            position: 'absolute',
            width: 121.09 * theme.dimensions.absoluteWidth,
            height: 121.81 * theme.dimensions.absoluteHeight,
            left: 134.48 * theme.dimensions.absoluteWidth,
            top: 130.38 * theme.dimensions.absoluteHeight
        },
        keyImage: {
            position: 'absolute',
            width: 57.09 * theme.dimensions.absoluteWidth,
            height: 30.54 * theme.dimensions.absoluteHeight,
            left: 166.48 * theme.dimensions.absoluteWidth,
            top: 162.7 * theme.dimensions.absoluteHeight
        },
        resendCodeContainer: {
            position: 'absolute',
            top: 547.49 * theme.dimensions.absoluteHeight,
            width: '100%',
            alignItems: 'center',
        },
        row: {
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
        },
        resendCodeText: {
            color: theme.colors.text.darkBlueGray,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Poppins',
            fontWeight: '500',
            lineHeight: 24 * theme.dimensions.absoluteHeight,
        },
        resendCodeRedText: {
            color: theme.colors.text.link,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Poppins',
            fontWeight: '500',
            lineHeight: 24 * theme.dimensions.absoluteHeight,
        },
        insertCode: {
            color: theme.colors.text.darkBlueGray,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Poppins',
            fontWeight: '700',
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            textAlign: 'center'
        },
        insertCodeContainer: {
            position: 'absolute',
            top: 362.44 * theme.dimensions.absoluteHeight,
            left: 38.26 * theme.dimensions.absoluteWidth,
            width: 313.45 * theme.dimensions.absoluteWidth,
            height: 56 * theme.dimensions.absoluteHeight
        },
        verifyPhone: {
            color: theme.colors.text.black,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 25.2 * theme.dimensions.absoluteHeight,
            textAlign: 'center'
        },
        verifyPhoneContainer: {
            position: 'absolute',
            top: 68.5 * theme.dimensions.absoluteHeight,
            left: 137.5 * theme.dimensions.absoluteWidth,
            width: 115 * theme.dimensions.absoluteWidth,
            height: 25 * theme.dimensions.absoluteHeight
        },
        codeInputContainer: {
            position: 'absolute',
            top: 457.94 * theme.dimensions.absoluteHeight,
            left: 43.5 * theme.dimensions.absoluteWidth,
            width: 299.99 * theme.dimensions.absoluteWidth,
            height: 58.75 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'center'
        },
        codeInput: {
            width: 45 * theme.dimensions.absoluteWidth,
            height: 58.75 * theme.dimensions.absoluteHeight,
            borderWidth: 1,
            borderColor: theme.colors.text.lightGray,
            borderRadius: 9,
            shadowColor: '#000000',
            shadowOffset: {
                width: 20,
                height: 20,
            },
            shadowOpacity: 0.12,
            shadowRadius: 50,
            elevation: 5,
            textAlign: 'center',
            fontSize: theme.fonts.size._24,
            fontWeight: 'bold',
        },
        errorMessage: {
            position: 'absolute',
            top: (457.94 + 58.75 + 10) * theme.dimensions.absoluteHeight,
            left: 43.5 * theme.dimensions.absoluteWidth,
            width: 299.99 * theme.dimensions.absoluteWidth,
            color: 'red',
            textAlign: 'center',
            fontSize: theme.fonts.size._10,
            fontWeight: '500'
        },
    })
};

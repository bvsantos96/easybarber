import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const whitheButtonColor = "#FFF";
export const thinBorderBlack = "#263238";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        button: {
            width: theme.dimensions.input.width,
            height: theme.dimensions.input.height,
            borderRadius: 50 * theme.dimensions.absoluteMinDimension,
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        },
        textButton: {
            textAlign: 'center',
            fontSize: theme.fonts.size._18,
            fontFamily: 'Mazzard',
            fontWeight: 'bold',
        },
        smallButton: {
            borderRadius: 9 * theme.dimensions.absoluteMinDimension,
            borderWidth: 1 * theme.dimensions.absoluteMinDimension,
            borderStyle: 'solid',
            borderColor: theme.colors.button.border,
            paddingTop: 11 * theme.dimensions.absoluteHeight,
            paddingBottom: 11 * theme.dimensions.absoluteHeight,
            paddingLeft: 20 * theme.dimensions.absoluteWidth,
            paddingRight: 20 * theme.dimensions.absoluteWidth,
        },
        smallButtonIconImage: {
            resizeMode: 'cover',
            marginRight: 10 * theme.dimensions.absoluteWidth,
        },
        smallTextButton: {
            color: 'black',
            fontSize: theme.fonts.size._9,
            fontFamily: 'Poppins',
            fontWeight: 'normal',
        },
        brandTextButton: {
            color: 'black',
            fontSize: theme.fonts.size._13,
            fontFamily: 'Poppins',
            fontWeight: 'normal',
        },
        button11: {
            position: 'absolute',
            width: 313 * theme.dimensions.absoluteWidth,
            height: 58.7 * theme.dimensions.absoluteHeight,
            top: 634 * theme.dimensions.absoluteHeight,
            left: 39 * theme.dimensions.absoluteWidth,
        },
        button12: {
            position: 'absolute',
            width: 313 * theme.dimensions.absoluteWidth,
            height: 58.7 * theme.dimensions.absoluteHeight,
            top: 724 * theme.dimensions.absoluteHeight,
            left: 39 * theme.dimensions.absoluteWidth,
        },
        brandButton: {
            backgroundColor: theme.colors.backgroundColor,
            borderWidth: 1,
            borderRadius: 10,
            borderColor: theme.colors.button.border,
            height: 59 * theme.dimensions.absoluteHeight,
            width: 153 * theme.dimensions.absoluteWidth,
        },
        brandButtonIcon: {
            position: 'absolute',
            left: 27 * theme.dimensions.absoluteWidth,
            top: 11 * theme.dimensions.absoluteHeight,
        },
        brandIcon: {
            width: 33 * theme.dimensions.absoluteWidth,
            height: 33 * theme.dimensions.absoluteHeight,
        },
        brandButtonTextContainer: {
            position: 'absolute',
            top: 0,
            left: 71 * theme.dimensions.absoluteWidth,
            height: 59 * theme.dimensions.absoluteHeight,
        },
        brandButtonTitleContainer: {
            position: 'absolute',
            left: 0,
            top: 12 * theme.dimensions.absoluteHeight,
        },
        brandButtonText: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._9,
            fontWeight: 'normal',
            color: theme.colors.text.black,
        },
        brandButtonNameContainer: {
            position: 'absolute',
            left: 0,
            bottom: 14 * theme.dimensions.absoluteHeight,
        },
        brandText: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._13,
            fontWeight: 'normal',
            color: theme.colors.text.black,
        },
    })
};

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
            fontWeight: '900',
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
            fontWeight: '400',
        },
        brandTextButton: {
            color: 'black',
            fontSize: theme.fonts.size._13,
            fontFamily: 'Poppins',
            fontWeight: '400',
        },
        button11: {
            position: 'absolute',
            width: 313 * theme.dimensions.absoluteWidth,
            height: 58.7 * theme.dimensions.absoluteHeight,
            top : 634 * theme.dimensions.absoluteHeight,
            left: 39 * theme.dimensions.absoluteWidth,
        },
        button12: {
            position: 'absolute',
            width: 313 * theme.dimensions.absoluteWidth,
            height: 58.7 * theme.dimensions.absoluteHeight,
            top : 724 * theme.dimensions.absoluteHeight,
            left: 39 * theme.dimensions.absoluteWidth,
        },
    })
};

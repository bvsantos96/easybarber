import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: theme.dimensions.input.width,
            height: theme.dimensions.input.height,
        },
        inputSmallBorderRadius: {
            borderRadius: 10 * theme.dimensions.absoluteHeight,
        },
        title: {
            position: 'absolute',
            top: -10 * theme.dimensions.absoluteHeight,
            left: 10 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.backgroundColor,
            paddingHorizontal: 5 * theme.dimensions.absoluteWidth,
            zIndex: 1,
        },
        inputView: {
            width: '100%',
            height: '100%',
            flexDirection: 'row',
            alignItems: 'center',
            borderWidth: 1,
            borderColor: theme.colors.text.lightWhite,
            borderRadius: 50,
            borderStyle: 'solid',
        },
        iconView: {
            width: 0.8 * theme.dimensions.input.height,
            height: 0.8 * theme.dimensions.input.height,
            borderRadius: 0.4 * theme.dimensions.input.height,
            backgroundColor: theme.colors.imageBackground,
            alignItems: 'center',
            justifyContent: 'center',
            margin: 5 * theme.dimensions.absoluteHeight,
            marginRight: 10 * theme.dimensions.absoluteWidth,
        },
        showPasswordIcon: {
            width: 0.8 * theme.dimensions.input.height,
            height: 0.8 * theme.dimensions.input.height,
            alignItems: 'center',
            justifyContent: 'center',
            margin: 5 * theme.dimensions.absoluteHeight,
            marginRight: 10 * theme.dimensions.absoluteWidth,
            zIndex: 9999
        },
        textInput: {
            width: theme.dimensions.input.width - (0.8 * theme.dimensions.input.height),
            height: 40 * theme.dimensions.absoluteHeight,
            borderWidth: 0,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._15,
            fontFamily: 'Mazzard',
            fontWeight: '400',
        },
        textInputWithShowPasswordIcon: {
            width: theme.dimensions.input.width - (1.6 * theme.dimensions.input.height) - 10 * theme.dimensions.absoluteHeight - 20 * theme.dimensions.absoluteWidth,
            height: 40 * theme.dimensions.absoluteHeight,
            borderWidth: 0,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._15,
            fontFamily: 'Mazzard',
            fontWeight: '400',
        },
        icon: {
            width: 25 * theme.dimensions.absoluteHeight,
            height: 25 * theme.dimensions.absoluteHeight,
            resizeMode: 'contain',
        }
    })
};

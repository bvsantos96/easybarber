import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: theme.dimensions.input.width,
            height: theme.dimensions.input.height,
        },
        inputView: {
            width: '100%',
            height: '100%',
            flexDirection: 'row',
            alignItems: 'center',
            borderWidth: 1,
            borderColor: 'rgba(0, 0, 0, 0.29)',
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
        textInput: {
            width: '100%',
            height: 40 * theme.dimensions.absoluteHeight,
            borderWidth: 0,
            color: theme.colors.text,
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

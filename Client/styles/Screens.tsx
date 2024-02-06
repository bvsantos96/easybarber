import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        centeredLogo: {
            position: 'absolute',
            top: 238.52 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
            alignSelf: 'center',
            justifyContent: 'center',
        }
    })
};

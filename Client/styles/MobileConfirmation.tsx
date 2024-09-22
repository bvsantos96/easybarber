import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            width: theme.dimensions.width,
            backgroundColor: theme.colors.backgroundColor,
            overflow: 'hidden',
            alignItems: 'center'
        },
        title: {
            position: 'absolute',
            top: 30 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
        },
        input: {
            height: 50,
            width: '80%',
            borderColor: 'gray',
            borderWidth: 1,
            borderRadius: 5,
            paddingHorizontal: 10,
            marginBottom: 20,
        },
        inputsContainer: {
            position: 'absolute',
            top : 110 * theme.dimensions.absoluteHeight,
            width: '100%',
            alignItems: 'center',
        },
        buttonContainer: {
            position: 'absolute',
            top: 314 * theme.dimensions.absoluteHeight,
            width: '100%',
            alignItems: 'center',
        }
    })
};

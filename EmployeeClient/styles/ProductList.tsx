import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        listContainer: {
            position: 'absolute',
            width: theme.dimensions.width,
            height: theme.dimensions.heightWithoutStatusBar - theme.dimensions.tabHeight - 20 * theme.dimensions.absoluteHeight,
        },
    })
};

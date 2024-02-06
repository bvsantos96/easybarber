import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        image: {
            width: 348 * theme.dimensions.absoluteMinDimension,
            height: 348 * theme.dimensions.absoluteMinDimension,
        },
        imageContainer: {
            position: 'absolute',
            width: 348 * theme.dimensions.absoluteMinDimension,
            height: 348 * theme.dimensions.absoluteMinDimension,
            top: 258 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignItems: 'center',
        },
        roundBackground: {
            backgroundColor: theme.colors.imageBackground,
            overflow: 'hidden',
            justifyContent: 'center',
            alignItems: 'center',
            width: '90%',
            height: '90%',
            borderRadius: 55,
            position: 'absolute',
        },
        pageSelectionContainer: {
            position: 'absolute',
            width: "100%",
            top: 685.41 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
        },
        titleContainer: {
            position: "absolute",
            width: 300 * theme.dimensions.absoluteWidth,
            height: 66 * theme.dimensions.absoluteHeight,
            top: 91.92 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignItems: 'center',
        },
        subTitleContainer: {
            position: "absolute",
            width: 300 * theme.dimensions.absoluteWidth,
            height: 66 * theme.dimensions.absoluteHeight,
            top: 160 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignItems: 'center',
        },
        buttonContainer: {
            position: "absolute",
            width: 313 * theme.dimensions.absoluteWidth,
            height: 58 * theme.dimensions.absoluteHeight,
            top: 728 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignItems: 'center',
        },
    });
}

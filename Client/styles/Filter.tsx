import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 40,
            zIndex: 20,
        },
        topBarContainer: {
            position: 'absolute',
            top: 34 * theme.dimensions.absoluteHeight,
        },
        title: {
            position: 'absolute',
            left: 36 * theme.dimensions.absoluteWidth,
            fontSize: 17 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 25.5 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.black,
        },
        clear: {
            position: 'absolute',
            left: 315 * theme.dimensions.absoluteWidth,
            color: theme.colors.mainColor,
            fontSize: 16 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 24 * theme.dimensions.absoluteHeight,
        },
        input: {
            position: 'absolute',
            top: 88 * theme.dimensions.absoluteHeight,
            left: 35 * theme.dimensions.absoluteWidth,
            width: 325 * theme.dimensions.absoluteWidth,
            height: 64 * theme.dimensions.absoluteHeight,
        },
        ratingTitleContainer: {
            position: 'absolute',
            top: 175 * theme.dimensions.absoluteHeight,
        },
        ratingTitle: {
            fontSize: 16 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.black,
            position: 'absolute',
            left: 36 * theme.dimensions.absoluteWidth,
        },
        ratingStars: {
            position: 'absolute',
            left: 310 * theme.dimensions.absoluteWidth,
            fontSize: 14 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 21 * theme.dimensions.absoluteHeight,
        },
        starsContainer: {
            position: 'absolute',
            top: 210 * theme.dimensions.absoluteHeight,
            left: 38 * theme.dimensions.absoluteWidth,
        },
        availableTimeTitle: {
            position: 'absolute',
            top: 280 * theme.dimensions.absoluteHeight,
            left: 36 * theme.dimensions.absoluteWidth,
            fontSize: 16 * theme.dimensions.absoluteWidth,
            fontFamily: 'Mazzard',
            fontWeight: '600',
            lineHeight: 24 * theme.dimensions.absoluteHeight,
        },
        timeSelectionContainer: {
            position: 'absolute',
            top: 326 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            height: 64 * theme.dimensions.absoluteHeight,
        },
        applyContainer: {
            position: 'absolute',
            top: 422 * theme.dimensions.absoluteHeight,
            left: 35 * theme.dimensions.absoluteWidth,
            width: theme.dimensions.width - 65,
        },
        picker: {
            width: '100%',
            height: '100%',
            backgroundColor: 'white',
            borderRadius: 25,
            borderWidth: 1,
            borderColor: 'rgba(0, 0, 0, 0.22)',
            borderStyle: 'solid',
        },
        pickerItem: {
            paddingLeft: 22 * theme.dimensions.absoluteWidth,
            height: '100%',
            width: '100%',
            fontSize: 14 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 21 * theme.dimensions.absoluteHeight,
        },
        pickerLabelContainer: {
            position: "absolute",
            top: -8 * theme.dimensions.absoluteHeight,
            left: 30 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.backgroundColor,
        },
        pickerLabel: {
            fontSize: 12 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 18 * theme.dimensions.absoluteHeight,
        },
        from: {
            position: 'absolute',
            left: 35 * theme.dimensions.absoluteWidth,
            width: 152 * theme.dimensions.absoluteWidth,
            height: "100%",
        },
        to: {
            position: 'absolute',
            left: 209 * theme.dimensions.absoluteWidth,
            width: 152 * theme.dimensions.absoluteWidth,
            height: "100%",
        }
    })
};

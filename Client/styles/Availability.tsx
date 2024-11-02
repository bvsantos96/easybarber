import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
    return StyleSheet.create({
        calendar: {
            position: "absolute",
            top: 60 * theme.dimensions.absoluteHeight,
            left: padding,
            width: theme.dimensions.width - padding * 2,
            maxHeight: 240 * theme.dimensions.absoluteHeight,
        },
        slotsContainer: {
            position: 'absolute',
            backgroundColor: theme.colors.backgroundColor,
            top: 370 * theme.dimensions.absoluteHeight,
            left: 2 * padding,
            width: theme.dimensions.width - (padding * 4),
            maxHeight: 170 * theme.dimensions.absoluteHeight,
        },
        slotsTitle: {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._16,
            fontWeight: 600,
            lineHeight: 24 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.02,
        },
        "timeSlotsContainer": {
            top: 10 * theme.dimensions.absoluteHeight,
            height: 140 * theme.dimensions.absoluteHeight,
        },
        "slotContainer": {
            width: 155 * theme.dimensions.absoluteWidth,
            height: 51 * theme.dimensions.absoluteHeight,
            borderWidth: 2 * theme.dimensions.absoluteWidth,
            borderRadius: 50 * theme.dimensions.absoluteMinDimension,
            justifyContent: 'center',
            alignItems: 'center'
        },
        "selectedBorder": {
            borderColor: theme.colors.mainColor,
        },
        "slotText": {
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._12,
            fontWeight: 600,
            lineHeight: 18 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.02,
            textAlign: 'center',
        },
        selectedText: {
            color: theme.colors.mainColor,
        },
        noSlotsContainer: {
            height: 140 * theme.dimensions.absoluteHeight,
            justifyContent: 'center',
            alignSelf: 'center',
            alignItems: 'center',
        },
        noSlots: {
            fontFamily: 'Poppins',
            textAlign: 'center',
            fontSize: theme.fonts.size._12,
            fontWeight: 600,
            lineHeight: 18 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.02,
            color: theme.colors.mainColor,
        },
    });
}

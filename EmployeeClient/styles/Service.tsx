import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;
    const iconSize = 20 * theme.dimensions.absoluteWidth;
    const iconContainerSize = iconSize + 15 * theme.dimensions.absoluteWidth;
    const imageContainerSize = 180 * theme.dimensions.absoluteWidth;
    const inputHeight = 50 * theme.dimensions.absoluteHeight;
    const inputSpaceInBetween = 20 * theme.dimensions.absoluteHeight;
    return StyleSheet.create({
        container: {
            width: "100%",
            height: "100%",
            alignItems: 'center',
        },
        imageContainer: {
            width: imageContainerSize,
            height: imageContainerSize,
            top: padding,
        },
        imageStyle: {
            objectFit: 'cover',
            width: "100%",
            height: "100%",
            borderRadius: imageContainerSize / 8,
        },
        iconContainer: {
            position: "absolute",
            bottom: -5 * theme.dimensions.absoluteWidth,
            width: iconContainerSize,
            height: iconContainerSize,
            alignItems: 'center',
            justifyContent: 'center',
            borderRadius: iconContainerSize / 2,
            borderWidth: 1 * theme.dimensions.absoluteWidth,
            borderColor: theme.colors.backgroundColor,
        },
        icon: {
            width: iconSize,
            height: iconSize,
        },
        addIcon: {
            right: -5 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.mainColor,
            color: theme.colors.backgroundColor,
        },
        inputContainer: {
            position: "absolute",
            alignItems: 'center',
            top: imageContainerSize + inputSpaceInBetween,
            width: "100%",
            height: (inputHeight + inputSpaceInBetween) * 5 + inputSpaceInBetween + theme.dimensions.input.height,
            justifyContent: "space-between",
        },
        input: {
            height: inputHeight
        },
        noImage: {
            backgroundColor: theme.colors.text.lightGray,
        },
    });
}

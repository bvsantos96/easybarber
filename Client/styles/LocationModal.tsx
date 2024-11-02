import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: '100%',
            height: '100%',
            backgroundColor: theme.colors.backgroundColor,
            borderTopLeftRadius: 31 * theme.dimensions.absoluteWidth,
            borderTopRightRadius: 31 * theme.dimensions.absoluteWidth,
        },
        horizontalPadding: {
            paddingHorizontal: 20 * theme.dimensions.absoluteWidth,
        },
        maxHeight: {
            height: '70%',
        },
        maxWidth: {
            width: '100%',
        },
        centerHorizontal: {
            alignItems: 'center',
            width: '100%',
        },
        divider: {
            maxHeight: 25 * theme.dimensions.absoluteHeight,
            minHeight: 10 * theme.dimensions.absoluteHeight,
        },
        titleContainer: {
            width: '100%',
            alignItems: 'flex-start',
        },
        title: {
            color: theme.colors.text.main,
            fontSize: 18 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '900',
        },
        itemContainer: {
            width: '100%',
            height: 70 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
            borderBottomWidth: 1,
            borderBottomColor: theme.colors.borderAlt,
            flexDirection: 'row',
        },
        selectedItem: {
            backgroundColor: theme.colors.borderAlt,
            borderBottomWidth: 0,
        },
        underlinedText: {
            textDecorationLine: 'underline',
        },
        itemTitle: {
            color: theme.colors.text.main,
            fontSize: 14 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '900',
        },
        itemSubtitle: {
            color: theme.colors.text.lightBlack,
            fontSize: 11 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '400',
        },
        rowContainer: {
            flexDirection: 'row',
            alignItems: 'center',
        },
        itemIcon: {
            width: 24 * theme.dimensions.absoluteWidth,
            height: 24 * theme.dimensions.absoluteHeight,
            marginRight: 5 * theme.dimensions.absoluteWidth,
        },
        itemIconContainer: {
            width: '10%',
            height: "100%",
            alignItems: 'center',
            justifyContent: 'center',
        },
        itemIconPadding: {
            padding: 10 * theme.dimensions.absoluteWidth,
        },
        itemTextContainer: {
            width: '80%',
            height: '100%',
            justifyContent: 'center',
        },
    });
}

import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    const dropDownMaxHeight = 250 * theme.dimensions.absoluteHeight;
    return StyleSheet.create({
        container: {
            flexDirection: 'row',
        },
        removeButton: {
            minWidth: 25 * theme.dimensions.absoluteWidth,
            color: theme.colors.mainColor
        },
        selected: {
            alignSelf: 'center',
            justifyContent: 'center',
            flexDirection: "row",
            alignItems: "center",
            borderBottomRightRadius: 25 * theme.dimensions.absoluteHeight,
            borderTopRightRadius: 25 * theme.dimensions.absoluteHeight,
            borderTopLeftRadius: 25 * theme.dimensions.absoluteHeight,
            borderBottomLeftRadius: 25 * theme.dimensions.absoluteHeight,
            borderColor: theme.colors.mainColor,
            borderWidth: 1 * theme.dimensions.absoluteWidth,
            height: 40 * theme.dimensions.absoluteHeight,
            backgroundColor: "transparent",
            paddingHorizontal: 10 * theme.dimensions.absoluteWidth,
            minWidth: 50 * theme.dimensions.absoluteWidth,
            right: -50 * theme.dimensions.absoluteWidth,
        },
        selectedText: {
            alignSelf: 'center',
            color: theme.colors.text.main,
            fontFamily: 'Poppins',
            fontSize: theme.fonts.size._13,
            fontWeight: '600',
            flexShrink: 1,
        },
        button: {
            backgroundColor: theme.colors.mainColor,
            color: theme.colors.backgroundColor,
            minWidth: 25 * theme.dimensions.absoluteWidth,
            borderRadius: 25 * theme.dimensions.absoluteHeight,
            width: 50 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
            justifyContent: 'center',
        },
        modalButton: {
            position: "absolute",
            bottom: theme.dimensions.input.height / 2,
        },
        modal: {
            width: 328 * theme.dimensions.absoluteWidth,
            //height: dropDownMaxHeight + 2 * theme.dimensions.input.height + 32 * theme.dimensions.absoluteHeight,
            //maxHeight: dropDownMaxHeight + 2 * theme.dimensions.input.height + 32 * theme.dimensions.absoluteHeight,
            height: theme.dimensions.input.height * 3,
            maxHeight: theme.dimensions.input.height * 3,
            alignSelf: 'center',
            alignItems: 'center',
        },
        dropdownContainer: {
            height: dropDownMaxHeight,
        },
        dropdown: {
            width: 328 * theme.dimensions.absoluteWidth,
            margin: 5 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 12 * theme.dimensions.absoluteWidth,
            padding: 12 * theme.dimensions.absoluteWidth,
            shadowColor: theme.colors.mainColor,
            shadowOffset: {
                width: 0,
                height: 1,
            },
            shadowOpacity: 0.2,
            shadowRadius: 1.41,
            elevation: 2,
        },
        icon: {
            marginRight: 5 * theme.dimensions.absoluteWidth,
            color: theme.colors.mainColor,
            width: 20 * theme.dimensions.absoluteWidth,
        },
        item: {
            padding: 17,
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'center',
        },
        textItem: {
            flex: 1,
            fontSize: theme.fonts.size._16,
            color: theme.colors.text.lightBlack,
        },
        textItemSelected: {
            color: theme.colors.text.main,
        },
        placeholderStyle: {
            fontSize: theme.fonts.size._15,
            fontFamily: 'Mazzard',
            fontWeight: '400',
            color: theme.colors.text.lightBlack,
        },
        selectedPlaceholderStyle: {
            fontSize: theme.fonts.size._16,
            color: theme.colors.text.main,
        },
        selectedTextStyle: {
            fontSize: theme.fonts.size._16,
            color: theme.colors.text.main,
        },
        iconStyle: {
            width: 20 * theme.dimensions.absoluteWidth,
            height: 20 * theme.dimensions.absoluteHeight,
        },
        inputSearchStyle: {
            height: 40 * theme.dimensions.absoluteHeight,
            fontSize: theme.fonts.size._16,
        },
    })
};

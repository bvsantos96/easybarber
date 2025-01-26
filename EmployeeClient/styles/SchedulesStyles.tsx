import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
        },
        calendar: {
            alignSelf: 'center',
            width: 358 * theme.dimensions.absoluteWidth,
        },
        appointmentController: {
            alignSelf: 'center',
            width: 358 * theme.dimensions.absoluteWidth,
            height: 95 * theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 15,
            marginVertical: 5 * theme.dimensions.absoluteHeight,
        },
        shadow: {
            ...(theme.shadow as any),
        },
        appoitmentTextContainer: {
            position: 'absolute',
            left: 20 * theme.dimensions.absoluteWidth,
        },
        timeText: {
            position: 'absolute',
            top: 16 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._13,
        },
        titleText: {
            position: 'absolute',
            top: 38 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._18,
            fontWeight: 'bold',
        },
        subTitleText: {
            position: 'absolute',
            top: 65 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._13,
        },
        icon: {
            position: 'absolute',
            right: 20 * theme.dimensions.absoluteWidth,
            top: 20 * theme.dimensions.absoluteHeight,
            width: 14 * theme.dimensions.absoluteWidth,
            color: theme.colors.mainColor,
        },
        buttonContainer: {
            position: 'absolute',
            bottom: 0,
            alignSelf: 'center',
        },
        listContainer: {
            top: 20 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            minHeight: 226 * theme.dimensions.absoluteHeight,
            maxHeight: 226 * theme.dimensions.absoluteHeight,
        },
        modal: {
            flex: 1,
            width: theme.dimensions.width,
            height: 393 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
        },
        modalTitle: {
            position: 'absolute',
            top: 19 * theme.dimensions.absoluteHeight,
            textAlign: 'center',
            fontSize: theme.fonts.size._18,
            fontWeight: 'bold',
        },
        modalContent: {
            position: 'absolute',
            alignSelf: 'center',
            alignItems: 'center',
            width: 358 * theme.dimensions.absoluteWidth,
            top: 62 * theme.dimensions.absoluteHeight,
            height: 200 * theme.dimensions.absoluteHeight,
        },
        modalButton: {
            position: 'absolute',
            bottom: 135 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
            width: theme.dimensions.width,
            height: 60 * theme.dimensions.absoluteHeight,
        },
        modalInput: {
            borderWidth: 1,
            borderColor: theme.colors.text.lightGray,
            color: theme.colors.text.lightBlack,
        },
        inputIcon: {
            width: 18 * theme.dimensions.absoluteWidth,
            color: theme.colors.text.lightWhite,
        },
        timeInput: {
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'space-between',
        }
    });
}

import { StyleSheet } from "react-native";
import { useTheme } from "./ThemeContext";

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            width: theme.dimensions.width,
            height: theme.dimensions.heightWithoutStatusBar - theme.dimensions.headerHeight,
            backgroundColor: theme.colors.backgroundColor,
        },
        listContainer: {
            position: "absolute",
            top: 5 * theme.dimensions.absoluteHeight,
            height: theme.dimensions.heightWithoutStatusBar - theme.dimensions.headerHeight,
            width: theme.dimensions.width,
            bottom: 0,
            alignItems: "center",
            justifyContent: "center",
        },
        icon: {
            zIndex: 3,
            elevation: 2,
            borderWidth: 1,
            borderColor: theme.colors.infoColor,
            backgroundColor: theme.colors.infoColor,
            borderRadius: 50 * theme.dimensions.absoluteWidth,
            marginHorizontal: 2 * theme.dimensions.absoluteWidth,
            padding: 10 * theme.dimensions.absoluteWidth,
            height: 50 * theme.dimensions.absoluteWidth,
            width: 50 * theme.dimensions.absoluteWidth,
            alignItems: "center",
            justifyContent: "center",
            fontSize: 24 * theme.dimensions.absoluteWidth,
        },
        redIcon: {
            backgroundColor: theme.colors.errorColor,
            borderColor: theme.colors.errorColor,
        },
        slidingContainer: {
            justifyContent: "center",
            alignSelf: "center",
            width: 345 * theme.dimensions.absoluteWidth,
            height: 80 * theme.dimensions.absoluteHeight,
        },
        listItemContainer: {
            flexDirection: "row",
            alignItems: "center",
            width: "100%",
            height: "99%",
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 11,
        },
        imageStyle: {
            width: '95%',
            height: '95%',
            borderRadius: 50,
        },
        imageContainer: {
            position: 'absolute',
            left: 20 * theme.dimensions.absoluteWidth,
            width: 56 * theme.dimensions.absoluteWidth,
            height: 56 * theme.dimensions.absoluteHeight,
            borderRadius: 50,
            borderWidth: 1,
            borderColor: theme.colors.mainColor,
            justifyContent: 'center',
            alignItems: 'center',
        },
        textContainer: {
            position: 'absolute',
            justifyContent: 'space-evenly',
            left: 93 * theme.dimensions.absoluteWidth,
            width: 143 * theme.dimensions.absoluteWidth,
            height: '100%',
        },
        titleText: {
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._14,
            fontFamily: 'Poppins',
            fontWeight: "900",
        },
        descriptionText: {
            color: theme.colors.text.lightWhite,
            fontSize: theme.fonts.size._12,
            fontFamily: 'Poppins',
            fontWeight: "900",
        },
        statusText: {
            position: 'absolute',
            right: 20 * theme.dimensions.absoluteWidth,
            alignSelf: 'center',
            color: theme.colors.text.lightBlack,
            fontSize: theme.fonts.size._10,
            fontFamily: 'Poppins',
            fontWeight: "900",
        },
        bottomButton: {
            position: 'absolute',
            bottom: 10 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
            justifyContent: 'center',
            alignSelf: 'center',
        },
        addEmployeeContainer: {
            backgroundColor: theme.colors.backgroundColor,
            width: theme.dimensions.width,
            height: 293 * theme.dimensions.absoluteHeight,
            maxHeight: 293 * theme.dimensions.absoluteHeight
        },
        addEmployeeText: {
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._18,
            fontFamily: 'Poppins',
            fontWeight: "900",
            alignSelf: 'center',
            marginTop: 20 * theme.dimensions.absoluteHeight,
        },
        phoneNumberContainer: {
            position: 'absolute',
            top: 70 * theme.dimensions.absoluteHeight,
            height: 50 * theme.dimensions.absoluteHeight,
            alignSelf: 'center',
        },
        inputIcon: {
            width: 18 * theme.dimensions.absoluteWidth,
            backgroundColor: theme.colors.mainColor
        },
        modalButton: {
            position: 'absolute',
            bottom: theme.dimensions.input.height,
            alignItems: 'center',
            justifyContent: 'center',
            alignSelf: 'center',
        },
        inputWidth: {
            width: theme.dimensions.input.width
        },
        heightWithoutStatusBar: {
            height: theme.dimensions.heightWithoutStatusBar,
        },
        displayEmployeeContainer: {
            backgroundColor: theme.colors.backgroundColor,
            width: theme.dimensions.width,
            height: 500 * theme.dimensions.absoluteHeight,
        },
        displayEmployeeImageStyle: {
            position: 'absolute',
            top: 10 * theme.dimensions.absoluteHeight,
            width: 100 * theme.dimensions.absoluteWidth,
            height: 100 * theme.dimensions.absoluteHeight,
            borderRadius: 50,
            alignSelf: 'center',
        },
        displayEmployeeRating: {
            position: 'absolute',
            top: 120 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.lightWhite,
            fontSize: theme.fonts.size._12,
            fontFamily: 'Poppins',
            fontWeight: "600",
            alignSelf: 'center',
        },
        displayEmployeeName: {
            position: 'absolute',
            top: 150 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.main,
            fontSize: theme.fonts.size._18,
            fontFamily: 'Poppins',
            fontWeight: "900",
            alignSelf: 'center',
        },
        displayEmployeeDesc: {
            position: 'absolute',
            top: 180 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.lightWhite,
            fontSize: theme.fonts.size._12,
            fontFamily: 'Poppins',
            fontWeight: "600",
            alignSelf: 'center',
        },
        servicesContainer: {
            position: 'absolute',
            top: 210 * theme.dimensions.absoluteHeight,
            width: "90%",
            alignSelf: 'center',
            alignItems: 'center',
            justifyContent: 'center',
            height: 80 * theme.dimensions.absoluteHeight,
            flexDirection: 'row',
            overflow: 'hidden',
        }
    });
}

import { StyleSheet } from 'react-native';
import { useTheme } from "./ThemeContext"

export const getStyles = () => {
    const theme = useTheme();
    const padding = (theme.dimensions.width - 356.11 * theme.dimensions.absoluteWidth) / 2;

    return StyleSheet.create({
        container: {
            flex: 1,
            width: "100%",
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
            textAlign: 'center',
            alignSelf: 'center',
            alignItems: 'center',
            alignContent: 'center',
        },
        imageStyle: {
            position: 'absolute',
            width: 356 * theme.dimensions.absoluteWidth,
            height: 190 * theme.dimensions.absoluteHeight,
            top: padding,
            left: padding,
            borderRadius: padding,
        },
        nameText:{
            position: 'absolute',
            width: 55 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 210 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left'
        },
        nameInput:{
            position: 'absolute',
            top: 253 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 52 * theme.dimensions.absoluteHeight,
            width: 327 * theme.dimensions.absoluteWidth
        },
        phoneText:{
            position: 'absolute',
            width: 55 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 324 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left'
        },
        phoneInput:{
            position: 'absolute',
            top: 362 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 52 * theme.dimensions.absoluteHeight,
            width: 327 * theme.dimensions.absoluteWidth
        },
        addressText:{
            position: 'absolute',
            width: 75 * theme.dimensions.absoluteWidth,
            height: 28 * theme.dimensions.absoluteHeight,
            top: 437 * theme.dimensions.absoluteHeight,
            left: 44 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '500',
            fontSize: 16 * theme.dimensions.absoluteHeight,
            lineHeight: 28 * theme.dimensions.absoluteHeight,
            letterSpacing: 0,
            textAlign: 'left'
        },
        addressInput:{
            position: 'absolute',
            top: 475 * theme.dimensions.absoluteHeight,
            left: 32 * theme.dimensions.absoluteWidth,
            height: 52 * theme.dimensions.absoluteHeight,
            width: 327 * theme.dimensions.absoluteWidth
        },
        buttonContainer:{
            position: 'absolute',
            top: 567 * theme.dimensions.absoluteHeight,
            left: 31 * theme.dimensions.absoluteWidth,
            width: 320 * theme.dimensions.absoluteWidth,
            height: 60 * theme.dimensions.absoluteHeight
        },
        uploadContainer:{
            position: 'absolute',
            width: 114 * theme.dimensions.absoluteWidth,
            height: 114 * theme.dimensions.absoluteHeight,
            top: 31 * theme.dimensions.absoluteHeight,
            left: 140 * theme.dimensions.absoluteWidth,
            borderRadius: padding,
        },
        uploadIcon:{
            width: 114 * theme.dimensions.absoluteWidth,
            height: 114 * theme.dimensions.absoluteHeight,
            color: theme.colors.mainColor
        }
    });
}

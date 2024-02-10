import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        shadow: {
            ...theme.shadow as any,
        },
        container:{
            flex: 1,
            width: theme.dimensions.width,
            backgroundColor: theme.colors.mainColor,
            overflow: 'hidden',
            alignItems: 'center'
        },
        logoContainer:{
            position: 'absolute',
            top: (42 + theme.dimensions.statusBarHeight / 2) * theme.dimensions.absoluteHeight,
            width: 113 * theme.dimensions.absoluteWidth,
            height: 113* theme.dimensions.absoluteHeight,
            backgroundColor: theme.colors.backgroundColor,
            borderRadius: 15,
            alignItems: 'center',
            justifyContent: 'center',
        },
        logo: {
            width: 100 * theme.dimensions.absoluteWidth,
            height: 100 * theme.dimensions.absoluteHeight,
        },
        bottomTabContainer:{
            position: 'absolute',
            bottom: 0,
            width: theme.dimensions.width,
            height: 573 * theme.dimensions.absoluteHeight,
            borderTopLeftRadius: 40,
            borderTopRightRadius: 40,
            alignItems: 'center',
            backgroundColor: theme.colors.backgroundColor,
        },
        titleContainer: {
            position: 'absolute',
            top: 30 * theme.dimensions.absoluteHeight,
            alignItems: 'center',
        },
        inputsContainer: {
            position: 'absolute',
            top : 110 * theme.dimensions.absoluteHeight,
            width: '100%',
            alignItems: 'center',
        },
        forgotPassContainer:{
            position: 'absolute',
            top: 270  * theme.dimensions.absoluteHeight,
            right: 40 * theme.dimensions.absoluteWidth,
        },
        forgotPass:{
            color: theme.colors.text.lightBlack,
            fontSize: theme.fonts.size._17,
            fontFamily: 'Mazzard',
            fontWeight: '500',
            lineHeight: 20 * theme.dimensions.absoluteHeight,
        },
        buttonContainer: {
            position: 'absolute',
            top: 314 * theme.dimensions.absoluteHeight,
            width: '100%',
            alignItems: 'center',
        },
        alreadyRegisteredContainer: {
            width: "100%",
            flexDirection: 'row',
            justifyContent: 'center',
        },
        newUserContainer: {
            position:  'absolute',
            top: 395 * theme.dimensions.absoluteHeight,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'center',
        },
        newUserText: {
            color: theme.colors.text.lightBlack,
            fontSize: theme.fonts.size._16,
            fontFamily: 'Mazzard',
            fontWeight: '500',
            lineHeight: 19 * theme.dimensions.absoluteHeight,
        },
        newUserRedText: {
            color: theme.colors.text.link,
            fontSize: theme.fonts.size._18,
            fontFamily: 'Mazzard',
            fontWeight: '600',
            lineHeight: 21 * theme.dimensions.absoluteHeight,
        },
        appleButtonContainer: {
            position: 'absolute',
            bottom: 68 * theme.dimensions.absoluteHeight,
            left: 34 * theme.dimensions.absoluteWidth,
        },
        googleButtonContainer: {
            position: 'absolute',
            bottom: 68 * theme.dimensions.absoluteHeight,
            right: 34 * theme.dimensions.absoluteWidth,
        },
    })
};

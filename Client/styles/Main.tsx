import { StyleSheet } from 'react-native';
import { useTheme } from './ThemeContext';

export const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            alignItems: 'center',
            backgroundColor: theme.colors.backgroundColor,
        },
        containerSpaceBetween: {
            flex: 1,
            justifyContent: 'space-between',
            alignItems: 'center',
            backgroundColor: theme.colors.backgroundColor,
        },
        containerCenter: {
            flex: 1,
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: theme.colors.backgroundColor,
            height: theme.dimensions.height, 
        },
        containerMax: {
            flex: 1,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor
        },
        spaceBetween: {
            justifyContent: 'space-between',
        },
        backgroundMainColor: {
            backgroundColor: theme.colors.mainColor,
        },
        hMargin5: {
            marginHorizontal: 5
        },
        hMargin3: {
            marginHorizontal: 3
        },
        w50c: {
            minWidth: '50%',
            alignItems: 'center',
        },
        w100: {
            minWidth: '100%',
        },
        w100Center: {
            minWidth: '100%',
            alignItems: 'center',
        },
        fixBottom: {
            position: 'absolute',
            bottom: 0
        },
        paddingBottom10: {
            paddingBottom: '50%'
        },
        noOverflow: {
            overflow: 'hidden'
        },
        loginContainer: {
            width: theme.dimensions.width,
            height: theme.dimensions.height * 0.7,
            justifyContent: 'space-between',
            alignItems: 'center',
            backgroundColor: theme.colors.backgroundColor,
            borderTopLeftRadius: 31,
            borderTopRightRadius: 31,
        },
        paddingRight10: {
            paddingRight: '10%',
        },
        alignLeft: {
            alignItems: 'flex-start',
            textAlign: 'left',
        },
        alignRight: {
            alignItems: 'flex-end',
            textAlign: 'right',
        },
        alignCenter: {
            alignItems: 'center',
            textAlign: 'center',
        },
        justifyCenter: {
            justifyContent: 'center',
        },
        row: {
            flexDirection: 'row',
        },
        hMargin2: {
            marginHorizontal: "2%"
        },
        hPadding2: {
            paddingHorizontal: "2%"
        },
        redBold: {
            color: theme.colors.mainColor,
            fontWeight: '900',
        },
        padding15px: {
            padding: 15,
        },
        normalText: {
            fontSize: 16,
            fontFamily: 'Mazzard',
            fontWeight: '500',
        },
        fontSize19: {
            fontSize: 19,
        },
        fontSize18: {
            fontSize: 18,
        },
        fontSize17: {
            fontSize: 17,
        },
        lightTextColor: {
            color: theme.colors.text.lightBlack,
        },
        rMargin10: {
            marginRight: 10,
        },
        rMargin7: {
            marginRight: 7,
        },
        rMargin5: {
            marginRight: 5,
        },
        lMargin10: {
            marginLeft: 10,
        },
        lMargin7: {
            marginLeft: 7,
        },
        lMargin5: {
            marginLeft: 5,
        },
        zIndex10: {
            zIndex: 10,
        },
        fontPoppins: {
            fontFamily: 'Poppins',
        },
        fontWeight400: {
            fontWeight: '400',
        },
        fontWeight600: {
            fontWeight: '600',
        },
        fontWeight700: {
            fontWeight: '700',
        },
        fonstSize18: {
            fontSize: 18,
        },
        colorDarkTitle: {
            color: theme.colors.text.black,
        },
        overlay: {
            position: 'absolute',
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: 'rgba(0,0,0,0.5)',
            zIndex: 10,
        },
        shadow: {
            elevation: 8,
            shadowColor: '#000',
            shadowOffset: {
                width: 0,
                height: 2,
            },
            shadowOpacity: 0.2,
            shadowRadius: 4,
        }
    })
};

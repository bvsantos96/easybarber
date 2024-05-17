import { Image, Text, View, StyleSheet } from "react-native";

import { useTheme } from "../styles/ThemeContext";

export default function Loading() {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    return (
        <View style={styles.container}>
            <Image  style={styles.icon} source={require('../assets/icons/loading.gif')} />
            <Text style={styles.textTitle}>{texts.gettingStarted}</Text>
            <Text style={styles.textSubtitle}>{texts.mayTakeSecs}</Text>
        </View>
    );
}

const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: theme.colors.backgroundColor,
        width: theme.dimensions.width,
        height: theme.dimensions.height,
        justifyContent: 'center',
        alignItems: 'center',
    },
    icon: {
        width: 33 * theme.dimensions.absoluteWidth,
        height: 33 * theme.dimensions.absoluteWidth,
        position: 'absolute',
        top: 320 * theme.dimensions.absoluteHeight,
        left: 182 * theme.dimensions.absoluteWidth,
    },
    textTitle: {
        fontSize: 17 * theme.dimensions.absoluteWidth,
        fontFamily: 'Poppins',
        fontWeight: '600',
        lineHeight: 25 * theme.dimensions.absoluteHeight,
        letterSpacing: 0.17,
        color: theme.colors.text.black,
    },
    textSubtitle: {
        fontSize: 13 * theme.dimensions.absoluteWidth,
        fontFamily: 'Poppins',
        fontWeight: '400',
        lineHeight: 25 * theme.dimensions.absoluteHeight,
        color: theme.colors.text.black,
    },
});
}

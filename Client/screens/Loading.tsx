import { Image, Text, View, StyleSheet } from "react-native";
import { PropNavigation } from "../App";

import { absoluteHeight, absoluteWidth, backgroundColor, height, textBlackColor, width } from "../styles/Main";

export default function Loading() {
    const texts = require("@lang/en.json");
    return (
        <View style={styles.container}>
            <Image  style={styles.icon} source={require('../assets/icons/loading.gif')} />
            <Text style={styles.textTitle}>{texts.gettingStarted}</Text>
            <Text style={styles.textSubtitle}>{texts.mayTakeSecs}</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: backgroundColor,
        width: width,
        height: height,
        justifyContent: 'center',
        alignItems: 'center',
    },
    icon: {
        width: 33 * absoluteWidth,
        height: 33 * absoluteWidth,
        position: 'absolute',
        top: 320 * absoluteHeight,
        left: 182 * absoluteWidth,
    },
    textTitle: {
        fontSize: 17 * absoluteWidth,
        fontFamily: 'Poppins',
        fontWeight: '600',
        lineHeight: 25 * absoluteHeight,
        letterSpacing: 0.17,
        color: textBlackColor,
    },
    textSubtitle: {
        fontSize: 13 * absoluteWidth,
        fontFamily: 'Poppins',
        fontWeight: '400',
        lineHeight: 25 * absoluteHeight,
        color: textBlackColor,
    },
});

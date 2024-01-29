import { Text, View, StyleSheet } from "react-native";
import { PropNavigation } from "../App";

import { absoluteHeight, absoluteWidth, backgroundColor, height, textBlackColor, width } from "../styles/Main";

import { SvgCssUri } from 'react-native-svg/css';

export default function Loading({ navigation }: PropNavigation) {
    const texts = require("@lang/en.json");
    return (
        <View style={styles.container}>
            <SvgCssUri
                width="100"
                height="100"
                uri="https://dev.w3.org/SVG/tools/svgweb/samples/svg-files/ruby.svg"
            />            
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

import { Text, StyleProp, ViewStyle, Animated, Easing, View } from "react-native";
import { getStyles } from "../styles/ExpandableView";
import Pressable from "./Pressable";
import { useEffect, useState } from "react";

type ExpandableViewProps = {
    children?: React.ReactNode,
    style?: StyleProp<ViewStyle>,
    maxHeight: number,
    minHeight?: number,
    title?: string,
    expanded?: boolean,
    onExpand?: () => void,
}

export default function ExpandableView({ children = <></>, style = {}, minHeight = 0, maxHeight = -1, title = "", expanded = true, onExpand = () => { } }: ExpandableViewProps) {
    const texts = require('@lang/en.json');
    const styles = getStyles();
    const expandCurrent = () => {
        onExpand();
    }
    const _minHeight = minHeight === 0 ? 0 : minHeight;
    const [heightAnim] = useState(new Animated.Value(expanded ? maxHeight : _minHeight));

    useEffect(() => {
        console.log(`expanded: ${expanded}, maxHeight: ${maxHeight}, _minHeight: ${_minHeight}`);
        Animated.timing(
            heightAnim,
            {
                toValue: expanded ? maxHeight : _minHeight,
                duration: 300,
                easing: Easing.ease,
                useNativeDriver: false,
            }
        ).start();

    }, [expanded, heightAnim, maxHeight]);

    return (
        <>
            <View style={styles.titleContainer}>
                <Text style={styles.titleText}>{title}</Text>
                <Pressable style={styles.expandContainer} onPress={expandCurrent}>
                    <Text style={styles.expandText} >{texts.viewAll}</Text>
                </Pressable>
            </View>
            <Animated.View style={[style, { height: heightAnim }]}>
                {children}
            </Animated.View>
        </>
    );
}

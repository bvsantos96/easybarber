import { Text, StyleProp, ViewStyle, Animated, Easing, View } from "react-native";
import { getStyles } from "../styles/ExpandableView";
import Pressable from "./Pressable";
import { useEffect, useRef } from "react";
import React from "react";

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
    const animatedHeight = useRef(new Animated.Value(expanded ? maxHeight : minHeight)).current;

    useEffect(() => {
        Animated.timing(
            animatedHeight,
            {
                toValue: expanded ? maxHeight : minHeight,
                duration: 300,
                easing: Easing.ease,
                useNativeDriver: false,
            }
        ).start();

    }, [expanded]);

    return (
        <>
            <View style={styles.titleContainer}>
                <Text style={styles.titleText}>{title}</Text>
                <Pressable style={styles.expandContainer} onPress={expandCurrent}>
                    <Text style={styles.expandText} >{texts.viewAll}</Text>
                </Pressable>
            </View>
            <Animated.View style={[style, { height: animatedHeight, overflow: "hidden" }]}>
                <Animated.View style={{
                    height: animatedHeight.interpolate({
                        inputRange: [0, maxHeight],
                        outputRange: ['0%', '100%']
                    })
                }}>
                    {children}
                </Animated.View>
            </Animated.View>
        </>
    );
}

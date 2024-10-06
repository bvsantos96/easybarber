import { Text, StyleProp, ViewStyle, View } from "react-native";
import { getStyles } from "../styles/ExpandableView";
import Pressable from "./Pressable";
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

    return (
        <>
            <View style={styles.titleContainer}>
                <Text style={styles.titleText}>{title}</Text>
                <Pressable style={styles.expandContainer} onPress={onExpand}>
                    <Text style={styles.expandText} >{texts.viewAll}</Text>
                </Pressable>
            </View>
            <View style={[style, { height: (!expanded) ? minHeight : maxHeight }]}>
                {children}
            </View>
        </>
    );
}

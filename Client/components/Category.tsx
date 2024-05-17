import { Animated, View, Text, Easing } from "react-native";
import Pressable from "./Pressable";
import Divider from "./Divider";
import { getStyles as topBarGetStyles } from "../styles/TopBar";
import { getStyles as homeGetStyles } from "../styles/Home";
import { useEffect, useState } from "react";

export default function Category({ expanded = true, icon = <></>, title = "" }) {
    const topBarStyles = topBarGetStyles();
    const homeStyles = homeGetStyles();
    const [heightAnim] = useState(new Animated.Value(expanded ? homeStyles.categoryContainer.height : 0));

    useEffect(() => {
        Animated.timing(
            heightAnim,
            {
                toValue: expanded ? homeStyles.categoryContainer.height : 0,
                duration: 300,
                easing: Easing.ease,
                useNativeDriver: false,
            }
        ).start();

    }, [expanded, heightAnim]);


    return (
        <Animated.View style={{ height: heightAnim }}>
            <Pressable onPress={() => { }}>
                <View style={topBarStyles.categoryIconContainer}>
                    {icon}
                </View>
                <Divider size={8} />
                <Text style={topBarStyles.categoryText}>{title}</Text>
            </Pressable>
        </Animated.View>
    );
}

import React from "react";
import { Animated, View, Text, Easing, ViewStyle } from "react-native";
import Pressable from "./Pressable";
import Divider from "./Divider";
import { getStyles as topBarGetStyles } from "../styles/TopBar";
import { getStyles as homeGetStyles } from "../styles/Home";
import { useEffect, useState } from "react";
import { IFilterRequest } from "../declarations";
import { useTheme } from "../styles/ThemeContext";

interface CategoryProps {
    id: number;
    expanded?: boolean;
    icon?: React.ReactNode;
    title?: string;
    select?: (filter: IFilterRequest) => void;
    selectedCategory: number;
    style?: ViewStyle;
    padding?: number;
}

export default function Category({
    id,
    expanded = true,
    icon = <></>,
    title = "",
    select = (_filter: IFilterRequest) => { },
    selectedCategory = -1,
    style = {},
    padding = 17.5
}: CategoryProps) {
    const topBarStyles = topBarGetStyles();
    const homeStyles = homeGetStyles();
    const [heightAnim] = useState(new Animated.Value(expanded ? homeStyles.categoryContainer.height : 0));
    const theme = useTheme();

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
        <Animated.View style={[{ height: heightAnim }, style]}>
            <Pressable onPress={() => {
                const filter: IFilterRequest = {
                    serviceType: id == selectedCategory ? null : `${id}`
                }
                select(filter);
            }}>
                <View style={[topBarStyles.categoryIconContainer, { padding: padding * theme.dimensions.absoluteWidth }, id != selectedCategory && selectedCategory != -1 ? topBarStyles.categorySelected : null]}>
                    {icon}
                </View>
                <Divider size={8} />
                <Text style={topBarStyles.categoryText}>{title}</Text>
            </Pressable>
        </Animated.View>
    );
}

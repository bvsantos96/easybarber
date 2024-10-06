import React from "react";
import { View, Text, ViewStyle } from "react-native";
import Pressable from "./Pressable";
import Divider from "./Divider";
import { getStyles as topBarGetStyles } from "../styles/TopBar";
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
    icon = <></>,
    title = "",
    select = (_filter: IFilterRequest) => { },
    selectedCategory = -1,
    style = {},
    padding = 17.5
}: CategoryProps) {
    const topBarStyles = topBarGetStyles();
    const theme = useTheme();

    return (
        <View style={[style]}>
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
        </View>
    );
}

import React from "react";
import { Text, View } from "react-native";

import StarIcon from '@assets/icons/star.svg';
import { getStyles } from "../styles/List";
import { useTheme } from "@styles/ThemeContext";

export const ImageRating = ({ rating, nvotes, right }: { rating: string, nvotes: number, right?: boolean }) => {
    const styles = getStyles();
    const theme = useTheme();
    return (
        <View style={[!right ? styles.ratingContainer : styles.ratingContainerRight, theme.shadow]} >
            <View style={[styles.ratingIconContainer, styles.center, theme.shadow]}>
                <StarIcon style={styles.ratingIcon} width="75%" height="75%" />
            </View>
            <Text style={[styles.ratingText, theme.shadow]}>{rating}</Text>
            <Text style={[styles.numberOfVotes, theme.shadow]}>({nvotes})</Text>
        </View >
    );
}

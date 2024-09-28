import React from "react";
import { Text, View } from "react-native";

import StarIcon from '@assets/icons/star.svg';

import { getStyles } from "../styles/List";
import { getStyles as mainGetStyles } from "../styles/Main"

export const ImageRating = ({ rating, nvotes, right }: { rating: string, nvotes: number, right?: boolean }) => {
    const styles = getStyles();
    const mainStyles = mainGetStyles();
    return (
        <View style={[!right ? styles.ratingContainer : styles.ratingContainerRight, mainStyles.shadow]} >
            <View style={[styles.ratingIconContainer, mainStyles.alignCenter, mainStyles.justifyCenter, mainStyles.shadow]}>
                <StarIcon style={styles.ratingIcon} width="75%" height="75%" />
            </View>
            <Text style={[styles.ratingText, mainStyles.shadow]}>{rating}</Text>
            <Text style={[styles.numberOfVotes, mainStyles.shadow]}>({nvotes})</Text>
        </View >
    );
}

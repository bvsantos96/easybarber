import React from "react";
import { View, Text, Image } from "react-native";

import StarIcon from '@assets/icons/star.svg';

import { getStyles } from "../styles/List";
import { getStyles as mainGetStyles } from "../styles/Main"
import { defaultBarberImage } from "../utils/Constants";

export const ImageWithRating = ({ rating, nvotes, data, right }: { rating: string, nvotes: number, data?: string, right?: boolean }) => {
    const styles = getStyles();
    const mainStyles = mainGetStyles();
    return (
        <>
            <Image source={{ uri: data ?? defaultBarberImage }} style={styles.imageStyle} />
            <View style={[!right ? styles.ratingContainer : styles.ratingContainerRight, mainStyles.shadow]} >
                <View style={[styles.ratingIconContainer, mainStyles.alignCenter, mainStyles.justifyCenter, mainStyles.shadow]}>
                    <StarIcon style={styles.ratingIcon} width="75%" height="75%" />
                </View>
                <Text style={[styles.ratingText, mainStyles.shadow]}>{rating}</Text>
                <Text style={[styles.numberOfVotes, mainStyles.shadow]}>({nvotes})</Text>
            </View >
        </>
    );
}

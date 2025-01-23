import React from "react";
import { Image } from "expo-image";

import { getStyles } from "../styles/List";
import { defaultBarberImage } from "../utils/Constants";
import { ImageRating } from "./ImageRating";

export const ImageWithRating = ({ rating, nvotes, data, right }: { rating: string, nvotes: number, data?: string, right?: boolean }) => {
    const styles = getStyles();
    return (
        <>
            <Image
                cachePolicy="memory"
                source={{ uri: data ?? defaultBarberImage }} style={styles.imageStyle} />
            <ImageRating rating={rating} nvotes={nvotes} right={right} />
        </>
    );
}

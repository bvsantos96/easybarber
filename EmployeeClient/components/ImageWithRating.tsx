import React from "react";
import { Image } from "expo-image";

import { getStyles } from "../styles/List";
import { defaultBarberImage } from "../utils/Constants";
import { ImageRating } from "./ImageRating";
import { StarImage } from "./StarImage";

export const ImageWithRating = ({ rating, nvotes, data, right, favorite }: { rating?: string, nvotes?: number, data?: string, right?: boolean, favorite?: boolean }) => {
    const styles = getStyles();
    return (
        <>
            <Image
                cachePolicy="memory"
                source={{ uri: data ?? defaultBarberImage }} style={styles.imageStyle} />
            {(rating && nvotes) ? (
                <ImageRating rating={rating} nvotes={nvotes} right={right} />
            ) : (
                favorite && (
                    <StarImage right={right} />
                )
            )
            }
        </>
    );
}

import { View, Text } from "react-native";
import { getStyles } from "../styles/List";
import Pressable from "./Pressable";
import LocationIcon from '@assets/icons/location.svg';

import React from "react";
import { defaultBarberImage } from "../utils/Constants";
import { ImageWithRating } from "./ImageWithRating";
import { useQuery } from "@tanstack/react-query";
import { isFavorite } from "utils/ApiRequest";

export default function ListItem({ establishment, onPress }: { establishment: EstablishmentInfo, onPress: (employeeId: number) => void }) {
    const styles = getStyles();
    const texts = require("../langs/en.json");

    useQuery({
        queryKey: [`/establishment/${establishment?.id}/favorite`, establishment?.id],
        queryFn: async () => await isFavorite(establishment.id),
        enabled: true,
        networkMode: 'offlineFirst',
        staleTime: 60000,
    });

    return (
        <View style={styles.itemContainer}>
            <Pressable onPress={() => onPress(establishment.id)} style={[styles.container, styles.shadow]}>
                <View style={styles.imageContainer} >
                    <ImageWithRating rating={establishment.sumVotes == 0 ? "0.0" : (establishment.sumVotes / establishment.nvotes).toFixed(1)} nvotes={establishment.nvotes} data={establishment.images ? establishment.images[0]?.data : defaultBarberImage} />
                </View>
                <View style={styles.textContainer} >
                    <Text numberOfLines={2} style={styles.title}>{establishment.name}</Text>
                    <View style={styles.locationContainer}>
                        <LocationIcon width={styles.locationIcon.width} height={styles.locationIcon.height} style={styles.locationIcon} />
                        <Text style={styles.locationText}>{establishment.distance.toFixed(1)}{texts.kmAway}</Text>
                    </View>
                    <Text style={styles.description}>{establishment.description}</Text>
                </View>
            </Pressable>
        </View>
    );
}

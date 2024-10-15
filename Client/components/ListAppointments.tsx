import React from "react";
import { View, Text } from "react-native";
import { Image } from "expo-image";


import { getStyles } from "../styles/Appointments";

import ClockIcon from "@assets/icons/clock.svg";
import Pressable from "../components/Pressable";

import { getDateAsString, getTimeAsString } from "../utils/Utils";

export default function AppointmentItem({ appointment }: { appointment: AppointmentInfo }) {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const date = new Date(`${appointment.date}T${appointment.time}`);
    const dateString = getDateAsString(date);
    const timeString = getTimeAsString(date);

    return (
        <Pressable onPress={() => { alert(`Open barber ${appointment.establishmentName}`); }} style={[styles.itemContainer, styles.shadow]}>
            <View style={styles.imageContainer}>
                <Image
                    cachePolicy="memory"
                    source={{ uri: appointment.photo }} style={styles.imageStyle} />
            </View>
            <View style={styles.textContainer}>
                <Text numberOfLines={1} style={styles.title}>{appointment.establishmentName}</Text>
                <View style={styles.subTitleContainer}>
                    <Text numberOfLines={1} style={styles.locationText}>{appointment.entityName}</Text>
                </View>
                <View style={styles.infoContainer}>
                    <View style={styles.locationContainer}>
                        <ClockIcon width={styles.locationIcon.width} height={styles.locationIcon.height} style={styles.locationIcon} />
                    </View>
                    <Text style={styles.locationText}>{dateString} {texts.at} {timeString}</Text>
                </View>
            </View>
        </Pressable >
    );
}

import { View, Image, Text } from "react-native";
import { getStyles } from "../styles/Appointments";
import { Appointment } from "../utils/ApiRequest";
import Pressable from "../components/Pressable";

import ClockIcon from "../assets/icons/clock.svg";

export default function ListAppointments({ appointment }: { appointment: Appointment }) {
    const styles = getStyles();
    return (
        <Pressable onPress={() => { alert(`Open barber ${appointment.name}`); }} style={[styles.itemContainer, styles.shadow]}>
            <View style={styles.imageContainer}>
                <Image source={{ uri: appointment.photo }} style={styles.imageStyle} />
            </View>
            <View style={styles.textContainer}>
                <Text style={styles.title}>{appointment.name}</Text>
                <View style={styles.infoContainer}>
                    <View style={styles.locationContainer}>
                        <ClockIcon width={styles.locationIcon.width} height={styles.locationIcon.height} style={styles.locationIcon} />
                    </View>
                    <Text style={styles.locationText}>{appointment.from} to {appointment.to}</Text>
                </View>
            </View>
        </Pressable>
    );
}

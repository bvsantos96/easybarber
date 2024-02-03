import { View, Image, Text } from "react-native";
import { styles } from "../styles/List";
import { Appointment } from "../utils/ApiRequest";
import Pressable from "../components/Pressable";

import ClockIcon from "../assets/icons/clock.svg";
import { absoluteWidth } from "../styles/Main";

export default function ListAppointments({ appointment }: { appointment: Appointment }) {
    return (
        <Pressable onPress={()=>{alert(`Open barber ${appointment.name}`);}} style={[styles.container]}>
            <View style={{paddingRight: 15}}>
                <Image source={{ uri: appointment.photo }} style={styles.imageStyle} />
            </View>
            <View style={styles.textContainer}>
                <Text style={styles.title}>{appointment.name}</Text>
                <View style={styles.locationContainer}>
                    <ClockIcon width={13 * absoluteWidth} height={13 * absoluteWidth} style={styles.locationIcon} />
                    <Text style={styles.locationText}>{appointment.from} to {appointment.to}</Text>
                </View>
            </View>
        </Pressable>
    );
}

import { View, Image, Text } from "react-native";
import { styles } from "../styles/List";
import { Appointment } from "../utils/ApiRequest";
import Pressable from "../components/Pressable";

export default function ListAppointments({ appointment }: { appointment: Appointment }) {
    const texts = require("../langs/en.json");
    return (
        <Pressable onPress={()=>{alert(`Open barber ${appointment.name}`);}} style={[styles.container]}>
            <View style={{paddingRight: 15}}>
                <Image source={{ uri: appointment.photo }} style={styles.imageStyle} />
            </View>
            <View style={styles.textContainer}>
                <Text style={styles.title}>{appointment.name}</Text>
                <View style={styles.locationContainer}>
                    <Image style={styles.locationIcon} source={require('@assets/icons/clock.png')} />
                    <Text style={styles.locationText}>{appointment.from} to {appointment.to}</Text>
                </View>
            </View>
        </Pressable>
    );
}

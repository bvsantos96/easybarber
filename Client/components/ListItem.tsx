import { View, Image, Text } from "react-native";
import { styles } from "../styles/List";
import {styles as mainStyles} from "../styles/Main";
import { BarberInfo } from "../utils/ApiRequest";

export default function ListItem({ barber }: { barber: BarberInfo }) {
    const texts = require("../langs/en.json");
    return (
        <View style={[styles.container]}>
            <View>
                <View style={styles.ratingContainer}>
                    <Image  style={styles.ratingIcon} source={require('@assets/icons/star.png')} />
                    <View style={[mainStyles.row, mainStyles.alignCenter, mainStyles.justifyCenter]}>
                        <Text style={styles.ratingText}>{barber.rating}</Text>
                        <Text style={styles.numberOfVotes}>({barber.nVotes})</Text>
                    </View>
                </View>
                <Image source={{ uri: barber.photo }} style={styles.imageStyle} />
            </View>
            <View style={styles.textContainer}>
                <Text style={styles.title}>{barber.name}</Text>
                <View style={styles.locationContainer}>
                    <Image style={styles.locationIcon} source={require('@assets/icons/location.png')} />
                    <Text style={styles.locationText}>{barber.distance}{texts.kmAway}</Text>
                </View>
                <Text style={styles.description}>{barber.description}</Text>
            </View>
        </View>
    );
}

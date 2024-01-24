import { View, Image, Text } from "react-native";
import { styles } from "../styles/List";
import { styles as mainStyles } from "../styles/Main";
import { BarberInfo } from "../utils/ApiRequest";
import Pressable from "../components/Pressable";

export default function ListItem({ barber }: { barber: BarberInfo }) {
    const texts = require("../langs/en.json");
    return (
        <Pressable onPress={()=>{alert(`Open barber ${barber.name}`);}} style={[styles.container]}>
            <Image source={{ uri: barber.photo }} style={styles.imageStyle} />
            <View  style={styles.ratingContainer} >
                <View style={[styles.ratingIconContainer, mainStyles.alignCenter, mainStyles.justifyCenter]}>
                    <Image style={styles.ratingIcon} source={require('@assets/icons/star.png')} />
                </View>
                <Text style={styles.ratingText}>{barber.rating.toFixed(1)}</Text>
                <Text style={styles.numberOfVotes}>({barber.nVotes})</Text>
            </View>
            <View style={styles.textContainer} >
                <Text style={styles.title}>{barber.name}</Text>
                <View style={styles.locationContainer}>
                    <Image style={styles.locationIcon} source={require('@assets/icons/location.png')} />
                    <Text style={styles.locationText}>{barber.distance.toFixed(1)}{texts.kmAway}</Text>
                </View>
                <Text style={styles.description}>{barber.description}</Text>
            </View>
        </Pressable>
    );
}

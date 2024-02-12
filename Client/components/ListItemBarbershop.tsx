import { View, Image, Text } from "react-native";
import { getStyles } from "../styles/List";
import { getStyles as mainGetStyles } from "../styles/Main"
import { BarberInfo } from "../utils/ApiRequest";
import Pressable from "../components/Pressable";

import StarIcon from '@assets/icons/star.svg';
import LocationIcon from '@assets/icons/location.svg';

export default function ListItem({ barber }: { barber: BarberInfo }) {
    const styles = getStyles();
    const mainStyles = mainGetStyles();
    const texts = require("../langs/en.json");
    return (
        <Pressable onPress={() => { alert(`Open barber ${barber.name}`); }} style={[styles.container]}>
            <Image source={{ uri: barber.photo }} style={styles.imageStyle} />
            <View style={[styles.ratingContainer, mainStyles.shadow]} >
                <View style={[styles.ratingIconContainer, mainStyles.alignCenter, mainStyles.justifyCenter, mainStyles.shadow]}>
                    <StarIcon style={styles.ratingIcon} width="75%" height="75%" />
                </View>
                <Text style={[styles.ratingText, mainStyles.shadow]}>{barber.rating.toFixed(1)}</Text>
                <Text style={[styles.numberOfVotes, mainStyles.shadow]}>({barber.nVotes})</Text>
            </View>
            <View style={styles.textContainer} >
                <Text style={styles.title}>{barber.name}</Text>
                <View style={styles.locationContainer}>
                    <LocationIcon width={styles.locationIcon.width} height={styles.locationIcon.height} style={styles.locationIcon} />
                    <Text style={styles.locationText}>{barber.distance.toFixed(1)}{texts.kmAway}</Text>
                </View>
                <Text style={styles.description}>{barber.description}</Text>
            </View>
        </Pressable>
    );
}

import LocationIcon from '@assets/icons/location.svg';
import { Text, View } from "react-native";
import { getStyles } from "../styles/List";
import { defaultBarberImage } from "../utils/Constants";
import Divider from "./Divider";
import { ImageWithRating } from "./ImageWithRating";
import Pressable from "./Pressable";

export default function EstablishmentItem({ establishment, onPress }: { establishment: EstablishmentInfo, onPress: (employeeId: number) => void }) {
    const styles = getStyles();
    const texts = require("../langs/en.json");

    return (
        <>
            <View style={styles.itemContainer}>
                <Pressable onPress={() => onPress && onPress(establishment.id)} style={[styles.container, styles.shadow]}>
                    <View style={styles.imageContainer}>
                        <ImageWithRating
                            favorite={establishment.admin}
                            data={establishment.images ? establishment.images[0]?.data : defaultBarberImage}
                        />
                    </View>
                    <View style={styles.textContainer} >
                        <Text numberOfLines={2} style={styles.title}>{establishment.name}</Text>
                        {establishment.distance && (
                            <View style={styles.locationContainer}>
                                <LocationIcon width={styles.locationIcon.width} height={styles.locationIcon.height} style={styles.locationIcon} />
                                <Text style={styles.locationText}>{establishment.distance.toFixed(1)}{texts.kmAway}</Text>
                            </View>
                        )}
                        <Text style={styles.description}>{establishment.description}</Text>
                    </View>
                </Pressable>
            </View>
            <Divider size={10} />
        </>
    );
}

import Entypo from '@expo/vector-icons/Entypo';
import { Text, TouchableOpacity, View } from "react-native";
import { getStyles } from "../styles/Settings";
import Divider from "./Divider";

interface Props {
    onPress: () => void;
    text: string;
    icon: React.ReactNode
}

export default function SettingItem({ onPress, text, icon }: Props) {
    const styles = getStyles();
    return (
        <TouchableOpacity style={styles.listItemContainer} onPress={onPress}>
            <View style={styles.iconContainer}>
                {icon}
            </View>
            <Divider size={20} horizontal />
            <Text style={styles.listItemText} >{text}</Text>
            <View style={styles.arrow}>
                <Entypo name="chevron-thin-right" size={styles.iconContainer.width / 3} color={styles.listItemText.color} />
            </View>
        </TouchableOpacity>
    );
}

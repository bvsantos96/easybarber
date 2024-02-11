import { Image } from "react-native";
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/TopBar';
import { buttonTextColor } from '../styles/Main';
import { AntDesign } from '@expo/vector-icons';

export default function ProfileImage({ uri = "" }: { uri?: string }) {
    const styles = getStyles();
    return (
        <Pressable onPress={() => { alert("Goto profile") }} style={styles.profileImageContainer}>
            {uri && uri.length > 0 ?
                (<Image source={{ uri: uri }} style={[styles.profileImage]} />)
                :
                (<AntDesign name="user" size={styles.profileImage.width} color={buttonTextColor} />)}
        </Pressable>
    );
}

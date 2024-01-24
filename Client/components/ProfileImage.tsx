import { Image } from "react-native";
import Pressable from '../components/Pressable';
import { profileIconSize, styles } from '../styles/TopBar';
import { buttonTextColor } from '../styles/Main';
import { AntDesign } from '@expo/vector-icons';

export default function ProfileImage({ uri = "" }: { uri?: string }) {
    return (
        <Pressable onPress={() => { alert("Goto profile") }} style={styles.profileImageContainer}>
            {uri && uri.length > 0 ?
                (<Image source={{ uri: uri }} style={[styles.profileImage]} />)
                :
                (<AntDesign name="user" size={profileIconSize} color={buttonTextColor} />)}
        </Pressable>
    );
}

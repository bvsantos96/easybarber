import { Pressable, Image } from "react-native";
import { profileIconSize, styles } from '../styles/TopBar';
import { buttonTextColor, styles as mainStyles } from '../styles/Main';
import { AntDesign } from '@expo/vector-icons';

export default function ProfileImage({ uri = "" }: { uri?: string }) {
    return (
        <Pressable onPress={() => { alert("Goto profile") }} style={[styles.profileImageContainer, mainStyles.lMargin10]}>
            {uri && uri.length > 0 ?
                (<Image source={{ uri: uri }} style={[styles.profileImage, mainStyles.hMargin5]} />)
                :
                (<AntDesign name="user" size={profileIconSize} color={buttonTextColor} />)}
        </Pressable>
    );
}

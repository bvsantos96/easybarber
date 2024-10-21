import { Image } from "expo-image";
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/TopBar';
import { AntDesign } from '@expo/vector-icons';
import { useTheme } from "../styles/ThemeContext";
import texts from "@lang/en.json";

interface Props extends PropNavigation {
    uri?: string;
}

export default function ProfileImage({ navigation, uri = "" }: Props) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable onPress={() => {
            navigation.navigate(texts.settings.title);
        }} style={styles.profileImageContainer}>
            {uri && uri.length > 0 ?
                (<Image
                    cachePolicy="memory-disk"
                    source={{ uri: uri }} style={[styles.profileImage]} />)
                :
                (<AntDesign name="user" size={styles.profileImage.width} color={theme.colors.button.alt} />)}
        </Pressable>
    );
}

import { Image } from "expo-image";
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/TopBar';
import { AntDesign } from '@expo/vector-icons';
import { useTheme } from "../styles/ThemeContext";
import { TOKEN_STORAGE_KEY } from 'utils/Constants';
import { removeData } from 'utils/ApiRequest';
import useAlertStore from "storage/stores/AlertStore";
import { AlertType } from "./Alert";
import { NavigationProp } from "@react-navigation/native";

interface Props extends PropNavigation {
    uri?: string;
}

export default function ProfileImage({ navigation, uri = "" }: Props) {
    const styles = getStyles();
    const theme = useTheme();
    const { alert } = useAlertStore();

    const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
        navigation.reset({
            index: 0,
            routes: [{ name: route }],
        });
    }

    return (
        <Pressable onPress={() => {
            alert({
                type: AlertType.Info,
                message: "Logged out successfully.",
                onPress: () => {
                    resetNavigation(navigation, 'OnBoarding');
                }
            });
            removeData(TOKEN_STORAGE_KEY);
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

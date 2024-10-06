import React from 'react';

import { Image } from "expo-image";
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/TopBar';
import { AntDesign } from '@expo/vector-icons';
import { useTheme } from "../styles/ThemeContext";
import { TOKEN_STORAGE_KEY } from 'utils/Constants';
import { removeData } from 'utils/ApiRequest';

export default function ProfileImage({ uri = "" }: { uri?: string }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable onPress={() => { removeData(TOKEN_STORAGE_KEY); }} style={styles.profileImageContainer}>
            {uri && uri.length > 0 ?
                (<Image
                    cachePolicy="memory-disk"
                    source={{ uri: uri }} style={[styles.profileImage]} />)
                :
                (<AntDesign name="user" size={styles.profileImage.width} color={theme.colors.button.alt} />)}
        </Pressable>
    );
}

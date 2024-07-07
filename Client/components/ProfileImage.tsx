import React from 'react';

import { Image } from "react-native";
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/TopBar';
import { AntDesign } from '@expo/vector-icons';
import { useTheme } from "../styles/ThemeContext";
import { Alert } from './Alert';
import { ALERT_TYPE } from 'react-native-alert-notification';

export default function ProfileImage({ uri = "" }: { uri?: string }) {
    const styles = getStyles();
    const theme = useTheme();
    return (
        <Pressable onPress={() => { Alert({ type: ALERT_TYPE.DANGER, title: "Implementation missing", message: "No profile layout defined" }) }} style={styles.profileImageContainer}>
            {uri && uri.length > 0 ?
                (<Image source={{ uri: uri }} style={[styles.profileImage]} />)
                :
                (<AntDesign name="user" size={styles.profileImage.width} color={theme.colors.button.alt} />)}
        </Pressable>
    );
}

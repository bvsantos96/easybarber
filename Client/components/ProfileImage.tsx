import React from 'react';

import { Image } from "react-native";
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/TopBar';
import { AntDesign } from '@expo/vector-icons';
import { useTheme } from "../styles/ThemeContext";

export default function ProfileImage({ uri = "" }: { uri?: string }) {
    const styles = getStyles();
    const theme = useTheme();
    return (
        <Pressable onPress={() => { alert("Goto profile") }} style={styles.profileImageContainer}>
            {uri && uri.length > 0 ?
                (<Image source={{ uri: uri }} style={[styles.profileImage]} />)
                :
                (<AntDesign name="user" size={styles.profileImage.width} color={theme.colors.button.alt} />)}
        </Pressable>
    );
}

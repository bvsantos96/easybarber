import Pressable from "@components/Pressable";
import React from "react";
import { View, Text } from "react-native";
import { NavigationProp } from "@react-navigation/native";

import texts from "@lang/en.json";
import { getStyles } from "@styles/Settings";
import LogOutIcon from "@assets/icons/logout.svg";
import { removeData } from "utils/ApiRequest";
import { TOKEN_STORAGE_KEY } from "utils/Constants";
import { Routes } from "@navigation/Router";

export default function Settings({ navigation }: PropNavigation) {
    const styles = getStyles();

    const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
        navigation.reset({
            index: 0,
            routes: [{ name: route }],
        });
    }

    return (
        <View style={styles.container}>
            <Pressable style={styles.logOutContainer} onPress={() => {
                removeData(TOKEN_STORAGE_KEY);
                resetNavigation(navigation, Routes.Onboarding);
            }}
            >
                <LogOutIcon width={styles.logOutIcon.width} height={styles.logOutIcon.height} color={styles.logOutIcon.color} size={styles.logOutIcon.width} />
                <Text style={styles.logOutText}>{texts.logout}</Text>
            </Pressable>
        </View >
    );
}

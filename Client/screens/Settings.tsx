import Pressable from "@components/Pressable";
import React, { useEffect, useState } from "react";
import { View, Text } from "react-native";
import { NavigationProp } from "@react-navigation/native";

import texts from "@lang/en.json";
import { getStyles } from "@styles/Settings";
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { getToken, removeData } from "utils/ApiRequest";
import { TOKEN_STORAGE_KEY } from "utils/Constants";
import { Routes } from "@navigation/Router";

export default function Settings({ navigation }: PropNavigation) {
    const styles = getStyles();
    const [authenticated, setAuthenticated] = useState(false);

    useEffect(() => {
        const checkAuthentication = async () => {
            setAuthenticated(await getToken() !== null);
        };
        checkAuthentication();
    }, []);
    
    const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
        navigation.reset({
            index: 0,
            routes: [{ name: route }],
        });
    }

    if (!authenticated) {
        return (
            <View style={styles.container}>
            <Pressable style={styles.logOutContainer} onPress={() => {
                resetNavigation(navigation, Routes.Sign);
            }}
            >
                <MaterialIcons name="login" width={styles.logOutIcon.width} height={styles.logOutIcon.height} color={styles.logOutIcon.color} size={styles.logOutIcon.width} />
                <Text style={styles.logOutText}>{texts.login.signIn}</Text>
            </Pressable>
        </View >
        );
    }

    return (
        <View style={styles.container}>
            <Pressable style={styles.logOutContainer} onPress={() => {
                removeData(TOKEN_STORAGE_KEY);
                resetNavigation(navigation, Routes.Onboarding);
            }}
            >
                <MaterialIcons name="logout" width={styles.logOutIcon.width} height={styles.logOutIcon.height} color={styles.logOutIcon.color} size={styles.logOutIcon.width} />
                <Text style={styles.logOutText}>{texts.logout}</Text>
            </Pressable>
        </View >
    );
}

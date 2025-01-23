import Pressable from "@components/Pressable";
import React, { useEffect, useState } from "react";
import { View, Text } from "react-native";

import SecurityIcon from "@assets/icons/security.svg";
import texts from "@lang/en.json";
import { getStyles } from "@styles/Settings";
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { getMobileCodeResetPwd, getMobileInformation, getToken } from "utils/ApiRequest";
import { Routes } from "@navigation/Router";
import { resetNavigation } from "utils/Utils";
import useAuthStore from "storage/stores/AuthStore";
import SettingItem from "@components/SettingItem";
import { MobileConfirmationFunctions } from "enums";
import useMobileConfirmationStore from "storage/stores/MobileConfirmationStore";

export default function Settings({ navigation }: PropNavigation) {
    const { toggleDoLogout } = useAuthStore();
    const styles = getStyles();
    const [authenticated, setAuthenticated] = useState(false);
    const { setBlockTime, resetBlockTime } = useMobileConfirmationStore();

    useEffect(() => {
        const checkAuthentication = async () => {
            setAuthenticated(await getToken() !== null);
        };
        checkAuthentication();
    }, []);


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
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <SettingItem
                text={texts.settings.resetPwd}
                icon={< SecurityIcon />}
                onPress={async () => {
                    const phoneInfo = await getMobileInformation();
                    if (!phoneInfo) {
                        return;
                    }

                    resetBlockTime();
                    getMobileCodeResetPwd(phoneInfo.countryCode, phoneInfo.phone).then((blockUntil) => {
                        setBlockTime(blockUntil);
                    });

                    navigation.navigate(Routes.MobileConfirmation, {
                        phoneNr: phoneInfo.phone,
                        countryCode: phoneInfo.countryCode,
                        nextScreen: Routes.ResetPwd,
                        resetNavigationBoolean: false,
                        resendFunction: MobileConfirmationFunctions.RESET_PASSWORD,
                    });
                }}
            />
            <Pressable style={styles.logOutContainer} onPress={() => {
                toggleDoLogout();
            }}
            >
                <MaterialIcons name="logout" width={styles.logOutIcon.width} height={styles.logOutIcon.height} color={styles.logOutIcon.color} size={styles.logOutIcon.width} />
                <Text style={styles.logOutText}>{texts.logout}</Text>
            </Pressable>
        </View >
    );
}

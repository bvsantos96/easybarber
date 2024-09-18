import { Text, View, StyleSheet } from "react-native";

import { useTheme } from "../styles/ThemeContext";
import React from "react";
import Button from "../components/Button";
import LogoSmall from "@assets/images/logoRounded.svg";
import useLocationStore from "../storage/stores/LocationStore";
import * as Location from 'expo-location';

export default function LocationRequest() {
    const {
        setRequestingLocationPermission,
        setHasLocationPermission
    } = useLocationStore();
    const texts = require("@lang/en.json");
    const styles = getStyles();

    const requestLocationPermission = async () => {
        const { status }: Location.PermissionResponse = await Location.requestForegroundPermissionsAsync();
        setRequestingLocationPermission(false);
        if (status === "granted") {
            setHasLocationPermission(true);
            return true;
        }
        setHasLocationPermission(false);
    }

    return (
        <View style={styles.container}>
            <View style={styles.icon}>
                <LogoSmall width={styles.logo.width} height={styles.logo.height} />
            </View>
            <View style={styles.containerTitle}>
                <View style={styles.row} >
                    <Text style={styles.textBold}>“{texts.appName}”</Text>
                    <Text style={styles.textTitle}>{texts.location.title1}</Text>
                </View>
                <Text style={styles.textTitle}>{texts.location.title2}</Text>
            </View>
            <View style={styles.containerSubTitle} >
                <Text style={styles.textSubtitle}>{texts.location.subTitle}</Text>
            </View>
            <View style={styles.buttonContainer}>
                <Button borderRadius={10} backgroundColor={styles.button.backgroundColor} buttonTextColor={styles.button.color} title={texts.allow} onPress={requestLocationPermission} />
            </View>
        </View>
    );
}

const getStyles = () => {
    const theme = useTheme();
    return StyleSheet.create({
        container: {
            flex: 1,
            backgroundColor: theme.colors.button.main,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            justifyContent: 'center',
            alignItems: 'center',
        },
        row: {
            flexDirection: 'row',
        },
        icon: {
            ...theme.strongShadow as any,
            width: 33 * theme.dimensions.absoluteWidth,
            height: 33 * theme.dimensions.absoluteWidth,
            position: 'absolute',
            top: 253.96 * theme.dimensions.absoluteHeight,
            left: 138.46 * theme.dimensions.absoluteWidth,
        },
        containerTitle: {
            position: 'absolute',
            top: 422 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            alignItems: 'center',
        },
        textTitle: {
            fontSize: 20 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '600',
            lineHeight: 30 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.17,
            alignSelf: 'center',
            textAlign: 'center',
            color: theme.colors.text.alt,
        },
        textBold: {
            fontSize: 20 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '900',
            lineHeight: 30 * theme.dimensions.absoluteHeight,
            letterSpacing: 0.17,
            alignSelf: 'center',
            textAlign: 'center',
            color: theme.colors.text.alt,
        },
        containerSubTitle: {
            position: 'absolute',
            top: 508 * theme.dimensions.absoluteHeight,
            width: theme.dimensions.width,
            alignItems: 'center',
        },
        textSubtitle: {
            fontSize: 14 * theme.dimensions.absoluteWidth,
            fontFamily: 'Poppins',
            fontWeight: '400',
            lineHeight: 21 * theme.dimensions.absoluteHeight,
            color: theme.colors.text.alt,
            alignSelf: 'center',
            textAlign: 'center',
        },
        buttonContainer: {
            position: 'absolute',
            top: 686 * theme.dimensions.absoluteHeight,
        },
        button: {
            backgroundColor: theme.colors.button.alt,
            color: theme.colors.button.main,
        },
        logo: {
            height: 113 * theme.dimensions.absoluteHeight,
            width: 113 * theme.dimensions.absoluteWidth,
        },
    });
}

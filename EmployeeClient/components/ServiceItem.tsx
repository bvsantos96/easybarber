import React from 'react';
import { View, Text } from 'react-native';
import { getStyles } from '../styles/ProductItem';
import { Image } from 'expo-image';
import Pressable from './Pressable';
import { useTheme } from '@styles/ThemeContext';
import EditIcon from "@assets/icons/edit.svg";
import { buildCurrencyString, fromMinutesToTime } from 'utils/Utils';

export default function ServiceItem({ service, onPress }: { service: ServiceDetails, onPress?: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable style={[styles.container, theme.shadow]} onPress={onPress ? onPress : () => console.log("test")}>
            <Image source={{ uri: service.image }} style={styles.image} />
            <EditIcon style={styles.editIcon} />
            <View style={styles.textContainer} >
                <Text style={styles.brandText}>{service.name}</Text>
                <Text style={styles.nameText}>{service.description}</Text>
                <Text style={styles.priceText}>{`${fromMinutesToTime(service.duration)}${service.price ? ` - ${buildCurrencyString(service.price)}` : ""}`}</Text>
            </View>
        </Pressable>
    );
}

import React from 'react';
import { View, Text } from 'react-native';
import { getStyles } from '../styles/ProductItem';
import { Image } from 'expo-image';
import Pressable from './Pressable';
import { useTheme } from '@styles/ThemeContext';
import { buildCurrencyString, fromMinutesToTime } from 'utils/Utils';
import Category from './Category';

export default function ServiceItem({ service, onPress }: { service: ServiceDetails, onPress: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable style={[styles.container, theme.shadow]} onPress={onPress}>
            <Image source={{ uri: service.image }} style={styles.image} />
            <Category style={styles.editIcon} id={service.serviceType} selectedCategory={service.serviceType} size={styles.editIcon.width} />
            <View style={styles.textContainer} >
                <Text style={styles.brandText}>{service.name}</Text>
                <Text style={styles.nameText}>{service.description}</Text>
                <Text style={[styles.priceText, { fontSize: 11 }]}>{`${fromMinutesToTime(service.duration)}h ${service.price ? ` - ${buildCurrencyString(service.price)}` : ""}`}</Text>
            </View>
        </Pressable>
    );
}

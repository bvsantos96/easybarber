import React from 'react';
import { View, Text } from 'react-native';
import { getStyles } from '../styles/ServiceItem';
import { Image } from 'expo-image';
import Pressable from './Pressable';
import { useTheme } from '@styles/ThemeContext';
import { buildCurrencyString, fromMinutesToTime } from 'utils/Utils';
import Category from './Category';
import Divider from './Divider';

export default function ServiceItem({ service, onPress }: { service: ServiceDetails, onPress: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable style={[styles.container, theme.shadow]} onPress={onPress}>
            <Divider size={17.5} />
            <Image
                source={{ uri: service.image }}
                style={[styles.image]}
            />
            <Category style={styles.editIcon} id={service.serviceType} selectedCategory={service.serviceType} size={styles.editIcon.width} />
            <Divider size={8.75} />
            <View style={styles.textContainer} >
                <Text style={styles.brandText}>{service.name}</Text>
                <Text style={styles.nameText} numberOfLines={2}>{service.description}</Text>
                <Divider size={8.75} />
                <Text style={[styles.priceText, { fontSize: 11 }]}>{`${fromMinutesToTime(service.duration)}h ${service.price ? ` - ${buildCurrencyString(service.price)}` : ""}`}</Text>
            </View>
            <Divider size={17.5} />
        </Pressable>
    );
}

import { useTheme } from '@styles/ThemeContext';
import { Image } from 'expo-image';
import React from 'react';
import { Text, View } from 'react-native';
import { buildCurrencyString, fromMinutesToTime } from 'utils/Utils';
import { getStyles } from '../styles/ServiceItem';
import Category from './Category';
import Divider from './Divider';
import Pressable from './Pressable';

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
            <Category style={styles.editIcon} id={service.serviceType?.id || 0} selectedCategory={service.serviceType?.id || 0} size={styles.editIcon.width} />
            <Divider size={8.75} />
            <View style={styles.textContainer} >
                <Text style={styles.brandText}>{service.name}</Text>
                <Text style={styles.nameText} numberOfLines={2}>{service.description}</Text>
                <Divider size={8.75} />
                <Text style={[styles.priceText, { fontSize: 11 }]}>{`${fromMinutesToTime(+service.duration)}h ${service.price ? ` - ${buildCurrencyString(service.price)}` : ""}`}</Text>
            </View>
            <Divider size={17.5} />
        </Pressable>
    );
}

import React from 'react';
import { View } from 'react-native';
import { getStyles } from '../styles/SelectionItem';
import { ServiceInfo } from '../declarations';
import { Image } from 'expo-image';
import FontAwesome from '@expo/vector-icons/FontAwesome';
import { TouchableOpacity } from 'react-native-gesture-handler';

export default function SelectionItem({ service, children, selected = false, onPress }: { service: ServiceInfo, children: React.ReactNode, selected?: boolean, onPress: () => void }) {
    const styles = getStyles();

    return (
        <TouchableOpacity style={styles.container} onPress={onPress}>
            <Image source={{ uri: service.image.data }} style={styles.image} />
            <View>
                {children}
            </View>
            <View style={[styles.radioContainer, selected ? styles.radioContainerSelectod : {}]}>
                {selected &&
                    <FontAwesome name="circle" size={styles.radio.width} color={styles.radio.color} />
                }
            </View>
        </TouchableOpacity>
    );
}

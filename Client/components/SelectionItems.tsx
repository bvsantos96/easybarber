import React from 'react';
import { View } from 'react-native';
import { getStyles } from '../styles/SelectionItem';
import { Image } from 'expo-image';
import FontAwesome from '@expo/vector-icons/FontAwesome';
import Pressable from './Pressable';

export default function SelectionItem({ image, children, selected = false, onPress }: { image: string, children: React.ReactNode, selected?: boolean, onPress: () => void }) {
    const styles = getStyles();

    return (
        <Pressable style={styles.container} onPress={onPress}>
            <Image source={{ uri: image }} style={styles.image} />
            <View>
                {children}
            </View>
            <View style={[styles.radioContainer, selected ? styles.radioContainerSelectod : {}]}>
                {selected &&
                    <FontAwesome name="circle" size={styles.radio.width} color={styles.radio.color} />
                }
            </View>
        </Pressable>
    );
}

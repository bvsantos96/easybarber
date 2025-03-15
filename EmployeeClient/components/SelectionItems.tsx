import FontAwesome from '@expo/vector-icons/FontAwesome';
import { useTheme } from '@styles/ThemeContext';
import { Image } from 'expo-image';
import React from 'react';
import { View } from 'react-native';
import { getStyles } from '../styles/SelectionItem';
import Pressable from './Pressable';

export default function SelectionItem({ image, children, selected = false, onPress }: { image: string, children: React.ReactNode, selected?: boolean, onPress: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable style={[styles.container, theme.shadow]} onPress={onPress}>
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

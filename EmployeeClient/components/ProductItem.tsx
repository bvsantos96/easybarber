import React from 'react';
import { getStyles } from '../styles/ProductItem';
import { Image } from 'expo-image';
import Pressable from './Pressable';
import { useTheme } from '@styles/ThemeContext';

export default function SelectionItem({ product, onPress }: { product:ProductEntity, onPress?: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable style={[styles.container, theme.shadow]} onPress={onPress? onPress: ()=> console.log("test")}>
            <Image source={{ uri: product.image }} style={styles.image} />
        </Pressable>
    );
}

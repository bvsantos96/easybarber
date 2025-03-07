import EditIcon from "@assets/icons/edit.svg";
import { useTheme } from '@styles/ThemeContext';
import { Image } from 'expo-image';
import React from 'react';
import { Text, View } from 'react-native';
import { getStyles } from '../styles/ProductItem';
import Pressable from './Pressable';

export default function ProductItem({ product, onPress }: { product: ProductDetails, onPress?: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable style={[styles.container, theme.shadow]} onPress={onPress ? onPress : () => console.log("test")}>
            <Image source={{ uri: product.images[0].data }} style={styles.image} />
            <EditIcon style={styles.editIcon} />
            <View style={styles.textContainer} >
                {/*<Text style={styles.brandText}>{product.brand}</Text>*/}
                <Text style={styles.brandText}>{product.name}</Text>
                <Text style={styles.nameText}>{product.description}</Text>
                <Text style={styles.priceText}>{product.price}</Text>
            </View>
        </Pressable>
    );
}

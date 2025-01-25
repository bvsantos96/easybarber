import React from 'react';
import { View, Text} from 'react-native';
import { getStyles } from '../styles/ProductItem';
import { Image } from 'expo-image';
import Pressable from './Pressable';
import { useTheme } from '@styles/ThemeContext';
import EditIcon from "@assets/icons/edit.svg";

export default function SelectionItem({ product, onPress }: { product:ProductEntity, onPress?: () => void }) {
    const styles = getStyles();
    const theme = useTheme();

    return (
        <Pressable useGradient={false} style={[styles.container, theme.shadow]} onPress={onPress? onPress: ()=> console.log("test")}>
            <Image source={{ uri: product.image }} style={styles.image} />
            <EditIcon style={styles.editIcon} />
            <View style={styles.textContainer} >
                <Text style={styles.brandText}>{product.brand}</Text>
                <Text style={styles.nameText}>{product.name}</Text>
                <Text style={styles.priceText}>{product.price}</Text>
            </View>
        </Pressable>
    );
}

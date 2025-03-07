import React from "react";
import { View, Text } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Image } from 'expo-image';

import { getStyles } from "../styles/Product";
import { Params } from "@navigation/Router";
import Input from "@components/Input";
import Button from "@components/Button";

export type Route = {
    product?: ProductDetails;
};

type Props = NativeStackScreenProps<typeof Params, 'Product'>;

export default function Product({ navigation, route }: Props) {
    const styles = getStyles();

    let data =
    {
        name: "Hair Energizer Coffein Shampoo C1",
        description: "image 1 descr",
        price: 20,
        image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
        id: "1",
        brand: "Alpecine"
    }

    return (
        <View>
            <Image source={{ uri: data.image }} style={styles.image} />
            <Text style={styles.brandText}>Brand</Text>
            <View style={styles.brandInput}>
                <Input
                    type="text"
                />
            </View>
            <Text style={styles.nameText}>Name</Text>
            <View style={styles.nameInput}>
                <Input
                    type="text"
                />
            </View>
            <Text style={styles.descriptionText}>Description</Text>
            <View style={styles.descriptionInput}>
                <Input
                    type="text"
                />
            </View>
            <Text style={styles.priceText}>Price</Text>
            <View style={styles.priceInput}>
                <Input
                    type="text"
                />
            </View>
            <Text style={styles.quantityText}>Quantity</Text>
            <View style={styles.quantityInput}>
                <Input
                    type="text"
                />
            </View>
            <View style={styles.buttonContainer}>
                <Button title="create" />
            </View>
        </View>
    );
}

import React, { useState } from "react";
import { View, Text, FlatList } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

import { getStyles } from "../styles/ProductList";
import { getStyles as getProductStyles } from "../styles/ProductItem";
import { Params, Routes } from "@navigation/Router";
import ProductItem from "@components/ProductItem";
import PageList from "@components/PageList";
import { PageListType } from "enums";

export type Route = {
    establishmentId: number;
    serviceId: number;
    date?: string;
    startHour?: string;
    availableEmployees?: number[];
};

type Props = NativeStackScreenProps<typeof Params, 'ProductList'>;

export default function ProductList({ navigation, route }: Props) {
    const styles = getStyles();
    const stylesProduct = getProductStyles();

    let data:ProductEntity[] = [
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 1 descr",
            price: 20,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "1",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "2",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "3",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "4",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "5",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "6",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "7",
            brand: "Alpecine"
        },
        {
            name: "Hair Energizer Coffein Shampoo C1",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/product1_bg_removed.png.png",
            id: "8",
            brand: "Alpecine"
        }
    ]
    
    const loadMoreData = async (page?: IPage<ProductEntity>, params?: Record<string, string | number | boolean>) => {
        const ret ={
            content: data,
            totalPages: 1,
            totalElements: 2,
            currentPage: 1,
            pageSize: 2,
            hasNextPage: false,
            hasPreviousPage: false
        }
        return Promise.resolve(ret);
    }
    return (
        <View style={styles.listContainer}>
            <PageList<ProductEntity>
                type={PageListType.MULTI_COL_LIST}
                renderItem={({ item }: { item: ProductEntity }) => 
                    <ProductItem
                        product={item}
                    />
                }
                requestFunction={loadMoreData}
                itemMaxWidth={stylesProduct.container.width}
            />
        </View>
    );
}

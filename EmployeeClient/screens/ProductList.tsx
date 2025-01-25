import React, { useState } from "react";
import { View, Text, FlatList } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

import { getStyles } from "../styles/ProductList";
import { Params, Routes } from "@navigation/Router";
import { buildCurrencyString } from "utils/Utils";
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

export default function EmployeeSelection({ navigation, route }: Props) {
    const styles = getStyles();

    let data:ProductEntity[] = [
        {
            name: "Image 1",
            description: "image 1 descr",
            price: 20,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/f8271cc9-af14-4aa1-994e-00cf3b021d99.jpg",
            id: "0"
        },
        {
            name: "Image 2",
            description: "image 2 descpr",
            price: 15,
            image: "https://bucket-barber-staging.s3.eu-south-2.amazonaws.com/f8271cc9-af14-4aa1-994e-00cf3b021d99.jpg",
            id: "1"
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
                itemMaxWidth={150}
            />
        </View>
    );
}

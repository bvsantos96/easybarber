import React, { useEffect, useRef, useState } from "react";
import { View } from "react-native";

import PageList, { PageListRef } from "@components/PageList";
import ProductItem from "@components/ProductItem";
import { PageListType, ServiceAction } from "enums";
import useEstablishmentStore from "storage/stores/EstablishmentStore";
import useUpdateStore from "storage/stores/UpdateStore";
import { getProducts } from "utils/ApiRequest";
import { getStyles as getProductStyles } from "../styles/ProductItem";
import { getStyles } from "../styles/ProductList";

export default function ProductList({ navigation }: PropNavigation) {
    const { toUpdate, clearToUpdate } = useUpdateStore();
    const styles = getStyles();
    const stylesProduct = getProductStyles();
    const { selectedEstablishment } = useEstablishmentStore();
    const currentEstablishmentRef = useRef(selectedEstablishment);
    const [resetSearch, setResetSearch] = useState(false);
    const ref = useRef<PageListRef<ProductDetails>>(null);

    useEffect(() => {
        if (toUpdate) {
            if (toUpdate.action === ServiceAction.REFRESH) {
                setResetSearch(!resetSearch);
            } else if (toUpdate.action === ServiceAction.UPDATE && toUpdate?.id) {
                ref.current?.updateItem(+toUpdate.id, toUpdate.obj);
            } else if (toUpdate.action === ServiceAction.DELETE && toUpdate?.id) {
                ref.current?.deleteItem(+toUpdate.id);
            }
            clearToUpdate();
        }
    }, [toUpdate]);

    useEffect(() => {
        currentEstablishmentRef.current = selectedEstablishment;
        setResetSearch(!resetSearch);
    }, [selectedEstablishment]);

    const loadMoreData = async (page?: IPage<ProductDetails>, params?: Record<string, string | number | boolean>) => {
        const establishment = currentEstablishmentRef.current;
        if (establishment === undefined) return;
        let _params = {
            ...params
        };

        return await getProducts(establishment ? +establishment.id : 0, page, _params);
    }

    return (
        <View style={styles.listContainer}>
            <PageList<ProductDetails>
                ref={ref}
                type={PageListType.MULTI_COL_LIST}
                reset={resetSearch}
                renderItem={({ item }: { item: ProductDetails }) =>
                    <ProductItem
                        onPress={() => navigation.navigate('Product', { product: item })}
                        product={item}
                    />
                }
                requestFunction={loadMoreData}
                itemMaxWidth={stylesProduct.container.width}
            />
        </View>
    );
}

import React, { useEffect, useRef, useState } from "react";
import { View } from "react-native";

import { getStyles } from "../styles/ProductList";
import { getStyles as getProductStyles } from "../styles/ProductItem";
import PageList from "@components/PageList";
import { PageListType } from "enums";
import useEstablishmentStore from "storage/stores/EstablishmentStore";
import { getServices } from "utils/ApiRequest";
import ServiceItem from "@components/ServiceItem";

export default function ServiceList() {
    const styles = getStyles();
    const stylesProduct = getProductStyles();
    const { selectedEstablishment } = useEstablishmentStore();
    const currentEstablishmentRef = useRef(selectedEstablishment);
    const [resetSearch, setResetSearch] = useState(false);

    useEffect(() => {
        currentEstablishmentRef.current = selectedEstablishment;
        setResetSearch(!resetSearch);
    }, [selectedEstablishment]);

    const loadMoreData = async (page?: IPage<ServiceDetails>, params?: Record<string, string | number | boolean>) => {
        const establishment = currentEstablishmentRef.current;
        let _params = {
            ...params
        };
        if (establishment) {
            console.log(establishment.id);
            _params.establishmentId = +establishment.id;
        }
        return await getServices(page, _params);
    }

    return (
        <View style={styles.listContainer}>
            <PageList<ServiceDetails>
                type={PageListType.MULTI_COL_LIST}
                reset={resetSearch}
                renderItem={({ item }: { item: ServiceDetails }) =>
                    <ServiceItem
                        service={item}
                    />
                }
                requestFunction={loadMoreData}
                itemMaxWidth={stylesProduct.container.width}
            />
        </View>
    );
}

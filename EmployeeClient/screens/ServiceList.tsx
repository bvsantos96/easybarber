import React, { useEffect, useRef, useState } from "react";
import { View } from "react-native";

import { getStyles } from "../styles/ProductList";
import { getStyles as getServiceStyles } from "../styles/ServiceItem";
import PageList from "@components/PageList";
import { PageListType } from "enums";
import useEstablishmentStore from "storage/stores/EstablishmentStore";
import { getServices } from "utils/ApiRequest";
import ServiceItem from "@components/ServiceItem";

export default function ServiceList({ navigation }: PropNavigation) {
    const styles = getStyles();
    const stylesProduct = getServiceStyles();
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
                        onPress={() => navigation.navigate('Service', { service: item })}
                        service={item}
                    />
                }
                requestFunction={loadMoreData}
                itemMaxWidth={stylesProduct.container.width}
            />
        </View>
    );
}

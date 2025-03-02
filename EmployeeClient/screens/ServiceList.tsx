import React, { useEffect, useRef, useState } from "react";
import { View } from "react-native";

import { getStyles } from "../styles/ProductList";
import { getStyles as getServiceStyles } from "../styles/ServiceItem";
import PageList, { PageListRef } from "@components/PageList";
import { PageListType, ServiceAction } from "enums";
import useEstablishmentStore from "storage/stores/EstablishmentStore";
import { getServices } from "utils/ApiRequest";
import ServiceItem from "@components/ServiceItem";
import useUpdateStore from "storage/stores/UpdateStore";

export default function ServiceList({ navigation }: PropNavigation) {
    const { toUpdate, clearToUpdate } = useUpdateStore();
    const styles = getStyles();
    const stylesProduct = getServiceStyles();
    const { selectedEstablishment } = useEstablishmentStore();
    const currentEstablishmentRef = useRef(selectedEstablishment);
    const [resetSearch, setResetSearch] = useState(false);
    const ref = useRef<PageListRef<ServiceDetails>>(null);

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
                ref={ref}
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

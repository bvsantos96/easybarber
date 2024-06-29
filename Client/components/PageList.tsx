import React, { useEffect, useImperativeHandle, useState } from "react";
import { FlatList, View, Text } from "react-native";
import { getStyles } from "../styles/Home";
import { IPage, ITimedRequest, Identifiable } from "../declarations";
import { TimedRequest } from "../utils/TimedRequest";
import { createPageable } from "../utils/PageHandling";

interface PageListProps<T extends Identifiable> {
    renderItem: (item: { item: T }) => React.JSX.Element;
    requestFunction: (page: IPage<T>, params?: Record<string, string | number | boolean>) => Promise<IPage<T> | undefined>;
}

export interface PageListRef<T extends Identifiable> {
    loadMoreItems: (req: ITimedRequest<T>) => void;
    request: ITimedRequest<T>;
    setRequest: React.Dispatch<React.SetStateAction<ITimedRequest<any>>>;
}

const PageList = <T extends Identifiable>(props: PageListProps<T>, ref: React.Ref<PageListRef<T>>) => {
    const { renderItem, requestFunction } = props;
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [loadingMore, setLoadingMore] = useState(false);
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(createPageable<T>(), 0));

    useImperativeHandle(ref, () => ({
        loadMoreItems,
        request,
        setRequest
    }));

    const loadMoreItems = async (req = request) => {
        const result = await req.request(requestFunction);
        if (result) {
            setRequest(new TimedRequest(req.page, req.lastRequest, req.pathParams));
        }
        setLoadingMore(false);
    };

    const _loadMoreItems = () => {
        setLoadingMore(true);
    }

    useEffect(() => {
        if (loadingMore) {
            loadMoreItems();
        }
    }, [loadingMore]);

    useEffect(() => {
        _loadMoreItems();
    }, []);

    return (
        <FlatList
            data={request?.page?.content}
            style={styles.homeListContainer}
            contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom }}
            renderItem={renderItem}
            keyExtractor={(item) => item.id.toString()}
            onEndReached={_loadMoreItems}
            onEndReachedThreshold={0.1}
            ListFooterComponent={() => (
                loadingMore && (
                    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                        <Text>{texts.loaingMore}</Text>
                    </View>
                )
            )}
        />
    );
};

export default React.forwardRef(PageList) as <T extends Identifiable>(
    props: PageListProps<T> & { ref?: React.Ref<PageListRef<T>> }
) => JSX.Element;

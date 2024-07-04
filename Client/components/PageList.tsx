import React, { useEffect, useImperativeHandle, useState } from "react";
import { FlatList, View, Text } from "react-native";
import { getStyles } from "../styles/Home";
import { IPage, ITimedRequest, Identifiable } from "../declarations";
import { TimedRequest } from "../utils/TimedRequest";
import { createPageable } from "../utils/PageHandling";
import { BottomSheetFlatList } from "@gorhom/bottom-sheet";

interface PageListProps<T extends Identifiable> {
    renderItem: (item: { item: T, index: number }) => React.JSX.Element;
    requestFunction: (page: IPage<T>, params?: Record<string, string | number | boolean>) => Promise<IPage<T> | undefined>;
    inModal?: boolean;
    reset?: boolean;
    loadCache?: () => T[];
    saveCache?: (items: T[]) => void;
}

export interface PageListRef<T extends Identifiable> {
    loadMoreItems: (req: ITimedRequest<T>) => void;
    request: ITimedRequest<T>;
    setRequest: React.Dispatch<React.SetStateAction<ITimedRequest<any>>>;
}

const PageList = <T extends Identifiable>(props: PageListProps<T>, ref: React.Ref<PageListRef<T>>) => {
    const { renderItem, requestFunction, loadCache, saveCache } = props;
    const inModal = props.inModal || false;
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [loadingMore, setLoadingMore] = useState(false);
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(createPageable<T>(), 0));
    const [firstLoad, setFirstLoad] = useState(true);

    useImperativeHandle(ref, () => ({
        loadMoreItems,
        request,
        setRequest
    }));

    const loadMoreItems = async (req = request) => {
        const reqElements = req.page.content.length;
        const result = await req.request(requestFunction);
        if (result) {
            if (saveCache && reqElements !== req.page.content.length) {
                saveCache(req.page.content);
            }
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
        if (!firstLoad) {
            setRequest(new TimedRequest(createPageable<T>(), 0));
            saveCache && saveCache([]);
            _loadMoreItems();
            return;
        } else {
            setFirstLoad(false);
        }
    }, [props.reset]);

    useEffect(() => {
        if (loadCache && request) {
            const content: T[] = loadCache();
            request.page.content = content;
            request.page.currentPage = 1;
            setRequest(new TimedRequest(request.page, 0, request.pathParams));
        }
        _loadMoreItems();
    }, []);

    if (inModal) {
        return (
            <BottomSheetFlatList
                data={request?.page?.content}
                style={styles.homeListContainer}
                contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom }}
                renderItem={renderItem}
                keyExtractor={(item) => { return item.id.toString(); }}
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
    }
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

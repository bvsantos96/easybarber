import React, { useEffect, useImperativeHandle, useState } from "react";
import { FlatList, View, Text, ViewStyle } from "react-native";
import PagerView from "react-native-pager-view";
import { BottomSheetFlatList } from "@gorhom/bottom-sheet";

import { getStyles } from "../styles/Home";
import { IPage, ITimedRequest, Identifiable } from "../declarations";
import { TimedRequest } from "../utils/TimedRequest";
import { createPageable, createPageableWContent } from "../utils/PageHandling";
import { PageListType } from "../enums";

interface PageListProps<T extends Identifiable> {
    renderItem: (item: { item: T, index: number }) => React.JSX.Element;
    requestFunction: (page: IPage<T>, params?: Record<string, string | number | boolean>) => Promise<IPage<T> | undefined>;
    type?: PageListType;
    reset?: boolean;
    loadCache?: () => T[];
    saveCache?: (items: T[]) => void;
    style?: ViewStyle;
    initialItems?: T[];
}

export interface PageListRef<T extends Identifiable> {
    _loadMoreItems: () => void;
    loadMoreItems: (req: ITimedRequest<T>) => void;
    request: ITimedRequest<T>;
    setRequest: React.Dispatch<React.SetStateAction<ITimedRequest<any>>>;
}

const PageList = <T extends Identifiable>(props: PageListProps<T>, ref: React.Ref<PageListRef<T>>) => {
    const { renderItem, requestFunction, loadCache, saveCache, style } = props;
    const type = props.type || PageListType.FLAT;
    const initialItems = props.initialItems || [];
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [loadingMore, setLoadingMore] = useState(false);
    const initPage = (): IPage<T> => {
        if (initialItems.length > 0) {
            return createPageableWContent<T>(initialItems);
        }
        return createPageable<T>();
    }
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(initPage(), 0));
    const [firstLoad, setFirstLoad] = useState(true);

    useImperativeHandle(ref, () => ({
        _loadMoreItems,
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
            setRequest(new TimedRequest(initPage(), 0));
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

    switch (type) {
        case PageListType.BOTTOM_SHEET:
            return (
                <BottomSheetFlatList
                    data={request?.page?.content}
                    style={[styles.homeListContainer, { ...style }]}
                    contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom }}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={_loadMoreItems}
                    onEndReachedThreshold={0.1}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListFooterComponent={() => (
                        loadingMore && (
                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                <Text>{texts.loaingMore}</Text>
                            </View>
                        )
                    )}
                />
            );
        case PageListType.FLAT:
            return (
                <FlatList
                    data={request?.page?.content}
                    style={[styles.homeListContainer, { ...style }]}
                    contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom }}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={_loadMoreItems}
                    onEndReachedThreshold={0.1}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListFooterComponent={() => (
                        loadingMore && (
                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                <Text>{texts.loaingMore}</Text>
                            </View>
                        )
                    )}
                />);
        case PageListType.PAGERVIEW:
            if (request.page.content.length > 0) {
                return (
                    <PagerView
                        style={{ flex: 1 }} >
                        {request.page.content.map((item, index) => {
                            return renderItem({ item, index });
                        })}
                    </PagerView>
                );
            }
        default:
            return <></>;

    }
};

export default React.forwardRef(PageList) as <T extends Identifiable>(
    props: PageListProps<T> & { ref?: React.Ref<PageListRef<T>> }
) => JSX.Element;

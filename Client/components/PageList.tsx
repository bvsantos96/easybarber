import React, { useCallback, useEffect, useImperativeHandle, useState } from "react";
import { FlatList, View, Text, ViewStyle, NativeSyntheticEvent } from "react-native";
import PagerView from "react-native-pager-view";
import { BottomSheetFlatList } from "@gorhom/bottom-sheet/src";

import { getStyles } from "../styles/Home";
import { TimedRequest } from "../utils/TimedRequest";
import { createPageable } from "../utils/PageHandling";
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
    pageSize?: number;
}

interface PageSwipeEvent {
    position: number;
    offset: number;
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
    const pageSize = props.pageSize || 10;
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [loadingMore, setLoadingMore] = useState(false);
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(createPageable<T>(pageSize), 0));
    const [firstLoad, setFirstLoad] = useState(true);

    const pagerViewRef = React.useRef<PagerView>(null);

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


    const _loadMoreItems = useCallback(async () => {
        if (loadingMore) return;

        try {
            setLoadingMore(true);
            await loadMoreItems();
        } finally {
            setLoadingMore(false);
        }
    }, [loadingMore]);

    useEffect(() => {
        if (!firstLoad) {
            setRequest(new TimedRequest(createPageable<T>(pageSize), 0));
            saveCache && saveCache([]);
            setLoadingMore(true);
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
                    onEndReached={() => { console.log("onEndReached"); _loadMoreItems(); }}
                    onEndReachedThreshold={0.3}
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
                    data={(request?.page?.content && request.page.content.length > 0) ? request.page.content : initialItems}
                    style={[styles.homeListContainer, { ...style }]}
                    contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom, minHeight: '100%' }}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={() => { console.log("onEndReached2"); _loadMoreItems(); }}
                    onEndReachedThreshold={0.3}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListEmptyComponent={() => (<View style={{ height: '100%' }} />)}
                    ListFooterComponent={() => (
                        loadingMore && (
                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                <Text>{texts.loaingMore}</Text>
                            </View>
                        )
                    )}
                />);
        case PageListType.PAGERVIEW:
            return (

                <PagerView
                    ref={pagerViewRef}
                    scrollEnabled={true}
                    overScrollMode={'never'}
                    onPageScroll={async (event: NativeSyntheticEvent<PageSwipeEvent>) => {
                        const { position } = event.nativeEvent;
                        if (position >= request.page.content.length - 3) {
                            _loadMoreItems();
                        }
                    }}
                    style={{ flex: 1 }} >
                    {(request.page.content.length > 0 ? request.page.content : initialItems).map((item, index) => {
                        return renderItem({ item, index });
                    })}
                </PagerView>
            );
        default:
            return <></>;

    }
};

export default React.forwardRef(PageList) as <T extends Identifiable>(
    props: PageListProps<T> & { ref?: React.Ref<PageListRef<T>> }
) => JSX.Element;

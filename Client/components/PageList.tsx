import React, { useEffect, useImperativeHandle, useRef, useState } from "react";
import { FlatList, View, Text, ViewStyle, NativeSyntheticEvent } from "react-native";
import PagerView from "react-native-pager-view";
import { BottomSheetFlatList } from "@gorhom/bottom-sheet/src";

import { getStyles } from "../styles/Home";
import { TimedRequest } from "../utils/TimedRequest";
import { createPageable } from "../utils/PageHandling";
import { PageListType } from "../enums";
import { debounce } from "lodash";

interface PageListProps<T extends Identifiable> {
    renderItem: (item: { item: T, index: number }) => React.JSX.Element;
    requestFunction: (page: IPage<T>, params?: Record<string, string | number | boolean>) => Promise<IPage<T> | undefined>;
    type?: PageListType;
    reset?: boolean;
    loadCache?: () => T[];
    saveCache?: (items: T[]) => void;
    resetCache?: () => void;
    style?: ViewStyle;
    initialItems?: T[];
    pageSize?: number;
    preload?: boolean;
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
    deleteItem: (id: number) => void;
}

const PageList = <T extends Identifiable>(props: PageListProps<T>, ref: React.Ref<PageListRef<T>>) => {
    const { renderItem, requestFunction, loadCache, saveCache, resetCache, style, preload = true } = props;
    const type = props.type || PageListType.FLAT;
    const initialItems = props.initialItems || [];
    const pageSize = props.pageSize || 10;
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(createPageable<T>(pageSize), 0));
    const [loadingMore, setLoadingMore] = useState(false);
    const firstEndReached = useRef(true);
    const setFirstEndReached = (value: boolean) => {
        firstEndReached.current = value;
    }
    const firstLoad = useRef(true);
    const setFirstLoad = (value: boolean) => {
        firstLoad.current = value;
    }

    const pagerViewRef = React.useRef<PagerView>(null);

    const loadMoreItems = async (req = request) => {
        setLoadingMore(true);
        const reqElements = req.page.content.length;
        await req.request(requestFunction);
        if (saveCache && reqElements !== req.page.content.length) {
            saveCache(req.page.content);
        }
        setRequest(new TimedRequest(req.page, req.lastRequest, req.pathParams));
        setLoadingMore(false);
    };

    const _loadMoreItems = useRef(debounce(loadMoreItems, 300)).current;

    const deleteItem = (id: number) => {
        const newItems = request.page.content.filter((item) => item.id !== id);
        request.page.content = newItems;
        setRequest(new TimedRequest(request.page, request.lastRequest, request.pathParams));
    }

    useImperativeHandle(ref, () => ({
        _loadMoreItems,
        loadMoreItems,
        request,
        setRequest,
        deleteItem,
    }));

    useEffect(() => {
        if (loadCache && request) {
            const content: T[] = loadCache();
            request.page.content = content;
            request.page.currentPage = 1;
            setRequest(new TimedRequest(request.page, 0, request.pathParams));
        }

        if (preload) {
            loadMoreItems();
        }
    }, []);

    const resetList = () => {
        firstEndReached.current = true;
        if (resetCache) resetCache(); else { saveCache && saveCache([]); }
        loadMoreItems(new TimedRequest(createPageable<T>(pageSize), 0));
        return;
    }

    const _resetList = useRef(debounce(resetList, 300)).current;

    useEffect(() => {
        if (!firstLoad.current) {
            _resetList();
        } else {
            setFirstLoad(false);
        }
    }, [props.reset]);

    switch (type) {
        case PageListType.BOTTOM_SHEET:
            return (
                <BottomSheetFlatList
                    data={request?.page?.content}
                    style={[styles.homeListContainer, { ...style }]}
                    contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom }}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={() => {
                        if (loadingMore) return;
                        if (preload || (!preload && !firstEndReached.current)) {
                            _loadMoreItems();
                        } else {
                            setFirstEndReached(false);
                        }
                    }}
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
                    refreshing={loadingMore}
                    onRefresh={_resetList}
                    data={(request?.page?.content && request.page.content.length > 0) ? request.page.content : initialItems}
                    style={[styles.homeListContainer, { ...style }]}
                    contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom, minHeight: '100%' }}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={() => {
                        if (loadingMore) return;
                        if (preload || (!preload && !firstEndReached.current)) {
                            _loadMoreItems();
                        } else {
                            setFirstEndReached(false);
                        }
                    }}
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
                        if (loadingMore) return;
                        const { position } = event.nativeEvent;
                        if (position >= request.page.content.length - 3) {
                            _loadMoreItems();
                        } else {
                            setFirstLoad(false);
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

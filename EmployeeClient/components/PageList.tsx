import React, { useCallback, useEffect, useImperativeHandle, useRef, useState } from "react";
import { FlatList, View, Text, ViewStyle, NativeSyntheticEvent, LayoutChangeEvent } from "react-native";
import PagerView from "react-native-pager-view";
import { BottomSheetFlatList } from "@gorhom/bottom-sheet/src";

import { getStyles } from "@styles/Home";
import { TimedRequest } from "../utils/TimedRequest";
import { createPageable } from "../utils/PageHandling";
import { PageListType } from "../enums";
import { debounce } from "lodash";
import texts from "@lang/en.json";
import Button from "./Button";
import Divider from "./Divider";
import { useTheme } from "@styles/ThemeContext";

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
    itemMaxWidth?: number;
    dontDisplayLoadMore?: boolean;
    gap?: number;
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
    const { renderItem, requestFunction, loadCache, saveCache, resetCache, style, preload = true, itemMaxWidth, dontDisplayLoadMore = false, gap = 5 } = props;
    const type = props.type || PageListType.FLAT;
    const theme = useTheme();
    const initialItems = props.initialItems || [];
    const pageSize = props.pageSize || 10;
    const styles = getStyles();
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(createPageable<T>(pageSize), 0));
    const [loadingMore, setLoadingMore] = useState(false);
    const firstEndReached = useRef(true);
    const pagerViewRef = React.useRef<PagerView>(null);
    const timeoutRef = useRef<NodeJS.Timeout | null>(null);;
    const [firstLoad, setFirstLoad] = useState(true);

    const startTimeout = () => {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
        }

        timeoutRef.current = setTimeout(() => {
            _resetList();
        }, 300000);
    };

    const loadMoreItems = async (req = request) => {
        setLoadingMore(true);
        try {
            const reqElements = req.page.content.length;
            await req.request(requestFunction);
            if (saveCache && reqElements !== req.page.content.length) {
                saveCache(req.page.content);
            }
            setRequest(new TimedRequest(req.page, req.lastRequest, req.pathParams));
            startTimeout();
        } finally {
            setLoadingMore(false);
            setFirstLoad(false);
        }
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

        return () => {
            if (timeoutRef.current) {
                clearTimeout(timeoutRef.current);
            }
        };
    }, []);

    const resetList = async () => {
        firstEndReached.current = true;
        if (resetCache) resetCache(); else { saveCache && saveCache([]); }
        setLoadingMore(true);
        _loadMoreItems(new TimedRequest(createPageable<T>(pageSize), 0));
        return;
    }

    const _resetList = useRef(debounce(resetList, 300)).current;

    useEffect(() => {
        if (!firstLoad) {
            _resetList();
        }
    }, [props.reset]);

    const [columns, setColumns] = useState(1);

    const calculateColumns = useCallback((width = theme.dimensions.width) => {
        if (!itemMaxWidth) {
            return 1;
        }
        const calculatedColumns = Math.round(width / (itemMaxWidth + gap * 2 * theme.dimensions.absoluteWidth));
        return calculatedColumns > 0 ? calculatedColumns : 1;
    }, [itemMaxWidth]);

    const [listWidth, setListWidth] = useState(theme.dimensions.width);
    const updateColumns = (event: LayoutChangeEvent) => {
        const { width } = event.nativeEvent.layout;
        setListWidth(width);
        setColumns(calculateColumns(width));
    };

    useEffect(() => {
        setColumns(calculateColumns());
    }, [itemMaxWidth, theme.dimensions.width]);

    switch (type) {
        case PageListType.BOTTOM_SHEET:
            return (
                <BottomSheetFlatList
                    data={request?.page?.content}
                    style={[styles.homeListContainer, { ...style }]}
                    contentContainerStyle={{ paddingBottom: styles.listBottom.paddingBottom }}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={debounce(() => {
                        if (loadingMore) return;
                        if (preload || (!preload && !firstEndReached.current)) {
                            loadMoreItems();
                        }
                    }, 300)}
                    onEndReachedThreshold={0.3}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListEmptyComponent={() =>
                        (dontDisplayLoadMore || firstLoad) ? (
                            null
                        ) : (
                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                <View style={{ justifyContent: "center", alignItems: 'center' }}>
                                    <Text>{texts.noItems}</Text>
                                    <Divider size={10} horizontal={false} />
                                    <Button stylesInput={{ maxHeight: "50%", minWidth: "25%" }} title={texts.reload} onPress={_resetList} />
                                </View>
                            </View>
                        )
                    }
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
                    nestedScrollEnabled={true}
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={debounce(() => {
                        if (loadingMore) return;
                        if (preload || (!preload && !firstEndReached.current)) {
                            loadMoreItems();
                        }
                    }, 300)}
                    onEndReachedThreshold={0.3}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListEmptyComponent={() =>
                        (dontDisplayLoadMore || firstLoad) ? (
                            null
                        ) : (
                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                <View style={{ justifyContent: "center", alignItems: 'center' }}>
                                    <Text>{texts.noItems}</Text>
                                    <Divider size={10} horizontal={false} />
                                    <Button stylesInput={{ maxHeight: "50%", minWidth: "25%" }} title={texts.reload} onPress={_resetList} />
                                </View>
                            </View>
                        )
                    }
                />);
        case PageListType.PAGERVIEW: return (
            <PagerView
                ref={pagerViewRef}
                scrollEnabled={true}
                overScrollMode={'never'}
                onPageScroll={async (event: NativeSyntheticEvent<PageSwipeEvent>) => {
                    if (loadingMore) return;
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
        case PageListType.MULTI_COL_LIST: return ( //TODO: scrolling on gaps is not working
            <FlatList
                key={columns}
                refreshing={loadingMore}
                onRefresh={_resetList}
                data={(request?.page?.content && request.page.content.length > 0) ? request.page.content : initialItems}
                numColumns={columns}
                horizontal={false}
                style={[styles.homeListContainer, { ...style }]}
                contentContainerStyle={{
                    paddingBottom: styles.listBottom.paddingBottom,
                    alignItems: "center",
                    rowGap: gap * theme.dimensions.absoluteHeight
                }}
                renderItem={(item) => {
                    return (columns > 1 ? (
                        <View style={{
                            width: (listWidth / columns) - gap * 4 * theme.dimensions.absoluteWidth,
                            alignItems: "center",
                            justifyContent: "center"
                        }}>
                            {renderItem(item)}
                        </View>
                    ) : (
                        renderItem(item)
                    ));
                }}
                keyExtractor={(item) => item.id.toString()}
                onEndReached={debounce(() => {
                    if (loadingMore) return;
                    if (preload || (!preload && !firstEndReached.current)) {
                        loadMoreItems();
                    }
                }, 300)}
                onEndReachedThreshold={0.3}
                showsVerticalScrollIndicator={false}
                showsHorizontalScrollIndicator={false}
                onLayout={updateColumns}
                ListEmptyComponent={() =>
                    (dontDisplayLoadMore || firstLoad) ? null : (
                        <View style={{
                            flex: 1,
                            justifyContent: 'center',
                            alignItems: 'center',
                            padding: 10
                        }}>
                            <View style={{
                                justifyContent: "center",
                                alignItems: 'center'
                            }}>
                                <Text>{texts.noItems}</Text>
                                <Divider size={10} horizontal={false} />
                                <Button
                                    stylesInput={{
                                        maxHeight: "50%",
                                        minWidth: "25%"
                                    }}
                                    title={texts.reload}
                                    onPress={_resetList}
                                />
                            </View>
                        </View>
                    )
                }
            />);
        default:
            return <></>;

    }
};

export default React.forwardRef(PageList) as <T extends Identifiable>(
    props: PageListProps<T> & { ref?: React.Ref<PageListRef<T>> }
) => JSX.Element;

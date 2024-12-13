import React, { useEffect, useImperativeHandle, useRef, useState } from "react";
import { FlatList, View, Text, ViewStyle, NativeSyntheticEvent, ActivityIndicator } from "react-native";
import PagerView from "react-native-pager-view";
import { BottomSheetFlatList } from "@gorhom/bottom-sheet/src";

import { getStyles } from "../styles/Home";
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
}

interface PageSwipeEvent {
    position: number;
    offset: number;
}
export interface PageListRef<T extends Identifiable> {
    _loadMoreItems: (req?: ITimedRequest<T>) => void;
    loadMoreItems: (req: ITimedRequest<T>) => void;
    request: ITimedRequest<T>;
    setRequest: React.Dispatch<React.SetStateAction<ITimedRequest<any>>>;
    deleteItem: (id: number) => void;
    reset: (req: ITimedRequest<T>) => void;
}

const PageList = <T extends Identifiable>(props: PageListProps<T>, ref: React.Ref<PageListRef<T>>) => {
    const theme = useTheme();
    const { renderItem, requestFunction, loadCache, saveCache, resetCache, style, preload = true } = props;
    const type = props.type || PageListType.FLAT;
    const initialItems = props.initialItems || [];
    const pageSize = props.pageSize || 10;
    const styles = getStyles();
    const [request, setRequest] = useState<ITimedRequest<T>>(new TimedRequest(createPageable<T>(pageSize), 0));
    const [loadingMore, setLoadingMore] = useState(false);
    const [reseting, setReseting] = useState(false);
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
            setReseting(false);
        }
    };

    const _loadMoreItems = useRef(
        debounce((passedReq = request) => {
            loadMoreItems(passedReq);
        }, 300)
    ).current;

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
        reset: _resetList
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

    const resetList = (req?: ITimedRequest<T>) => {
        setLoadingMore(true);
        setFirstLoad(true);
        setReseting(true);
        firstEndReached.current = true;
        if (resetCache) resetCache(); else { saveCache && saveCache([]); }
        if (req) {
            req.page.currentPage = 1;
            setRequest(new TimedRequest(req.page, 0, req.pathParams));
            loadMoreItems(req);
            return;
        }
        loadMoreItems(new TimedRequest(createPageable<T>(pageSize), 0));
        return;
    }

    const _resetList = useRef(debounce(resetList, 300)).current;

    useEffect(() => {
        if (!firstLoad) {
            _resetList();
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
                        }
                    }}
                    onEndReachedThreshold={0.3}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListEmptyComponent={() =>
                        loadingMore ? (
                            reseting ? (
                                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                    <ActivityIndicator size="large" color={theme.colors.text.lightGray} />
                                </View>
                            ) : null
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
                    keyExtractor={(item) => item.id.toString()}
                    onEndReached={() => {
                        if (loadingMore) return;
                        if (preload || (!preload && !firstEndReached.current)) {
                            _loadMoreItems();
                        }
                    }}
                    onEndReachedThreshold={0.3}
                    showsVerticalScrollIndicator={false}
                    showsHorizontalScrollIndicator={false}
                    ListEmptyComponent={() =>
                        loadingMore ? (
                            reseting ? (
                                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                    <ActivityIndicator size="large" color={theme.colors.text.lightGray} />
                                </View>
                            ) : null
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
        default:
            return <></>;

    }
};

export default React.forwardRef(PageList) as <T extends Identifiable>(
    props: PageListProps<T> & { ref?: React.Ref<PageListRef<T>> }
) => JSX.Element;

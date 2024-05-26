import { FlatList, Text, View } from 'react-native';
import { useEffect, useRef, useState } from 'react';

import { getStyles as topBarGetStyles } from '../styles/TopBar';
import { getStyles as getHomeGetStyles } from '../styles/Home';

import TopBar from '../components/TopBar';
import { getBarbersNearMe } from '../utils/ApiRequest';
import ListItem from '../components/ListItemBarbershop';
import Filter, { FilterRef } from './Filter';

import ExpandableView from '../components/ExpandableView';
import Divider from '../components/Divider';
import Category from '../components/Category';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { createPageable } from '../utils/PageHandling';
import { TimedRequest } from '../utils/TimedRequest';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import { SvgUri } from 'react-native-svg';
import { BarberInfo, ICategory, IFilterRequest, ITimedRequest } from '../declarations';

export default function Home() {
    const topBarStyles = topBarGetStyles();
    const homeStyles = getHomeGetStyles();
    const filterRef = useRef<FilterRef>(null);
    const [barberRequest, setBarberRequest] = useState<ITimedRequest<BarberInfo>>(new TimedRequest(createPageable<BarberInfo>(), 0));
    const [topCategoriesExpanded, setTopCategoriesExpanded] = useState(true);
    const [nearbyBarbersExpanded, setNearbyBarbersExpanded] = useState(false);
    const [filterExpanded, setFilterExpanded] = useState(false);
    const [loadingMore, setLoadingMore] = useState(false);
    const texts = require("../langs/en.json");
    const inserts = useSafeAreaInsets();
    const [categories, setCategories] = useState<ICategory[]>([]);

    const _loadMoreItems = () => {
        setLoadingMore(true);
    }

    const loadMoreItems = async (req = barberRequest) => {
        const result = await req.request(getBarbersNearMe);
        if (result) {
            setBarberRequest(new TimedRequest(req.page, req.lastRequest, req.pathParams));
        }
        setLoadingMore(false);
    };

    useEffect(() => {
        if (loadingMore) {
            loadMoreItems();
        }
    }, [loadingMore]);

    const loadCategories = async () => {
        setCategories(await retrieveCategories());
    }

    useEffect(() => {
        _loadMoreItems();
        loadCategories();
    }, []);

    const toggleFilter = () => {
        setFilterExpanded(!filterExpanded);
        filterRef.current?.handlePresentModalPress();
    }

    const setFilter = (filter: IFilterRequest) => {
        let req: ITimedRequest<BarberInfo> = new TimedRequest(createPageable<BarberInfo>(), 0, filter);
        loadMoreItems(req);
    }

    return (
        <>
            <TopBar toggleFilter={toggleFilter} />
            <View style={topBarStyles.homeContainer}>
                <Divider size={28.44} color="transparent" />
                <ExpandableView
                    style={homeStyles.topCategoriesContainer}
                    maxHeight={homeStyles.topCategoriesHeights.maxHeight}
                    expanded={topCategoriesExpanded}
                    onExpand={() => { setNearbyBarbersExpanded(topCategoriesExpanded); setTopCategoriesExpanded(!topCategoriesExpanded) }}
                    title={texts.topCategories}>
                    <Divider size={19} />
                    <View style={homeStyles.topCategoriesList}>
                        {categories && categories.map((category) => (
                            <Category
                                key={category.id}
                                id={category.id}
                                icon={
                                    <SvgUri width={topBarStyles.categoryIcon.width}
                                        height={topBarStyles.categoryIcon.height}
                                        style={homeStyles.alignCenter}
                                        uri={category.imageURL} />}
                                title={category.name}
                                expanded={topCategoriesExpanded}
                                select={setFilter}
                            />
                        ))}
                    </View>
                    <Divider size={19} />
                </ExpandableView>
                <ExpandableView
                    style={homeStyles.nearByBarbersContainer}
                    maxHeight={homeStyles.nearByBarbersContainerHeights.maxHeight - inserts.bottom * 2}
                    minHeight={homeStyles.nearByBarbersContainerHeights.minHeight - inserts.bottom}
                    expanded={nearbyBarbersExpanded}
                    onExpand={() => { setTopCategoriesExpanded(nearbyBarbersExpanded); setNearbyBarbersExpanded(!nearbyBarbersExpanded) }}
                    title={texts.nearbyBarbers}>
                    <Divider size={10} />
                    <FlatList
                        data={barberRequest?.page?.content}
                        style={homeStyles.homeListContainer}
                        contentContainerStyle={{ paddingBottom: homeStyles.listBottom.paddingBottom }}
                        renderItem={({ item }) => <ListItem barber={item} />}
                        keyExtractor={(item) => item.id.toString()}
                        onEndReached={_loadMoreItems}
                        onEndReachedThreshold={0.1}
                        ListFooterComponent={() => (
                            loadingMore && (
                                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 10 }}>
                                    <Text>Loading more...</Text>
                                </View>
                            )
                        )}
                    />
                </ExpandableView>
            </View>
            <Filter ref={filterRef} setFilter={setFilter}/>
        </>
    );
}

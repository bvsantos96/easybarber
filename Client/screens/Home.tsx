import { View } from 'react-native';
import React, { useEffect, useRef, useState } from 'react';

import { getStyles as topBarGetStyles } from '../styles/TopBar';
import { getStyles as getHomeGetStyles } from '../styles/Home';

import TopBar from '../components/TopBar';
import { getNearByBarbers } from '../utils/ApiRequest';
import ListItem from '../components/ListItemBarbershop';

import ExpandableView from '../components/ExpandableView';
import Divider from '../components/Divider';
import Category from '../components/Category';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { createPageable } from '../utils/PageHandling';
import { TimedRequest } from '../utils/TimedRequest';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import { SvgUri } from 'react-native-svg';
import { BarberInfo, ICategory, IFilterRequest, IPage, ITimedRequest } from '../declarations';
import PageList, { PageListRef } from '../components/PageList';
import useLocationStore from '../storage/stores/LocationStore';

export default function Home() {
    const topBarStyles = topBarGetStyles();
    const homeStyles = getHomeGetStyles();
    const pageListRef = useRef<PageListRef<BarberInfo>>(null);
    const [topCategoriesExpanded, setTopCategoriesExpanded] = useState(true);
    const [nearbyBarbersExpanded, setNearbyBarbersExpanded] = useState(false);
    const texts = require("../langs/en.json");
    const inserts = useSafeAreaInsets();
    const [categories, setCategories] = useState<ICategory[]>([]);
    const [resetSearch, setResetSearch] = useState(false);

    const {
        selectedLocation,
        getSelectedLocation,
        setLocations
    } = useLocationStore();

    const loadCategories = async () => {
        setCategories(await retrieveCategories());
    }

    useEffect(() => {
        getSelectedLocation();
        loadCategories();
    }, []);

    useEffect(() => {
        setResetSearch(!resetSearch);
    }, [selectedLocation]);

    const replaceFilter = (filter: IFilterRequest) => {
        let req: ITimedRequest<BarberInfo> = new TimedRequest(createPageable<BarberInfo>(), 0, filter);
        pageListRef?.current?.loadMoreItems(req);
    }

    const setFilter = (_filter: IFilterRequest) => {
        replaceFilter({ ...pageListRef?.current?.request.pathParams, ..._filter });
    }

    const setName = (name: string) => {
        pageListRef?.current?.setRequest({ ...pageListRef?.current?.request, pathParams: { ...pageListRef?.current?.request.pathParams, partialName: name } });
    }

    const loadMoreLocations = async (page?: IPage<BarberInfo>, params?: Record<string, string | number | boolean>) => {
        if (selectedLocation)
            return await getNearByBarbers(page, params, selectedLocation);
        return undefined;
    }

    return (
        <>
            <TopBar location={selectedLocation} filter={pageListRef?.current?.request.pathParams} setFilter={setFilter} setName={setName} />
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
                                selectedCategory={
                                    pageListRef?.current?.request.pathParams && typeof pageListRef?.current?.request.pathParams === 'object'
                                        && 'serviceType' in pageListRef?.current?.request.pathParams
                                        && typeof pageListRef?.current?.request.pathParams.serviceType === 'string'
                                        ? parseInt(pageListRef?.current?.request.pathParams.serviceType)
                                        : -1
                                }
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
                    <PageList<BarberInfo> reset={resetSearch} ref={pageListRef} renderItem={({ item }: { item: BarberInfo }) => <ListItem barber={item} />} requestFunction={loadMoreLocations} />
                </ExpandableView>
            </View>
        </>
    );
}

import { View, Text } from 'react-native';
import React, { useEffect, useRef, useState } from 'react';

import { getStyles as topBarGetStyles } from '../styles/TopBar';
import { getStyles as getHomeGetStyles } from '../styles/Home';
import { getStyles as getExpandedGetStyles } from '../styles/ExpandableView';

import TopBar from '../components/TopBar';
import { getNearByBarbers } from '../utils/ApiRequest';
import ListItem from '../components/ListEstablishments';

import ExpandableView from '../components/ExpandableView';
import Divider from '../components/Divider';
import Category from '../components/Category';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { createPageable } from '../utils/PageHandling';
import { TimedRequest } from '../utils/TimedRequest';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import { SvgUri } from 'react-native-svg';
import PageList, { PageListRef } from '../components/PageList';
import useLocationStore from '../storage/stores/LocationStore';
import { NavigationProp } from '@react-navigation/native';
import { getSelectedLocation } from 'utils/Location';
import Pressable from '@components/Pressable';

export type Props = {
    navigation: NavigationProp<any, any>,
}

export default function Home({ navigation }: Props) {
    const topBarStyles = topBarGetStyles();
    const homeStyles = getHomeGetStyles();
    const expandedStyles = getExpandedGetStyles();
    const pageListRef = useRef<PageListRef<EstablishmentInfo>>(null);
    const [topCategoriesExpanded, setTopCategoriesExpanded] = useState(true);
    const [nearbyBarbersExpanded, setNearbyBarbersExpanded] = useState(false);
    const texts = require("../langs/en.json");
    const inserts = useSafeAreaInsets();
    const [categories, setCategories] = useState<ICategory[]>([]);
    const [resetSearch, setResetSearch] = useState(false);
    const [filter, _setFilter] = useState<IFilterRequest | undefined>({});

    const {
        selectedLocation,
    } = useLocationStore();

    const loadCategories = async () => {
        setCategories(await retrieveCategories());
    }

    useEffect(() => {
        const _getSelectedLocation = async () => {
            await getSelectedLocation();
        };
        _getSelectedLocation();
        loadCategories();
    }, []);

    useEffect(() => {
        setResetSearch(!resetSearch);
    }, [selectedLocation]);

    const replaceFilter = (filter: IFilterRequest) => {
        let req: ITimedRequest<EstablishmentInfo> = new TimedRequest(createPageable<EstablishmentInfo>(), 0, filter);
        pageListRef?.current?.loadMoreItems(req);
        _setFilter(filter);
    }

    const setFilter = (_filter: IFilterRequest) => {
        replaceFilter({ ...pageListRef?.current?.request.pathParams, ..._filter });
    }

    const setName = (name: string) => {
        pageListRef?.current?.setRequest({ ...pageListRef?.current?.request, pathParams: { ...pageListRef?.current?.request.pathParams, partialName: name } });
    }

    const loadMoreLocations = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>) => {
        let _selectedLocation = selectedLocation;
        if (selectedLocation === undefined) {
            _selectedLocation = await getSelectedLocation();
        }
        return await getNearByBarbers(page, params, selectedLocation);
    }

    return (
        <>
            <TopBar location={selectedLocation} filter={filter} setFilter={setFilter} setName={setName} />
            <View style={topBarStyles.homeContainer}>
                <Divider size={10} color="transparent" />
                <ExpandableView
                    style={homeStyles.topCategoriesContainer}
                    maxHeight={homeStyles.topCategoriesHeights.maxHeight}
                    onExpand={() => { setNearbyBarbersExpanded(topCategoriesExpanded); setTopCategoriesExpanded(!topCategoriesExpanded) }}
                    expanded={topCategoriesExpanded}
                    title={texts.topCategories}>
                    <Divider size={10} />
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
                                select={setFilter}
                                selectedCategory={
                                    filter && typeof filter === 'object'
                                        && 'serviceType' in filter
                                        && typeof filter.serviceType === 'string'
                                        ? parseInt(filter.serviceType)
                                        : -1
                                }
                            />
                        ))}
                    </View>
                </ExpandableView>
                <View style={expandedStyles.titleContainer}>
                    <Text style={expandedStyles.titleText}>{texts.nearbyBarbers}</Text>
                    <Pressable style={expandedStyles.expandContainer} onPress={() => { setTopCategoriesExpanded(nearbyBarbersExpanded); setNearbyBarbersExpanded(!nearbyBarbersExpanded) }}>
                        <Text style={expandedStyles.expandText} >{texts.viewAll}</Text>
                    </Pressable>
                </View>
                <View style={[homeStyles.nearByBarbersContainer]}>
                    <Divider size={10} />
                    <PageList<EstablishmentInfo>
                        reset={resetSearch}
                        ref={pageListRef}
                        renderItem={({ item }: { item: EstablishmentInfo }) =>
                            <ListItem
                                onPress={
                                    () => {
                                        navigation.navigate(texts.tabs.establishmentDetails, item);
                                    }
                                }
                                establishment={item} />
                        }
                        requestFunction={loadMoreLocations} />
                </View>
            </View >
        </>
    );
}

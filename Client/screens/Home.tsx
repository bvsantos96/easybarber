import { ScrollView, View, Text } from 'react-native';
import { useEffect, useRef, useState } from 'react';

import { getStyles as topBarGetStyles } from '../styles/TopBar';
import { getStyles as getHomeGetStyles } from '../styles/Home';

import TopBar from '../components/TopBar';
import { BarberInfo, getBarbersNearMe } from '../utils/ApiRequest';
import ListItem from '../components/ListItemBarbershop';
import Filter, { FilterRef } from './Filter';

import BarberIcon from '@assets/icons/haircut.svg';
import SpaIcon from '@assets/icons/spa.svg';
import CreamBathIcon from '@assets/icons/creamBath.svg';
import MassageIcon from '@assets/icons/massage.svg';
import ExpandableView from '../components/ExpandableView';
import Divider from '../components/Divider';

export default function Home() {
    const topBarStyles = topBarGetStyles();
    const homeStyles = getHomeGetStyles();
    const filterRef = useRef<FilterRef>(null);
    const [barberList, setBarberList] = useState<BarberInfo[]>([]);
    const [topCategoriesExpanded, setTopCategoriesExpanded] = useState(true);
    const [nearbyBarbersExpanded, setNearbyBarbersExpanded] = useState(false);
    const [filterExpanded, setFilterExpanded] = useState(false);
    const texts = require("../langs/en.json");

    useEffect(() => {
        const fetchBarbers = async () => {
            const barbers: BarberInfo[] = await getBarbersNearMe();
            setBarberList(barbers);
        }

        fetchBarbers();
    }, []);

    const toggleFilter = () => {
        setFilterExpanded(!filterExpanded);
        filterRef.current?.handlePresentModalPress();
    }

    return (
        <>
            <TopBar toggleFilter={toggleFilter} />
            <View style={topBarStyles.homeContainer}>
                <Divider size={28.44} color="transparent"  />
                <ExpandableView
                    style={homeStyles.topCategoriesContainer}
                    maxHeight={homeStyles.topCategoriesHeights.maxHeight}
                    expanded={topCategoriesExpanded}
                    onExpand={() => {setNearbyBarbersExpanded(topCategoriesExpanded);setTopCategoriesExpanded(!topCategoriesExpanded)}}
                    title={texts.topCategories}>
                    <Divider size={19} />
                    <View style={homeStyles.topCategoriesList}>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <BarberIcon style={[topBarStyles.categoryIcon, homeStyles.alignCenter]} />
                            </View>
                            <Divider size={8} />
                            <Text style={topBarStyles.categoryText}>{texts.haircut}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <SpaIcon style={[topBarStyles.categoryIcon, homeStyles.alignCenter]} />
                            </View>
                            <Divider size={8} />
                            <Text style={topBarStyles.categoryText}>{texts.spa}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <CreamBathIcon style={[topBarStyles.categoryIcon, homeStyles.alignCenter]} />
                            </View>
                            <Divider size={8} />
                            <Text style={topBarStyles.categoryText}>{texts.creamBath}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <MassageIcon style={[topBarStyles.categoryIcon, homeStyles.alignCenter]} />
                            </View>
                            <Divider size={8} />
                            <Text style={topBarStyles.categoryText}>{texts.massage}</Text>
                        </View>
                    </View>
                    <Divider size={19} />
                </ExpandableView>
                <ExpandableView
                    style={homeStyles.nearByBarbersContainer}
                    maxHeight={homeStyles.nearByBarbersContainerHeights.maxHeight}
                    minHeight={homeStyles.nearByBarbersContainerHeights.minHeight}
                    expanded={nearbyBarbersExpanded}
                    onExpand={() => { setTopCategoriesExpanded(nearbyBarbersExpanded); setNearbyBarbersExpanded(!nearbyBarbersExpanded) }}
                    title={texts.nearbyBarbers}>
                    <Divider size={10} />
                    <ScrollView style={homeStyles.homeListContainer}>
                        {barberList && barberList.map((barber: BarberInfo, idx: number) => {
                            return (
                                <ListItem key={idx} barber={barber} />
                            );
                        })}
                    </ScrollView>
                </ExpandableView>
            </View>
            <Filter ref={filterRef} />
        </>
    );
}

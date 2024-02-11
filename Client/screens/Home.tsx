import { ScrollView, View, Text } from 'react-native';
import { useEffect, useRef, useState } from 'react';

import { absoluteHeight, styles } from '../styles/Main';
import { getStyles } from '../styles/TopBar';

import TopBar from '../components/TopBar';
import { BarberInfo, getBarbersNearMe } from '../utils/ApiRequest';
import ListItem from '../components/ListItemBarbershop';
import Pressable from '../components/Pressable';
import Filter, { FilterRef } from './Filter';

import BarberIcon from '@assets/icons/haircut.svg';
import SpaIcon from '@assets/icons/spa.svg';
import CreamBathIcon from '@assets/icons/creamBath.svg';
import MassageIcon from '@assets/icons/massage.svg';
import Divider from '../components/Divider';

export default function Home() {
    const topBarStyles = getStyles();
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
                <View style={[styles.row, styles.w100, topCategoriesExpanded ? topBarStyles.topCategoriesContainerExpanded :  topBarStyles.topCategoriesContainer]}>
                    <Text style={[styles.fontPoppins, styles.fontWeight700, styles.fontSize18, styles.colorDarkTitle, topBarStyles.homeTitleContainer]}>{texts.topCategories}</Text>
                    <Pressable style={[topBarStyles.viewAllContainer, styles.w100, styles.alignRight]} onPress={() => {setNearbyBarbersExpanded(topCategoriesExpanded);setTopCategoriesExpanded(!topCategoriesExpanded)}}>
                        <Text style={[styles.fontPoppins, styles.fontSize18, styles.colorDarkTitle, styles.fontWeight400]} >{texts.viewAll}</Text>
                    </Pressable>
                    <View style={topCategoriesExpanded ? topBarStyles.categoriesContainerExpanded : topBarStyles.categoriesContainer}>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <BarberIcon style={[topBarStyles.categoryIcon, styles.alignCenter]} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.haircut}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <SpaIcon style={[topBarStyles.categoryIcon, styles.alignCenter]} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.spa}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <CreamBathIcon style={[topBarStyles.categoryIcon, styles.alignCenter]} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.creamBath}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <MassageIcon style={[topBarStyles.categoryIcon, styles.alignCenter]} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.massage}</Text>
                        </View>
                    </View>
                </View>
                <View style={ nearbyBarbersExpanded ? topBarStyles.nearByBarbersContainerExpanded : topBarStyles.nearByBarbersContainer}>
                    <View style={[styles.row, topBarStyles.nearByContainer]}>
                        <Text style={[styles.fontPoppins, styles.fontWeight600, styles.fontSize18, styles.colorDarkTitle, topBarStyles.homeTitleContainer]}>{texts.nearbyBarbers}</Text>
                        <Pressable style={[topBarStyles.viewAllContainer, styles.w100, styles.alignRight]} onPress={() => {setTopCategoriesExpanded(nearbyBarbersExpanded);setNearbyBarbersExpanded(!nearbyBarbersExpanded)}}>
                            <Text style={[styles.fontPoppins, styles.fontSize18, styles.colorDarkTitle, styles.fontWeight400]} >{texts.viewAll}</Text>
                        </Pressable>
                    </View>
                    <ScrollView style={nearbyBarbersExpanded ? topBarStyles.homeListContainerExpanded : topBarStyles.homeListContainer} contentContainerStyle={[styles.alignCenter, styles.justifyCenter]}>
                        <Divider size={5 * absoluteHeight} />
                        {barberList && barberList.map((barber: BarberInfo, idx: number) => {
                            return (
                                <ListItem key={idx} barber={barber} />
                            );
                        })}
                        <Divider size={70 * absoluteHeight} />
                    </ScrollView>
                </View>
            </View>
            <Filter ref={filterRef} />
        </>
    );
}

import { ScrollView, View, Text, Image } from 'react-native';
import TopBar from '../components/TopBar';

import { styles } from '../styles/Main';
import { styles as topBarStyles } from '../styles/TopBar';
import { useEffect, useState } from 'react';
import { BarberInfo, getBarbersNearMe } from '../utils/ApiRequest';
import ListItem from '../components/ListItemBarbershop';
import Pressable from '../components/Pressable';

export default function Home() {
    const [barberList, setBarberList] = useState<BarberInfo[]>([]);
    const [topCategoriesExpanded, setTopCategoriesExpanded] = useState(true);
    const [nearbyBarbersExpanded, setNearbyBarbersExpanded] = useState(false);
    const texts = require("../langs/en.json");

    useEffect(() => {
        const fetchBarbers = async () => {
            const barbers: BarberInfo[] = await getBarbersNearMe();
            setBarberList(barbers);
        }

        fetchBarbers();
    }, []);

    return (
        <>
            <TopBar/>
            <View style={topBarStyles.homeContainer}>
                <View style={[styles.row, styles.w100, topCategoriesExpanded ? topBarStyles.topCategoriesContainerExpanded :  topBarStyles.topCategoriesContainer]}>
                    <Text style={[styles.fontPoppins, styles.fontWeight700, styles.fontSize18, styles.colorDarkTitle, topBarStyles.homeTitleContainer]}>{texts.topCategories}</Text>
                    <Pressable style={[topBarStyles.viewAllContainer, styles.w100, styles.alignRight]} onPress={() => {setNearbyBarbersExpanded(topCategoriesExpanded);setTopCategoriesExpanded(!topCategoriesExpanded)}}>
                        <Text style={[styles.fontPoppins, styles.fontSize18, styles.colorDarkTitle, styles.fontWeight400]} >{texts.viewAll}</Text>
                    </Pressable>
                    <View style={topCategoriesExpanded ? topBarStyles.categoriesContainerExpanded : topBarStyles.categoriesContainer}>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <Image style={[topBarStyles.categoryIcon, styles.alignCenter]} source={require('@assets/icons/haircut.png')} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.haircut}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <Image style={[topBarStyles.categoryIcon, styles.alignCenter]} source={require('@assets/icons/spa.png')} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.spa}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <Image style={[topBarStyles.categoryIcon, styles.alignCenter]} source={require('@assets/icons/creamBath.png')} />
                            </View>
                            <Text style={topBarStyles.categoryText}>{texts.creamBath}</Text>
                        </View>
                        <View>
                            <View style={topBarStyles.categoryIconContainer}>
                                <Image style={[topBarStyles.categoryIcon, styles.alignCenter]} source={require('@assets/icons/massage.png')} />
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
                        {barberList && barberList.map((barber: BarberInfo) => {
                            return (
                                <ListItem key={barber.id} barber={barber} />
                            );
                        })}
                    </ScrollView>
                </View>
            </View>
        </>
    );
}

import { ScrollView, View } from 'react-native';
import TopBar from '../components/TopBar';

import { styles } from '../styles/Main';
import { useEffect, useState } from 'react';
import { BarberInfo, getBarbersNearMe } from '../utils/ApiRequest';
import ListItem from '../components/ListItemBarbershop';

export default function Home() {
    const [barberList, setBarberList] = useState<BarberInfo[]>([]);

    useEffect(()=>{
        const fetchBarbers = async () => {
            const barbers: BarberInfo[] = await getBarbersNearMe();
            setBarberList(barbers);
        }

        fetchBarbers();
    },[]);

    return (
        <View style={styles.container}>
            <TopBar />
            <ScrollView contentContainerStyle={[styles.homeListContainer, styles.alignCenter, styles.justifyCenter]}>
                {barberList && barberList.map((barber: BarberInfo) => {
                    return (
                        <ListItem key={barber.id} barber={barber} />
                    );
                })}
            </ScrollView>
        </View>
    );
}

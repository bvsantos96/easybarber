import React, { useRef } from 'react';
import { View, Text } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useState } from 'react';
import { getAppointments } from '../utils/ApiRequest';
import ListAppointments from '../components/ListAppointments';
import PageList, { PageListRef } from '../components/PageList';
import AnimatedSwitch from '@components/AnimatedSwitch';

export default function Appointments() {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [resetSearch] = useState(false);
    const pageListRef = useRef<PageListRef<AppointmentInfo>>(null);
    const [upComming, setUpComming] = useState(true);
    const [nUpcomming, setNUpcomming] = useState(0);
    const [nPast, setNPast] = useState(0);

    const loadMoreLocations = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
        return await getAppointments(page, params);
    }

    return (
        <View style={styles.container}>
            <View style={styles.titleContainer}>
                <AnimatedSwitch
                    setSelected={setUpComming}
                    text1={`${texts.appointments.upcomming}(${nUpcomming})`} text2={`${texts.appointments.past}(${nPast})`} />
            </View>

            <View style={[styles.listContainer]}>
                <PageList<AppointmentInfo>
                    reset={resetSearch}
                    ref={pageListRef}
                    renderItem={({ item }: { item: AppointmentInfo }) =>
                        <ListAppointments key={item.id} appointment={item} />
                    }
                    requestFunction={loadMoreLocations} />
            </View>
        </View>
    );
}

import React, { useEffect, useRef } from 'react';
import { View } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useState } from 'react';
import { getAppointmentCount, getAppointments } from '../utils/ApiRequest';
import AppointmentItem from '../components/AppointmentItem';
import PageList, { PageListRef } from '../components/PageList';
import AnimatedSwitch from '@components/AnimatedSwitch';

export default function Appointments({ navigation }: PropNavigation) {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [resetSearch, _] = useState(false);
    const pageListRef = useRef<PageListRef<AppointmentInfo>>(null);
    const pageListUpcommingRef = useRef<PageListRef<AppointmentInfo>>(null);
    const [upComming, setUpComming] = useState(true);
    const [nUpcomming, setNUpcomming] = useState(0);
    const [nPast, setNPast] = useState(0);

    const loadUpcomming = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
        return await getAppointments(page, { ...params, future: true, activeOnly: true });
    }

    const loadPast = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
        return await getAppointments(page, { ...params, future: false, activeOnly: true });
    }

    useEffect(() => {
        getAppointmentCount().then((conts: AppointmentCounts) => {
            setNUpcomming(conts.upcomming);
            setNPast(conts.past);
        });
    }, [resetSearch]);

    const cancelAppointment = async (id: number) => {
        pageListUpcommingRef.current?.deleteItem(id);
        setNUpcomming(nUpcomming - 1);
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
                    key="upcomming"
                    style={upComming ? {} : { display: 'none' }}
                    reset={resetSearch}
                    ref={pageListUpcommingRef}
                    renderItem={({ item }: { item: AppointmentInfo }) =>
                        <AppointmentItem navigation={navigation} cancel={cancelAppointment} key={item.id} appointment={item} past={false} />
                    }
                    requestFunction={loadUpcomming} />
                <PageList<AppointmentInfo>
                    style={upComming ? { display: 'none' } : {}}
                    key="past"
                    reset={resetSearch}
                    ref={pageListRef}
                    renderItem={({ item }: { item: AppointmentInfo }) =>
                        <AppointmentItem navigation={navigation} key={item.id} appointment={item} past={true} />
                    }
                    requestFunction={loadPast} />
            </View>
        </View>
    );
}

import React, { useEffect, useRef } from 'react';
import { View } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useState } from 'react';
import { getAppointmentCount, getAppointments } from '../utils/ApiRequest';
import AppointmentItem from '../components/AppointmentItem';
import PageList, { PageListRef } from '../components/PageList';
import AnimatedSwitch from '@components/AnimatedSwitch';
import texts from '@lang/en.json';
import useAppointmentStore from 'storage/stores/AppointmentStore';

export default function Appointments({ navigation }: PropNavigation) {
    const styles = getStyles();
    const [resetSearch, setResetSearch] = useState(false);
    const pageListRef = useRef<PageListRef<AppointmentInfo>>(null);
    const pageListUpcommingRef = useRef<PageListRef<AppointmentInfo>>(null);
    const [upComming, setUpComming] = useState(true);
    const [nUpcomming, setNUpcomming] = useState(0);
    const [nPast, setNPast] = useState(0);
    const { updateAppointments, resetUpdateAppointments } = useAppointmentStore();

    useEffect(() => {
        if (updateAppointments) {
            resetUpdateAppointments();
            setResetSearch(!resetSearch);
        }
    }, [updateAppointments]);

    const loadUpcomming = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
        return await getAppointments(page, { ...params, future: true, activeOnly: true });
    }

    const loadPast = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
        return await getAppointments(page, { ...params, future: false });
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

    const MemoizedAppointmentItem = React.memo(AppointmentItem);
    const renderItemPast = ({ item }: { item: AppointmentInfo }) =>
        <MemoizedAppointmentItem navigation={navigation} key={item.id} appointment={item} past={true} />

    const renderItemFuture = ({ item }: { item: AppointmentInfo }) =>
        <MemoizedAppointmentItem navigation={navigation} cancel={cancelAppointment} key={item.id} appointment={item} past={false} />

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
                        renderItem={renderItemFuture}
                        requestFunction={loadUpcomming}
                    />
                    <PageList<AppointmentInfo>
                        key="past"
                        style={upComming ? { display: 'none' } : {}}
                        reset={resetSearch}
                        ref={pageListRef}
                        renderItem={renderItemPast}
                        requestFunction={loadPast}
                    />
            </View>
        </View>
    );
}

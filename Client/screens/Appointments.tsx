import React, { useRef } from 'react';
import { View, Text } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useState } from 'react';
import { getAppointments } from '../utils/ApiRequest';
import ListAppointments from '../components/ListAppointments';
import { AppointmentFilter, AppointmentInfo, IPage } from '../declarations';
import PageList, { PageListRef } from '../components/PageList';

export default function Home() {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [resetSearch] = useState(false);
    const pageListRef = useRef<PageListRef<AppointmentInfo>>(null);

    const loadMoreLocations = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
        return await getAppointments(page, params);
    }

    return (
        <View style={styles.container}>
            <View style={styles.titleContainer}>
                <Text style={styles.titleText}>{texts.appointments.upcomming}</Text>
            </View>

            <View style={styles.listContainer}>
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

// <ScrollView contentContainerStyle={styles.listContainer}>
//     {appointmentList && appointmentList.map((appointment: Appointment) => {
//         return (
//             <ListAppointments key={appointment.id} appointment={appointment} />
//         );
//     })}
// </ScrollView>

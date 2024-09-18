import { Text, ScrollView, View } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useEffect, useState } from 'react';
import { getAppointments } from '../utils/ApiRequest';
import ListAppointments from '../components/ListAppointments';
import React from 'react';
import { Appointment } from '../declarations';

export default function Home() {
    const texts = require("@lang/en.json");
    const styles = getStyles();
    const [appointmentList, setAppointmentList] = useState<Appointment[]>([]);

    useEffect(() => {
        const fetchBarbers = async () => {
            const appointments: Appointment[] = await getAppointments();
            setAppointmentList(appointments);
        }
        fetchBarbers();
    }, []);

    return (
        <View style={styles.container}>
            <View style={styles.titleContainer}>
                <Text style={styles.titleText}>{texts.appointments.upcomming}</Text>
            </View>
            <ScrollView contentContainerStyle={styles.listContainer}>
                {appointmentList && appointmentList.map((appointment: Appointment) => {
                    return (
                        <ListAppointments key={appointment.id} appointment={appointment} />
                    );
                })}
            </ScrollView>
        </View>
    );
}

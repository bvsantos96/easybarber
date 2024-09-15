import { Button, ScrollView, View } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useEffect, useState } from 'react';
import { getAppointments } from '../utils/ApiRequest';
import ListAppointments from '../components/ListAppointments';
import React from 'react';
import { Appointment } from '../declarations';
import useLocationStore from '../storage/stores/LocationStore';

export default function Home() {
    const styles = getStyles();
    const [appointmentList, setAppointmentList] = useState<Appointment[]>([]);

    useEffect(() => {
        const fetchBarbers = async () => {
            const appointments: Appointment[] = await getAppointments();
            setAppointmentList(appointments);
        }

        fetchBarbers();
    }, []);

    const {
        setRequestingLocationPermission
    } = useLocationStore();

    return (
        <View style={styles.container}>
            <Button title="Book an appointment" onPress={() => { setRequestingLocationPermission(true) }} />
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

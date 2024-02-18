import { ScrollView, View } from 'react-native';
import { getStyles } from '../styles/Appointments';
import { useEffect, useState } from 'react';
import { Appointment, getAppointments } from '../utils/ApiRequest';
import ListAppointments from '../components/ListAppointments';

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

    return (
        <View style={styles.container}>
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

import { ScrollView, View } from 'react-native';
import { styles } from '../styles/Main';
import { useEffect, useState } from 'react';
import { Appointment, getAppointments } from '../utils/ApiRequest';
import ListAppointments from '../components/ListAppointments';

export default function Home() {
    const [appointmentList, setAppointmentList] = useState<Appointment[]>([]);

    useEffect(()=>{
        const fetchBarbers = async () => {
            const appointments: Appointment[] = await getAppointments();
            setAppointmentList(appointments);
        }

        fetchBarbers();
    },[]);

    return (
        <View style={styles.container}>
            <ScrollView contentContainerStyle={[styles.homeListContainer, styles.alignCenter, styles.justifyCenter]}>
                {appointmentList && appointmentList.map((appointment: Appointment) => {
                    return (
                        <ListAppointments key={appointment.id} appointment={appointment} />
                    );
                })}
            </ScrollView>
        </View>
    );
}

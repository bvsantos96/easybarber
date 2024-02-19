import { LocationObject, getCurrentPositionAsync, Accuracy, PermissionResponse, requestForegroundPermissionsAsync } from 'expo-location';

async function getLocation(): Promise<void> {
    let { status }: PermissionResponse = await requestForegroundPermissionsAsync();
    if (status !== 'granted') {
        console.error('Permission to access location was denied');
        return;
    }

    try {
        let location: LocationObject = await getCurrentPositionAsync({ accuracy: Accuracy.High });
    } catch (error) {
        console.log(error);
    }
}

export type BarberInfo = {
    id: number,
    name: string,
    description: string,
    distance: number,
    rating: number,
    nVotes: number,
    photo: string,
}

export type Appointment = {
    id: number,
    name: string,
    from: string,
    to: string,
    photo: string,
}

export const getNearByBarbers = async (location: any): Promise<BarberInfo[]> => {
    return require("../assets/fakeAPI/nearBarbers.json");
}

export const getBarbersNearMe = async (): Promise<BarberInfo[]> => {
    return getNearByBarbers(getLocation());
}

export const getAppointments = async (): Promise<Appointment[]> => {
    return require("../assets/fakeAPI/appointments.json");
}

import comboboxes from "../assets/fakeAPI/comboboxes.json";
import { PickerItem } from '../components/Picker';

export const getCategories = async (): Promise<PickerItem[]> => {
    return comboboxes.categories;
}

export const getTimes = async ({from, to}:{from? :string, to?: string}) : Promise<PickerItem[]> => { 
    from = from || "08:00";
    to = to || "20:00";
    let startHour = parseInt(from.split(":")[0]);
    let startMinute = parseInt(from.split(":")[1]);
    let endHour = parseInt(to.split(":")[0]);
    let endMinute = parseInt(to.split(":")[1]);
    let times: PickerItem[] = [];
    for(let i = startHour; i < 20; i++){
        for(let j = startHour == i ? startMinute : 0; j < 60; j+=30){
            if(endHour <= i && endMinute <=j)
                return times;
            let time = `${i}:${j == 0 ? "00" : j}`;
            times.push({
                label: time,
                value: time
            });
            if(startHour == i && startMinute == j){
                j = startMinute + 30 > 60? 30 : 0;
            }
        }
    }

    return times;
}

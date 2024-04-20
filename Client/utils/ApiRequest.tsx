import { LocationObject, getCurrentPositionAsync, Accuracy, PermissionResponse, requestForegroundPermissionsAsync } from 'expo-location';
import AsyncStorage from '@react-native-async-storage/async-storage';
import langs from '../langs/en.json';

const apiUrl = process.env.EXPO_PUBLIC_API_URL;

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

export const getTimes = async ({ from, to }: { from?: string, to?: string }): Promise<PickerItem[]> => {
    from = from || "08:00";
    to = to || "20:00";
    let startHour = parseInt(from.split(":")[0]);
    let startMinute = parseInt(from.split(":")[1]);
    let endHour = parseInt(to.split(":")[0]);
    let endMinute = parseInt(to.split(":")[1]);
    let times: PickerItem[] = [];
    for (let i = startHour; i < 20; i++) {
        for (let j = startHour == i ? startMinute : 0; j < 60; j += 30) {
            if (endHour <= i && endMinute <= j)
                return times;
            let time = `${i}:${j == 0 ? "00" : j}`;
            times.push({
                label: time,
                value: time
            });
            if (startHour == i && startMinute == j) {
                j = startMinute + 30 > 60 ? 30 : 0;
            }
        }
    }

    return times;
}

export type Result = {
    success: boolean,
    message: string
}

const storeData = async (key: string, value: string) => {
    try {
        await AsyncStorage.setItem(key, value);
    } catch (e) {
        // saving error
    }
};

const getData = async (key: string): Promise<string | null> => {
    try {
        const value = await AsyncStorage.getItem(key);
        if (value !== null) {
            return value;
        }
        return null;
    } catch (e) {
        // error reading value
        return null;
    }
};

const removeData = async (key: string) => {
    try {
        await AsyncStorage.removeItem(key);
    } catch (e) {
        // error reading value
    }
}

const hasData = async (key: string): Promise<boolean> => {
    try {
        const value = await AsyncStorage.getItem(key);
        return value !== null;
    } catch (e) {
        return false;
    }
}

const request = async (url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed): Promise<Result> => {
    if (!apiUrl)
        return { success: false, message: langs.apiMessages.failed };
    if (url.startsWith("/"))
        url = url.substring(1);
    let separator = apiUrl.endsWith("/") ? "" : "/";
    let token = await getData("token");

    console.log(`${apiUrl}${separator}${url}`);
    console.log({
        method: method,
        ...(method !== "GET" && {
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                ...(token !== null && token !== undefined && { 'Authorization': `Bearer ${token}` })
            },
            body: JSON.stringify(body)
        })
    });


    return fetch(`${apiUrl}${separator}${url}`, {
        method: method,
        mode: 'cors',
        ...(method !== "GET" && {
            headers: {
                'Content-Type': 'application/json',
                ...(token !== null && token !== undefined && { 'Authorization': `Bearer ${token}` })
            },
            body: JSON.stringify(body)
        })
    }).then(async response => {
        if (response.status != 200 && response.status != 201) {
            try {
                const json = await response.json();
                if (json.responseMessage)
                    return { success: false, message: json.responseMessage };
                return { success: false, message: errorMessage };
            } catch (e) {
                return { success: false, message: response }
            }
        }
        else
            return { success: true, message: successMessage };
    }).catch(error => {
        console.log(error);
        return { success: false, message: errorMessage };
    });
}

const isValidNumberString = (input: string): boolean => {
    alert(input)
    const pattern = /^[-+]?\d*\.?\d+$/;
    return pattern.test(input);
}

const isValidPassword = (password: string): boolean => {
    // Check if the password meets the following criteria:
    // - At least 8 characters long
    // - Contains at least one uppercase letter
    // - Contains at least one lowercase letter
    // - Contains at least one digit
    const hasMinLength = password.length >= 8;
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasDigit = /\d/.test(password);

    return hasMinLength && hasUppercase && hasLowercase && hasDigit;
}

export const doLogin = async (countryCode: string, phone: string, password: string): Promise<Result> => {
    phone = phone.trim();
    const _countryCode = countryCode.startsWith('+') ? countryCode : `+${countryCode}`;
    if (!isValidNumberString(`${_countryCode}${phone}`))
        return { success: false, message: langs.apiMessages.invalidPhone };

    const result = await request("login", "POST", { countryMobile: _countryCode, mobile: phone, password }, langs.apiMessages.login.success, langs.apiMessages.login.failed);

    console.log(result)

    if (result.success)
        storeData("token", result.message);
    else
        alert(result.message);
    return result;
}

export const doRegister = async (countryCode: string, phone: string, password: string, confirmPassword: string, name: string): Promise<Result> => {
    phone = phone.trim();
    if (!isValidNumberString(phone))
        return { success: false, message: langs.apiMessages.invalidPhone };
    if (!isValidPassword(password))
        return { success: false, message: langs.apiMessages.register.invalidPassword };
    if (password != confirmPassword)
        return { success: false, message: langs.apiMessages.register.passwordMismatch };
    name = name.trim();
    if (name.length < 3)
        return { success: false, message: langs.apiMessages.register.invalidName };
    const result = await request("register", "POST", { countryMobile: countryCode, mobile: phone, password, name }, langs.apiMessages.register.success, langs.apiMessages.register.failed);
    if (result.success)
        doLogin(countryCode, phone, password);
    return result;
}

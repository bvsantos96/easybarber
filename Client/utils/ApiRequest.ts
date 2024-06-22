import AsyncStorage from '@react-native-async-storage/async-storage';
import langs from '../langs/en.json';
import { PickerItem } from '../components/Picker';
import { createPageable, parsePage } from './PageHandling';
import { downloadToDevice } from '../storage/StorageUtils';
import { Appointment, BarberInfo, ICategory, ILocation, IPage, IResult } from '../declarations';
import { getCachedLocation } from './Location';
import { getCurrentSelectedLocation } from '../storage/ApiLongTermStorage';

const apiUrl = process.env.EXPO_PUBLIC_API_URL;
const debugRequests = process.env.EXPO_PUBLIC_DEBUG_SERVER_REQUESTS?.toLowerCase() == "true";

export const getAppointments = async (): Promise<Appointment[]> => {
    return require("../assets/fakeAPI/appointments.json");
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

const storeData = async (key: string, value: string) => {
    try {
        await AsyncStorage.setItem(key, value);
    } catch (e) {
        // saving error
        alert(`Error saving data(${key},${value}): ${e}`);
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

export const getToken = async (): Promise<string | null> => {
    return getData("token");
}

export const apiUrlMaker = (url: string): string => {
    if (url.startsWith("/"))
        url = url.substring(1);
    if (!apiUrl) {
        console.error("API URL is not set");
        return "";
    }
    let separator = apiUrl.endsWith("/") ? "" : "/";
    return `${apiUrl}${separator}${url}`;
}

const stringRequest = async (url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed): Promise<string> => {
    const result = await request<string>(url, method, body, successMessage, errorMessage, true);
    return result.data ?? "";
}

const request = async<T>(url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed, stringResponse = false): Promise<IResult<T>> => {
    let _url = apiUrlMaker(url);
    if (_url.length <= 0)
        return { success: false, message: langs.apiMessages.failed };
    let token = await getToken();
    if (debugRequests) {
        console.log(_url);
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
    }

    return fetch(_url, {
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
        if (stringResponse && response.status == 200) {
            const text = await response.text();
            const _response = { success: true, message: text, data: text };
            if (debugRequests) {
                console.log(_response);
            }
            return _response;
        }
        const json = await response.json();
        if (response.status != 200 && response.status != 201) {
            if (json !== undefined && json !== null) {
                try {
                    if (json.responseMessage)
                        return { success: false, message: json.responseMessage };
                    return { success: false, message: errorMessage };
                } catch (e) {
                    return { success: false, message: response }
                }
            } else
                return { success: false, message: errorMessage };
        } else if (json !== undefined && json !== null) {
            const _response = {
                success: true,
                message: successMessage,
                ...(json.items ? { items: json.items } : { data: json.data })
            };

            if (debugRequests) {
                console.log(_response);
            }

            return _response;
        }
        return { success: true, message: successMessage };
    }).catch(error => {
        console.error(error);
        return { success: false, message: errorMessage };
    });
}

const isValidNumberString = (input: string): boolean => {
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

export const doLogin = async (countryCode: string, phone: string, password: string): Promise<IResult<any>> => {
    phone = phone.trim();
    const _countryCode = countryCode.startsWith('+') ? countryCode : `+${countryCode}`;
    if (!isValidNumberString(`${_countryCode}${phone}`))
        return { success: false, message: langs.apiMessages.invalidPhone };

    removeData("token");

    const result = await request("login", "POST", { countryMobile: _countryCode, mobile: phone, password }, langs.apiMessages.login.success, langs.apiMessages.login.failed, true);

    if (result.success)
        await storeData("token", result.message);
    else
        alert(result.message);
    return result;
}

export const doRegister = async (countryCode: string, phone: string, password: string, confirmPassword: string, name: string): Promise<IResult<any>> => {
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

const parsePathParams = (_path: string, params: Record<string, string | number | boolean>): string => {
    let first = true;
    for (const key in params) {
        if (params[key] === null || params[key] === undefined)
            continue;
        _path += `${first ? '?' : '&'}${key}=${params[key]}`;
        first = false;
    }
    return _path;
}

export const getNearByBarbers = async (page?: IPage<BarberInfo>, params?: Record<string, string | number | boolean>, location?: ILocation): Promise<IPage<BarberInfo> | undefined> => {
    if (page === undefined || page === null) {
        page = createPageable();
    } else if (!page.hasNextPage) {
        page.content = [];
        return page;
    }
    location = location ?? await getCurrentSelectedLocation();
    if (location === undefined || location === null) {
        // TODO: alerta para ativar localizacao
        return page;
    }
    if (params === undefined || params === null)
        params = {};
    if (!params.hasOwnProperty("latitude"))
        params["latitude"] = location.latitude;
    if (!params.hasOwnProperty("longitude"))
        params["longitude"] = location.longitude;
    if (!params.hasOwnProperty("page"))
        params["page"] = page.currentPage;
    if (!params.hasOwnProperty("size"))
        params["size"] = page.pageSize;
    const result: IResult<BarberInfo> = await request(parsePathParams("establishment/list", params), `GET`, null, langs.apiMessages.success, langs.apiMessages.failed);
    return result.items ? parsePage(result.items) : page;

}

export const getBarbersNearMe = async (page: IPage<BarberInfo>, params?: Record<string, string | number | boolean>): Promise<IPage<BarberInfo> | undefined> => {
    return await getNearByBarbers(page, params);
}

export const setNewLocation = async (location: ILocation): Promise<boolean> => {
    const response = await request("/location", "POST", location, langs.apiMessages.success, langs.apiMessages.failed, true);
    if (response.success) {
        return true;
    }
    throw new Error(langs.apiMessages.failed);
}

export const getLocations = async (): Promise<ILocation[]> => {
    const reponse = await request("/locations", "GET", null, langs.apiMessages.success, langs.apiMessages.failed);
    if (reponse.hasOwnProperty("items")) {
        for (const element of reponse?.items) {
            element.hash = `${element.latitude}${element.longitude}`;
        }
        return reponse.items;
    }
    throw new Error(langs.apiMessages.failed);
}

export const getCategories = async (): Promise<ICategory[]> => {
    const response = await request("/service/types", "GET", null, langs.apiMessages.success, langs.apiMessages.failed);
    if (response.hasOwnProperty("items")) {
        // TODO: save the retrieve images into device storage and replace the imageUrls with the local paths
        for (const element of response?.items) {
            if (element.hasOwnProperty("imageURL")) {
                element.imageURL = await downloadToDevice(element.name, apiUrlMaker(element.imageURL));
            }
        }
        return response.items;
    }
    throw new Error(langs.apiMessages.failed);
}

export const getApiVersion = async (): Promise<string> => {
    const response: string = await stringRequest("version", "GET", null, langs.apiMessages.success, langs.apiMessages.failed);
    if (response.length > 0)
        return response;
    throw new Error(langs.apiMessages.failed);
}

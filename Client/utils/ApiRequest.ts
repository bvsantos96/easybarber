import AsyncStorage from '@react-native-async-storage/async-storage';
import langs from '../langs/en.json';
import { PickerItem } from '../components/Picker';
import { createPageable, parsePage } from './PageHandling';
import { downloadToDevice } from '../storage/StorageUtils';
import { AppointmentFilter, AppointmentInfo, EmployeeInfo, EstablishmentInfo, ICategory, ILocation, IPage, IResult } from '../declarations';
import { API_URL, DEBUG_SERVER_REQUESTS } from './EnvVariables';
import { LOCATIONS_STORAGE_KEY, TOKEN_STORAGE_KEY } from './Constants';
import { getSelectedLocation } from './Location';
import useLocationStore from '../storage/stores/LocationStore';
import { Alert, Banner } from '../components/Alert';
import { ALERT_TYPE } from 'react-native-alert-notification';

import texts from '../langs/en.json';

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
        console.error(`Error saving data(${key},${value}): ${e}`);
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
    return getData(TOKEN_STORAGE_KEY);
}

export const apiUrlMaker = (url: string): string => {
    if (url.startsWith("/"))
        url = url.substring(1);
    if (!API_URL) {
        Alert({ type: ALERT_TYPE.DANGER, title: texts.errors.apiDownTitle, message: texts.errors.apiDown });
        return "";
    }
    let separator = API_URL.endsWith("/") ? "" : "/";
    return `${API_URL}${separator}${url}`;
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
    if (DEBUG_SERVER_REQUESTS) {
        console.log(_url);
        console.log({
            method: method,
            mode: 'cors',
            headers: {
                ...(token && { 'Authorization': `Bearer ${token}` }),
                ...(method !== "GET" && { 'Content-Type': 'application/json' }),
            },
            ...(method !== "GET" && { body: JSON.stringify(body) }),
        });
    }

    return fetch(_url, {
        method: method,
        mode: 'cors',
        headers: {
            ...(token && { 'Authorization': `Bearer ${token}` }),
            ...(method !== "GET" && { 'Content-Type': 'application/json' }),
        },
        ...(method !== "GET" && { body: JSON.stringify(body) }),
    }).then(async response => {
        let json;
        if (stringResponse) {
            const text = await response.text();
            if (response.status == 200) {
                const _response = { success: true, message: text, data: text };
                if (DEBUG_SERVER_REQUESTS) {
                    console.log(_response);
                }
                return _response;
            }
            json = text;
        } else {
            json = await response.json();
        }
        if (response.status != 200 && response.status != 201) {
            if (json !== undefined && json !== null) {
                try {
                    if (json.responseMessage) {
                        Banner({ type: ALERT_TYPE.WARNING, message: json.responseMessage });
                        return { success: false, message: json.responseMessage };
                    }
                    Banner({ type: ALERT_TYPE.WARNING, message: errorMessage });
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

            if (DEBUG_SERVER_REQUESTS) {
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
    if (!isValidNumberString(`${_countryCode}${phone}`)) {
        return { success: false, message: langs.apiMessages.invalidPhone };
    }

    removeData(TOKEN_STORAGE_KEY);
    removeData(LOCATIONS_STORAGE_KEY);

    const result = await request("login", "POST", { countryMobile: _countryCode, mobile: phone, password }, langs.apiMessages.login.success, langs.apiMessages.login.failed, true);

    if (result.success) {
        await storeData(TOKEN_STORAGE_KEY, result.message);
    } else {
        Banner({ type: ALERT_TYPE.WARNING, title: "", message: result.message });
    }
    return result;
}

export const doRegister = async (countryCode: string, phone: string, password: string, confirmPassword: string, name: string): Promise<IResult<any>> => {
    phone = phone.trim();
    if (!isValidNumberString(phone))
        return { success: false, message: langs.apiMessages.invalidPhone };
    if (password != confirmPassword)
        return { success: false, message: langs.apiMessages.register.passwordMismatch };
    if (!isValidPassword(password))
        return { success: false, message: langs.apiMessages.register.invalidPassword };
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

export const pageGet = async <T>(url: string, page?: IPage<T>, params?: Record<string, string | number | boolean>): Promise<IPage<T> | undefined> => {
    if (page === undefined || page === null) {
        page = createPageable();
    } else if (!page.hasNextPage) {
        page.content = [];
        return page;
    }
    if (params === undefined || params === null)
        params = {};
    if (!params.hasOwnProperty("page"))
        params["page"] = page.currentPage;
    if (!params.hasOwnProperty("size"))
        params["size"] = page.pageSize;
    const result = await request<T>(parsePathParams(url, params), `GET`, null, langs.apiMessages.success, langs.apiMessages.failed);
    return result.items ? parsePage<T>(result.items) : page;
}

export const getLocationsRequest = async (page?: IPage<ILocation>, params?: Record<string, string | number | boolean>): Promise<IPage<ILocation> | undefined> => {
    if (params === undefined || params === null) {
        params = {};
    }
    params.sort = "selected,desc&sort=id,desc";
    return await pageGet<ILocation>("locations", page, params);
}

export const getLocationList = async (page: IPage<ILocation>, params?: Record<string, string | number | boolean>): Promise<IPage<ILocation> | undefined> => {
    const {
        locations,
        hasMoreLocations
    } = useLocationStore.getState();

    if (!hasMoreLocations) {
        page.hasNextPage = false;
        return page;
    }

    if (page && page.content.length === 0 && page.currentPage === 0 && locations.length > 0) {
        page.content = locations;
        page.totalElements = locations.length;
        page.currentPage = 1;
        page.pageSize = locations.length;
        page.hasNextPage = true;
        page.hasPreviousPage = false;
        return page;
    }
    const _locations = await getLocationsRequest(page, params);

    if (_locations?.hasNextPage === false) {
        useLocationStore.setState({ hasMoreLocations: false });
    }

    return _locations;
}

export const getEmployee = async (id: number): Promise<EmployeeInfo | undefined> => {
    const result = await request<EmployeeInfo>(`employee/${id}`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed);
    if (result.success && result.data !== undefined && result.data !== null) {
        return result.data;
    }
    return undefined;
}


export const getAppointments = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter): Promise<IPage<AppointmentInfo> | undefined> => {
    if (params === undefined || params === null)
        params = {
            future: true,
            activeOnly: true
        };
    params.sort = "date,time";
    return await pageGet<AppointmentInfo>("/appointment/list", page, params);
}

export const getNearByBarbers = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>, location?: ILocation): Promise<IPage<EstablishmentInfo> | undefined> => {
    location = location ?? await getSelectedLocation();
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
    return await pageGet<EstablishmentInfo>("establishment/list", page, params);
}

export const getBarbersNearMe = async (page: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>): Promise<IPage<EstablishmentInfo> | undefined> => {
    return await getNearByBarbers(page, params);
}

export const setNewLocation = async (location: ILocation): Promise<number> => {
    const response = await request<number>("/location", "POST", location, langs.apiMessages.success, langs.apiMessages.failed, true);
    if (response.success && response.data !== undefined && response.data !== null) {
        return response.data;
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

export const getMobileCode = async (mobileNr: string): Promise<boolean> => {
    const response = await request("/sms/confirmation", "POST", { phoneNr: mobileNr }, langs.apiMessages.success, langs.apiMessages.failed, true);
    if (response.success) {
        return true;
    }
    return false;
}

export const confirmMobileCode = async (mobileNr: string, confirmationCode: string): Promise<boolean> => {
    const response = await request("/sms/confirm", "POST", { phoneNr: mobileNr, confirmationCode: confirmationCode }, langs.apiMessages.success, langs.apiMessages.failed, true);
    if (response.success) {
        return true;
    }
    return false;
}

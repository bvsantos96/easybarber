import AsyncStorage from '@react-native-async-storage/async-storage';
import langs from '../langs/en.json';
import { PickerItem } from '../components/Picker';
import { createPageable, parsePage } from './PageHandling';
import { downloadToDevice } from '../storage/StorageUtils';
import { API_URL, DEBUG_SERVER_REQUESTS } from './EnvVariables';
import { LOCATIONS_STORAGE_KEY, TOKEN_STORAGE_KEY } from './Constants';
import { Alert, Banner } from '../components/Alert';
import { ALERT_TYPE } from 'react-native-alert-notification';
import useLocationStore from '../storage/stores/LocationStore';

import texts from '../langs/en.json';
import { ResponseType } from '../enums';

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
        return value;
    } catch (e) {
        console.error(`Error reading data(${key}): ${e}`);
        return null;
    }
};

const removeData = async (key: string) => {
    try {
        await AsyncStorage.removeItem(key);
    } catch (e) {
        console.error(`Error removing data(${key}): ${e}`);
        // error reading value
    }
}

export const getToken = async (): Promise<string | null> => {
    return await getData(TOKEN_STORAGE_KEY);
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
    const result = await request<string>(url, method, body, successMessage, errorMessage, ResponseType.STRING);
    return result.data ?? "";
}

const request = async<T>(url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed, responseType: ResponseType): Promise<IResult<T>> => {
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
        let data;
        try {
            if (responseType == ResponseType.STRING) {
                data = await response.text()
            } else {
                data = await response.json();
            }
        } catch (e) {
            console.error(e);
        }

        if (response.status != 200 && response.status != 201) {
            try {
                if (data !== undefined && data !== null) {
                    try {
                        if (data.responseMessage) {
                            Banner({ type: ALERT_TYPE.WARNING, message: data.responseMessage });
                            return { success: false, message: data.responseMessage };
                        }
                        Banner({ type: ALERT_TYPE.WARNING, message: errorMessage });
                        return { success: false, message: errorMessage };
                    } catch (e) {
                        return { success: false, message: response }
                    }
                } else
                    return { success: false, message: errorMessage };
            } catch (e) {
                return { success: false, message: errorMessage };
            }
        } else if (data !== undefined && data !== null) {
            let _response;
            switch (responseType) {
                case ResponseType.STRING:
                    _response = { success: true, message: data, data: data };
                    break;
                case ResponseType.OBJECT:
                    _response = { success: true, message: data, data: data };
                    break;
                case ResponseType.LIST:
                    _response = {
                        success: true,
                        message: successMessage,
                        ...(data.items ? { items: data.items } : { data: data.data })
                    };
                    break;
                case ResponseType.FULL_LIST:
                    _response = {
                        success: true,
                        message: successMessage,
                        ...(data.items ? { data: data.items } : { data: data.data })
                    };
                    break;
                default:
                    _response = { success: true, message: successMessage, data: data };
                    break;
            }

            if (DEBUG_SERVER_REQUESTS) {
                console.log(_response);
            }

            return _response;
        }
        return { success: true, message: successMessage };
    }).catch(error => {
        console.log(error);
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

    const result = await request("login", "POST", { countryMobile: _countryCode, mobile: phone, password }, langs.apiMessages.login.success, langs.apiMessages.login.failed, ResponseType.STRING);

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
    const result = await request("register", "POST", { countryMobile: countryCode, mobile: phone, password, name }, langs.apiMessages.register.success, langs.apiMessages.register.failed, ResponseType.LIST);
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
    const result = await request<T>(parsePathParams(url, params), `GET`, null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
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

export const getImageList = async (urlPrefix: string, page: IPage<IImage>, params?: Record<string, string | number | boolean>): Promise<IPage<IImage> | undefined> => {
    return await pageGet<IImage>(`${urlPrefix}/images`, page, params);
}

const getItemsFromRequest = <T>(result: IResult<T>): T => {
    if (result.success) {
        if (result.items !== undefined && result.items !== null) {
            return result.items as T;
        } else if (result.data !== undefined && result.data !== null) {
            return result.data as T;
        }
    }
    throw new Error(langs.apiMessages.failed);
}

export const getEstablishmentCats = async (id: number): Promise<number[] | undefined> => {
    const result = await request<number[]>(`establishment/${id}/servicetypes`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    return getItemsFromRequest(result);
}

export const getUnavailableDates = async (establishmentId: number, serviceId: number, employeeId: number, year: number, month: number, startHour: string): Promise<string[]> => {
    const params = {
        establishmentId: establishmentId,
        serviceId: serviceId,
        ...(employeeId == 0 ? {} : { employeeId: employeeId }),
        available: false,
        startHour: startHour,
    };
    const url = parsePathParams(`schedules/availabledays/year/${year}/month/${month}`, params);
    const result = await request<string[]>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest<string[]>(result);
}

export const setAppointment = async (appointment: Appointment): Promise<boolean> => {
    const result = await request<number>("appointment", "POST", appointment, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    try {
        getItemsFromRequest<number>(result);
        return true;
    } catch (e) {
        return false;
    }
}

export const getAvailability = async (establishmentId: number, serviceId: number, employeeId: number, date: string, startHour: string): Promise<TimeSlots> => {
    const params = {
        establishmentId: establishmentId,
        serviceId: serviceId,
        from: date,
        startHour: startHour,
        ...(employeeId == 0 ? {} : { employeeId: employeeId }),
    };
    const url = parsePathParams("schedules/day", params);
    const result = await request<TimeSlots>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest<TimeSlots>(result);
}

export const getEstablishmentServiceEmployees = async (establishmentId: number, serviceId: number): Promise<ImageEntity[]> => {
    const result = await request<ImageEntity[]>(`establishment/${establishmentId}/service/${serviceId}/employees`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    return getItemsFromRequest<ImageEntity[]>(result);
}

export const getEstablishmentServices = async (establishementId: number): Promise<ServiceInfo[]> => {
    const result = await request<ServiceInfo[]>(`establishment/${establishementId}/services/list`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    return getItemsFromRequest<ServiceInfo[]>(result);
}

export const getEstablishmentDetails = async (id: number): Promise<EstablishmentDetail | undefined> => {
    const result = await request<EstablishmentDetail>(`establishment/${id}/details`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest(result);
}

export const getEmployee = async (id: number): Promise<EmployeeInfo | undefined> => {
    const result = await request<EmployeeInfo>(`employee/${id}`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest(result);
}

export const getAppointmentCount = async (): Promise<AppointmentCounts> => {
    return getItemsFromRequest<AppointmentCounts>(await request<AppointmentCounts>(`appointment/count`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT));
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
    if (params === undefined || params === null)
        params = {};
    if (location === undefined || location === null) {
        return await pageGet<EstablishmentInfo>("establishment/list", page, params);
    }
    if (!params.hasOwnProperty("latitude"))
        params["latitude"] = location.latitude;
    if (!params.hasOwnProperty("longitude"))
        params["longitude"] = location.longitude;
    return await pageGet<EstablishmentInfo>("establishment/list", page, params);
}

export const setNewLocation = async (location: ILocation): Promise<number> => {
    const response = await request<number>("/location", "POST", location, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.STRING);
    if (response.success && response.data !== undefined && response.data !== null) {
        return response.data;
    }
    throw new Error(langs.apiMessages.failed);
}

export const getCategories = async (): Promise<ICategory[]> => {
    const response = await request<ICategory[]>("/service/types", "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.FULL_LIST);
    if (response.hasOwnProperty("data") && response.data !== undefined && response.data !== null) {
        // TODO: save the retrieve images into device storage and replace the imageUrls with the local paths
        for (const element of response.data) {
            if (element.hasOwnProperty("imageURL")) {
                element.imageURL = await downloadToDevice(element.name, apiUrlMaker(element.imageURL));
            }
        }
        return response.data;
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
    const response = await request("/sms/confirmation", "POST", { phoneNr: mobileNr }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.STRING);
    if (response.success) {
        return true;
    }
    return false;
}

export const confirmMobileCode = async (mobileNr: string, confirmationCode: string): Promise<boolean> => {
    const response = await request("/sms/confirm", "POST", { phoneNr: mobileNr, confirmationCode: confirmationCode }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.STRING);
    if (response.success) {
        return true;
    }
    return false;
}

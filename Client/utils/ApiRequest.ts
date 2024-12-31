import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';
import * as LocalAuthentication from 'expo-local-authentication';

import langs from '../langs/en.json';
import { PickerItem } from '../components/Picker';
import { createPageable, parsePage } from './PageHandling';
import { downloadToDevice } from '../storage/StorageUtils';
import { API_URL, DEBUG_SERVER_REQUESTS } from './EnvVariables';
import { FIRST_TIME, LOCATIONS_STORAGE_KEY, MOBILE_INFORMATION, SECURE_STORAGE_LOGIN_KEY, TOKEN_STORAGE_KEY } from './Constants';
import useLocationStore from '../storage/stores/LocationStore';
import { ResponseType } from '../enums';
import { twoDigits } from './Utils';
import useAlertStore from 'storage/stores/AlertStore';
import { AlertType } from '@components/Alert';
import useAppointmentStore from 'storage/stores/AppointmentStore';
import useFavoriteStore from 'storage/stores/FavoriteStore';
import useAuthStore from 'storage/stores/AuthStore';

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

const setMobileInformation = async (countryCode: string, phone: string) => {
    await storeData(MOBILE_INFORMATION, JSON.stringify({
        countryCode: countryCode,
        phone: phone
    }));
}

export const getMobileInformation = async (): Promise<{ countryCode: string, phone: string } | null> => {
    const mobileInformation = await getData(MOBILE_INFORMATION);
    if (mobileInformation === null) {
        return null;
    }
    return JSON.parse(mobileInformation);
}

export const deleteMobileInformation = async () => {
    await removeData(MOBILE_INFORMATION);
}

const _setToken = async (token: string | null | undefined) => {
    const { setToken } = useAuthStore.getState();

    if (token !== null && token !== undefined) {
        setToken(token);
        await storeData(TOKEN_STORAGE_KEY, token);
    }
}

export const getToken = async (): Promise<string | null> => {
    const { token } = useAuthStore.getState();
    if (token) {
        return token;
    }
    return await getData(TOKEN_STORAGE_KEY);
}

export const deleteToken = async () => {
    await removeData(TOKEN_STORAGE_KEY);
}

export const isFirstTime = async (): Promise<boolean> => {
    const firstTime = await getData(FIRST_TIME);
    const isFirstTime: boolean = (firstTime !== null);
    if (isFirstTime) {
        storeData(FIRST_TIME, "false");
    }
    return isFirstTime;
}

export const apiUrlMaker = (url: string): string => {
    if (url.startsWith("/"))
        url = url.substring(1);

    let separator = API_URL?.endsWith("/") ? "" : "/";
    return `${API_URL}${separator}${url}`;
}

const stringRequest = async (url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed): Promise<string> => {
    const result = await request<string>(url, method, body, successMessage, errorMessage, ResponseType.STRING);
    return result.data ?? "";
}

const request = async<T>(url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed, responseType: ResponseType): Promise<IResult<T>> => {
    return _request<T>(url, method, body, successMessage, errorMessage, responseType, true);
}

const _request = async<T>(url: string, method: string, body: any, successMessage: string = langs.apiMessages.success, errorMessage: string = langs.apiMessages.failed, responseType: ResponseType, first: boolean): Promise<IResult<T>> => {
    const { toggleDoLogout } = useAuthStore.getState();
    const { alert } = useAlertStore.getState();

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
                ...(method !== "GET" && body && { 'Content-Type': 'application/json' }),
            },
            ...(method !== "GET" && body && { body: JSON.stringify(body) }),
        });
    }

    return fetch(_url, {
        method: method,
        mode: 'cors',
        headers: {
            ...(token && { 'Authorization': `Bearer ${token}` }),
            ...(method !== "GET" && body && { 'Content-Type': 'application/json' }),
        },
        ...(method !== "GET" && body && { body: JSON.stringify(body) }),
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
                if (response.status == 401 && first) {
                    if (await refreshToken()) {
                        return _request<T>(url, method, body, successMessage, errorMessage, responseType, false);
                    } else {
                        if (token !== null) {
                            toggleDoLogout();
                        }
                        return { success: false, message: langs.apiMessages.unauthorized };
                    }
                }
                if (data !== undefined && data !== null) {
                    if (data.responseMessage) {
                        alert({ type: AlertType.Error, message: data.responseMessage, buttonText: langs.dismiss });
                        return { success: false, message: data.responseMessage, data: data.value };
                    }
                    return { success: false, message: errorMessage, data: data };
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
                case ResponseType.VALUE:
                    _response = { success: true, message: successMessage, data: data?.value };
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

            _setToken(response?.headers?.get("Authorization")?.split(" ")[1]);
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

const validateBiometricUser = async (): Promise<boolean> => {
    const hasHardware = await LocalAuthentication.hasHardwareAsync();
    const isEnrolled = await LocalAuthentication.isEnrolledAsync();

    if (!hasHardware || !isEnrolled) {
        return false;
    }

    const authResult = await LocalAuthentication.authenticateAsync({
        promptMessage: langs.saveLogin,
        fallbackLabel: langs.usePasscode,
    });

    return authResult.success;
}

export const doLogin = async (countryCode: string, phone: string, password: string): Promise<IResult<any>> => {
    const {
        alert
    } = useAlertStore.getState();

    phone = phone.trim();
    phone = phone.replace(/\s/g, '');
    const _countryCode = parseCountryCode(countryCode);
    if (!isValidNumberString(`${_countryCode}${phone}`)) {
        return { success: false, message: langs.apiMessages.invalidPhone };
    }

    removeData(TOKEN_STORAGE_KEY);
    removeData(LOCATIONS_STORAGE_KEY);

    const result = await request("login", "POST", { countryMobile: _countryCode, mobile: phone, password }, langs.apiMessages.login.success, langs.apiMessages.login.failed, ResponseType.STRING);

    if (result.success) {
        const user = await loadSecureTokenFromStorage();
        if (user === null || (user.countryCode !== countryCode || user.phone !== phone || user.password !== password)) {
            alert({
                type: AlertType.Info,
                message: langs.saveLogin,
                onPress: async () => {
                    if (await validateBiometricUser()) {
                        SecureStore.setItemAsync(SECURE_STORAGE_LOGIN_KEY, createSecureToken(countryCode, phone, password));
                    }
                },
                buttonText: langs.enable,
                onPress2: () => { },
                buttonText2: langs.notNow
            });
        }
        setMobileInformation(countryCode, phone);
        _setToken(result.message);
    }

    return result;
}

export const validateRegister = async (countryCode: string, phone: string, password: string, confirmPassword: string, name: string): Promise<IResult<any>> => {
    phone = phone.trim();
    phone = phone.replace(/\s/g, '');
    const _countryCode = parseCountryCode(countryCode);
    if (name.length < 3) {
        return { success: false, message: langs.apiMessages.register.invalidName };
    }
    if (!isValidNumberString(`${_countryCode}${phone}`)) {
        return { success: false, message: langs.apiMessages.invalidPhone };
    }
    if (password != confirmPassword)
        return { success: false, message: langs.apiMessages.register.passwordMismatch };
    if (!isValidPassword(password))
        return { success: false, message: langs.apiMessages.register.invalidPassword };
    return { success: true, message: "" };
}

export const doRegister = async (countryCode: string, phone: string, password: string, confirmPassword: string, name: string, confirmationCode: string): Promise<IResult<any>> => {
    phone = phone.trim();
    validateRegister(countryCode, phone, password, confirmPassword, name);
    name = name.trim();
    if (name.length < 3)
        return { success: false, message: langs.apiMessages.register.invalidName };
    const result = await request("register", "POST", { countryMobile: countryCode, mobile: phone, password, name, confirmationCode }, langs.apiMessages.register.success, langs.apiMessages.register.failed, ResponseType.LIST);
    if (result.success)
        await doLogin(countryCode, phone, password);
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
    if (await getToken() === null) {
        return undefined;
    }
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

    if (await getToken() === null) {
        return undefined;
    }

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
    throw new Error(result.message ?? langs.apiMessages.failed);
}

export const getEstablishmentCats = async (id: number): Promise<number[] | undefined> => {
    const result = await request<number[]>(`establishment/${id}/servicetypes`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    return getItemsFromRequest(result);
}

export const getUnavailableDates = async (establishmentId: number, serviceId: number, employeeId: number, year: number, month: number, startHour: string): Promise<string[]> => {
    const params = {
        establishmentId: establishmentId,
        establishmentServiceId: serviceId,
        ...(employeeId == 0 ? {} : { establishmentStaffId: employeeId }),
        available: false,
        startHour: startHour,
    };
    const url = parsePathParams(`schedules/availabledays/year/${year}/month/${month}`, params);
    const result = await request<string[]>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest<string[]>(result);
}

export const setAppointment = async (appointment: AppointmentCreate): Promise<string> => {
    const result = await request<number>("appointment", "POST", appointment, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    try {
        getItemsFromRequest<number>(result);
        return "";
    } catch (e: any) {
        return e.message;
    }
}

export const getDynamicSlots = async (establishmentId: number, serviceId: number, employeeId: number, date: string, startHour: string): Promise<String[]> => {
    const params = {
        establishmentId: establishmentId,
        establishmentServiceId: serviceId,
        from: date,
        startHour: startHour,
        ...(employeeId == 0 ? {} : { establishmentStaffId: employeeId }),
    };
    const url = parsePathParams("service/list/dynamicprices/day", params);
    const result = await request<String[]>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    try {
        return getItemsFromRequest<String[]>(result);
    } catch (e) {
        return [];
    }
}

export const getAvailability = async (establishmentId: number, serviceId: number, employeeId: number, date: string, startHour: string): Promise<TimeSlots> => {
    const params = {
        establishmentId: establishmentId,
        establishmentServiceId: serviceId,
        from: date,
        startHour: startHour,
        ...(employeeId == 0 ? {} : { establishmentStaffId: employeeId }),
    };
    const url = parsePathParams("schedules/day", params);
    const result = await request<TimeSlots>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    try {
        return getItemsFromRequest<TimeSlots>(result);
    } catch (e) {
        return {
            slots: []
        };
    }
}

export const getEstablishmentServiceEmployees = async (establishmentId: number, establishmentServiceId: number, date?: string, time?: string): Promise<EmployeeEntity[]> => {
    const dateTime = `${date}T${time}`;
    const result = await request<EmployeeEntity[]>(`establishment/${establishmentId}/service/${establishmentServiceId}/employees${(date && time) ? "?date=" + dateTime : ""}`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    return getItemsFromRequest<EmployeeEntity[]>(result);
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
    if (await getToken() === null) {
        return {
            upcomming: 0,
            past: 0
        }
    }
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
    if (await getToken() === null) {
        return -1;
    }
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
            if (element.hasOwnProperty("imageURL") && element.imageURL !== undefined && element.imageURL !== null && element.imageURL.length > 0) {
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

const parseCountryCode = (countryCode: string): string => {
    if (countryCode.startsWith('+'))
        return countryCode;
    return `+${countryCode}`;
}

export const getMobileCode = async (phoneCountryCode: string, phoneNr: string): Promise<number | undefined> => {
    const response = await request<number>("/sms/confirmation", "POST", { phoneNr: phoneNr, phoneCountryCode: parseCountryCode(phoneCountryCode) }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.VALUE);

    if (response.success) {
        return response.data;
    }
    return response.data
}

export const confirmMobileCode = async (phoneNr: string, confirmationCode: string): Promise<boolean> => {
    const response = await request("/sms/confirm", "POST", { phoneNr: parseCountryCode(phoneNr), confirmationCode: confirmationCode }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.STRING);
    if (response.success) {
        return true;
    }
    return false;
}

export const getMobileCodeResetPwd = async (phoneCountryCode: string, phoneNr: string): Promise<number | undefined> => {
    const response = await request<number>("/sms/resetpwd", "POST", { phoneNr: phoneNr, phoneCountryCode: parseCountryCode(phoneCountryCode) }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.VALUE);
    if (response.success) {
        return response.data;
    }
    return response.data;
}

export const resetPwdRQ = async (phoneNr: string, confirmationCode: string, password: string, confirmPassword: string): Promise<boolean> => {
    if (password != confirmPassword)
        return false;

    const response = await request("/pwd/reset", "PUT", { phoneNr: parseCountryCode(phoneNr), confirmationCode: confirmationCode, newPassword: password }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.STRING);
    if (response.success) {
        return true;
    }
    return false;
}

export const cancelAppointment = async (id: number, reason = ""): Promise<boolean> => {
    const response = await request('/appointment/cancel', "PUT", { id, reason }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    if (response.success) {
        return true;
    }
    return false;
}

export const getNowHourAndMinutes = () => {
    const today = new Date();
    return twoDigits(today.getHours()) + ":" + twoDigits(today.getMinutes());
}

export const getStartingHour = (today: Date, date: string): string => {
    const todayHourAndMinute = getNowHourAndMinutes();
    return date == today.toISOString().split('T')[0] ? todayHourAndMinute : "00:01";
}

const createSecureToken = (countryCode: string, phone: string, password: string) => {
    return JSON.stringify({ countryCode, phone, password });
}

const loadSecureToken = (token: string) => {
    const loginInfo: LoginInfo = JSON.parse(token);
    return loginInfo;
}

const loadSecureTokenFromStorage = async (): Promise<LoginInfo | null> => {
    try {
        if (!(await validateBiometricUser())) {
            return null;
        }
        const secureLoginString = await SecureStore.getItemAsync(SECURE_STORAGE_LOGIN_KEY);
        if (secureLoginString === null)
            return null;
        const secureLoginInfo = loadSecureToken(secureLoginString);
        return { countryCode: secureLoginInfo.countryCode, phone: secureLoginInfo.phone, password: secureLoginInfo.password };
    } catch (error) {
        return null;
    }
}

export const refreshToken = async (): Promise<boolean> => {
    try {
        const secureLoginInfo = await loadSecureTokenFromStorage();
        if (secureLoginInfo === null)
            return false;
        doLogin(secureLoginInfo.countryCode, secureLoginInfo.phone, secureLoginInfo.password);
        return true;
    } catch (error) {
        console.error('Failed to refresh token:', error);
        return false;
    }
}

export const appointmentFeedback = async (appointmentId: number, rating: number): Promise<boolean> => {
    const response = await request(`/appointment/${appointmentId}/feedback/${rating}`, "POST", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.NONE);
    if (response.success) {
        return true;
    }
    return false;
}

export const makeRequest = async (url: string, method: string = "GET"): Promise<boolean> => {
    const response = await request(url, method, null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.NONE);
    if (response.success) {
        return true;
    }
    return false;
}

export const getFavoriteIds = async (): Promise<number[]> => {
    if (await getToken() === null) {
        return [];
    }
    const response = await request<number[]>("/favorites/establishments/ids", "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST);
    const favorites = getItemsFromRequest(response);
    const { setFavorites } = useFavoriteStore.getState();
    setFavorites(favorites);
    return favorites;
}

export const isFavorite = async (id: number): Promise<boolean> => {
    if (await getToken() === null) {
        return false;
    }
    const { favorites } = useFavoriteStore.getState();
    let _favorites = favorites;
    if (favorites === undefined || favorites === null) {
        _favorites = await getFavoriteIds();
    }
    return _favorites?.includes(id) || false;
}

export const getFavorites = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>, location?: ILocation): Promise<IPage<EstablishmentInfo> | undefined> => {
    if (await getToken() === null) {
        return undefined;
    }
    if (params === undefined || params === null)
        params = {};
    if (location === undefined || location === null) {
        return await pageGet<EstablishmentInfo>("/favorites/establishments", page, params);
    }
    if (!params.hasOwnProperty("latitude"))
        params["latitude"] = location.latitude;
    if (!params.hasOwnProperty("longitude"))
        params["longitude"] = location.longitude;
    return await pageGet<EstablishmentInfo>("/favorites/establishments", page, params);
}

export const requestFeedback = async (): Promise<void> => {
    const {
        alert
    } = useAlertStore.getState();
    const response = await request<Feedback>("/appointments/feedback", "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    if (response.success) {
        try {
            if (response.data !== undefined && response.data !== null && response.data?.id !== undefined && response.data?.id !== null && response.data?.id as number > 0) {
                const message = langs.appointments.feedback.replace("{employee}", response.data?.employeeName || "").replace("{establishment}", response.data?.establishmentName || "");
                alert({
                    type: AlertType.Voting,
                    defaultVoting: 0,
                    message: message,
                    fontSize: 16,
                    message2: langs.appointments.feedBackThanks,
                    buttonText: langs.submit,
                    onPress: async (rank: number) => {
                        appointmentFeedback(response.data?.id as number, rank);
                    },
                    onPress2: () => { },
                    buttonText2: langs.dismiss
                });
                return;
            }
            return;
        } catch {
            return;
        }
    }
    throw new Error(langs.apiMessages.failed);
}

export const validateAppointments = async (): Promise<void> => {
    const response = await request<string>("/appointments/validate", "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.STRING);

    const {
        validateHash
    } = useAppointmentStore.getState();

    validateHash(response.data || "");
}

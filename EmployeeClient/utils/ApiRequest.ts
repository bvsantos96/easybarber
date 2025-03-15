import AsyncStorage from '@react-native-async-storage/async-storage';
import * as LocalAuthentication from 'expo-local-authentication';
import * as SecureStore from 'expo-secure-store';

import { AlertType } from '@components/Alert';
import langs from '@lang/en.json';
import { ResponseType } from 'enums';
import { downloadToDevice } from 'storage/StorageUtils';
import useAlertStore from "storage/stores/AlertStore";
import useAuthStore from "storage/stores/AuthStore";
import useEstablishmentStore from 'storage/stores/EstablishmentStore';
import { LOCATIONS_STORAGE_KEY, MOBILE_INFORMATION, SECURE_STORAGE_LOGIN_KEY, TOKEN_STORAGE_KEY } from './Constants';
import { API_URL, DEBUG_SERVER_REQUESTS } from "./EnvVariables";
import { createPageable, parsePage } from './PageHandling';
import { getCalendarReadyDate, parseCountryCode, twoDigits } from './Utils';

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

const parsePathParams = (_path: string, params: Record<string, string | number | boolean | Date | undefined>): string => {
    let first = true;
    for (const key in params) {
        if (params[key] === null || params[key] === undefined)
            continue;
        if (params[key] instanceof Date) {
            params[key] = getCalendarReadyDate(params[key] as Date);
        } else
            if (params[key] === undefined) {
                continue;
            }
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

const createSecureToken = (countryCode: string, phone: string, password: string) => {
    return JSON.stringify({ countryCode, phone, password });
}

const loadSecureToken = (token: string) => {
    const loginInfo: LoginInfo = JSON.parse(token);
    return loginInfo;
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
    const { alert, setAlertVisible } = useAlertStore.getState();

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
                        setAlertVisible(false);
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

export const getApiVersion = async (): Promise<string> => {
    const response: string = await stringRequest("version", "GET", null, langs.apiMessages.success, langs.apiMessages.failed);
    if (response.length > 0)
        return response;
    throw new Error(langs.apiMessages.failed);
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

export const parsePhone = (phone: string | undefined, countryCode: string): string | undefined => {
    if (!phone) {
        return undefined;
    }
    phone = phone.trim();
    phone = phone.replace(/\s/g, '');
    const _countryCode = parseCountryCode(countryCode);
    const mobileInfo = `${_countryCode}${phone}`;
    if (!isValidNumberString(mobileInfo)) {
        return mobileInfo;
    }
    return undefined;
}

export const doLogin = async (countryCode: string, phone: string, password: string): Promise<IResult<any>> => {
    const {
        alert
    } = useAlertStore.getState();

    const {
        setEstablishments
    } = useEstablishmentStore.getState()

    phone = phone.trim();
    phone = phone.replace(/\s/g, '');
    const _countryCode = parseCountryCode(countryCode);
    if (!isValidNumberString(`${_countryCode}${phone}`)) {
        return { success: false, message: langs.apiMessages.invalidPhone };
    }

    removeData(TOKEN_STORAGE_KEY);
    removeData(LOCATIONS_STORAGE_KEY);

    const result = await request("login?employee=true", "POST", { countryMobile: _countryCode, mobile: phone, password }, langs.apiMessages.login.success, langs.apiMessages.login.failed, ResponseType.STRING);

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
        getMyEstablishment().then((establishments: EstablishmentBase[]) => {
            setEstablishments(establishments);
        });
    }

    return result;
}

export const getMyEstablishment = async (): Promise<EstablishmentBase[]> => {
    return getItemsFromRequest<EstablishmentBase[]>(await request<EstablishmentBase[]>("/employee/establishments/small", "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST));
}

export const doRegister = async (countryCode: string, phone: string, password: string, confirmPassword: string, name: string, confirmationCode: string): Promise<IResult<any>> => {
    phone = phone.trim();
    validateRegister(countryCode, phone, password, confirmPassword, name);
    name = name.trim();
    if (name.length < 3)
        return { success: false, message: langs.apiMessages.register.invalidName };
    const result = await request("employee", "POST", { countryMobile: countryCode, mobile: phone, password, name, confirmationCode }, langs.apiMessages.register.success, langs.apiMessages.register.failed, ResponseType.LIST);
    if (result.success)
        await doLogin(countryCode, phone, password);
    return result;
}

export const cancelAppointment = async (id: number, reason = ""): Promise<boolean> => {
    const response = await request('/appointment/cancel', "PUT", { id, reason }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    if (response.success) {
        return true;
    }
    return false;
}

export const getAppointmentCount = async (establishmentId?: number): Promise<AppointmentCounts> => {
    if (await getToken() === null) {
        return {
            upcomming: 0,
            past: 0
        }
    }
    const params = {
        userView: false,
        establishmentId: establishmentId
    }
    return getItemsFromRequest<AppointmentCounts>(await request<AppointmentCounts>(parsePathParams(`appointment/count`, params), "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT));
}

export const getAppointments = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter): Promise<IPage<AppointmentInfo> | undefined> => {
    if (params === undefined || params === null) {
        params = {
            future: true,
            activeOnly: true,
            userView: false
        };
    } else {
        params.activeOnly = true;
        params.userView = false;
    }
    params.sort = "date,time";
    return await pageGet<AppointmentInfo>("/appointment/list", page, params);
}

export const getEstablishments = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>): Promise<IPage<EstablishmentInfo> | undefined> => {
    return await pageGet<EstablishmentInfo>("/employee/establishments", page, params);
}

export const getTimesheets = async (page?: IPage<TimeSheetItem>, params?: Record<string, string | number | boolean>, establishmentId?: number): Promise<IPage<TimeSheetItem> | undefined> => {
    if (params === undefined || params === null) {
        params = {};
    }
    if (establishmentId !== undefined) {
        params.establishmentId = establishmentId;
    }
    if (page === undefined || page === null) {
        page = createPageable();
    }
    page.pageSize = 100;
    return await pageGet<TimeSheetItem>("/employee/schedules", page, params);
}

export const setTimesheet = async (timeSheet: TimeSheetItem): Promise<BaseResponse | undefined> => {
    return (await request<BaseResponse>("/employee/schedule", "POST", timeSheet, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).data;
}

export const deleteSchedule = async (id: number): Promise<boolean> => {
    return (await request<BaseResponse>(`/employee/schedule/${id}`, "DELETE", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const fetchMonthAppointments = async (month: number, year: number, establishmentId?: number): Promise<MonthCalendar | undefined> => {
    const from = new Date(year, month - 1, 1);
    const to = new Date(year, month, 0);
    let params: { from: Date; to: Date; establishmentId?: number } = { from, to };
    if (establishmentId !== undefined) {
        params.establishmentId = establishmentId;
    }
    const url = parsePathParams(`/schedule/calendar`, params);
    return Promise.resolve((await request<MonthCalendar>(url, "GET", { month, year, establishmentId }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).data);
}

export const createException = async (exception: Absence): Promise<boolean> => {
    return (await request<BaseResponse>("/schedule/exception", "POST", exception, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const fetchDayAppointments = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter, date?: string, establishmentId?: number): Promise<IPage<AppointmentInfo> | undefined> => {
    if (params === undefined || params === null) {
        params = {
            userView: false
        };
        if (date !== undefined) {
            params.date = date;
        }
        if (establishmentId !== undefined) {
            params.establishmentId = establishmentId;
        }
    }
    params.sort = "date,time";
    return await pageGet<AppointmentInfo>("/appointment/list", page, params);
}

export const fetchAppointments = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter): Promise<IPage<AppointmentInfo> | undefined> => {
    if (page === undefined || page === null) {
        page = createPageable();
    }
    page.pageSize = 1000;

    if (params === undefined || params === null) {
        params = {
            userView: false
        };
    }
    params.sort = "date,time";
    return await pageGet<AppointmentInfo>("/appointment/list", page, params);
}

export const getEmployees = async (establishmentId: number, page?: IPage<EmployeeListInfo>, _params?: EmployeeFilter): Promise<IPage<EmployeeListInfo> | undefined> => {
    if (page === undefined || page === null) {
        page = createPageable();
    }

    return await pageGet<EmployeeListInfo>(`/establishment/${establishmentId}/employees/list`, page, _params);
}

export const getEmployee = async (mobileInformation: string): Promise<EmployeeBase> => {
    return getItemsFromRequest<EmployeeBase>(await request<EmployeeBase>(`/employee/${mobileInformation}`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT));
}

export const getServiceTypes = async (): Promise<ICategory[]> => {
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

export const addEmployee = async (establishmentId: number, employeeId: number): Promise<boolean> => {
    return (await request<BaseResponse>(`/establishment/${establishmentId}/employee/${employeeId}`, "POST", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const fireEmployee = async (establishmentId: number, employeeId: number): Promise<boolean> => {
    return (await request<BaseResponse>(`/establishment/${establishmentId}/employee/${employeeId}`, "DELETE", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const hasEnabledSchedules = async (establishmentId: number): Promise<boolean> => {
    return (await request<BaseResponse>(`/establishment/${establishmentId}/schedules/valid`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const getServices = async (page?: IPage<ServiceDetails>, params?: Record<string, string | number | boolean>): Promise<IPage<ServiceDetails> | undefined> => {
    return await pageGet<ServiceDetails>("/employee/services", page, params);
}

export const getProducts = async (establishmentID: number, page?: IPage<ProductDetails>, params?: Record<string, string | number | boolean>): Promise<IPage<ProductDetails> | undefined> => {
    return await pageGet<ProductDetails>(`/establishment/${establishmentID}/products`, page, params);
}

export const getImageList = async (urlPrefix: string, page: IPage<IImage>, params?: Record<string, string | number | boolean>): Promise<IPage<IImage> | undefined> => {
    return await pageGet<IImage>(`${urlPrefix}/images`, page, params);
}

export const storeServiceDetails = async (service: ServiceDetails): Promise<IResult<BaseResponse>> => {
    const _service: CreateServiceDTO = {
        name: service.name,
        description: service.description,
        duration: +service.duration,
        price: service.price ? +service.price : 0,
        serviceTypeId: service.serviceType?.id ?? 0
    }

    return await request<BaseResponse>("/employee/service", "POST", _service, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
}

export const updateServiceDetails = async (service: ServiceDetails): Promise<IResult<BaseResponse>> => {
    const _service: ServiceDTO = {
        id: service.id,
        name: service.name,
        description: service.description,
        duration: +service.duration,
        price: service.price ? +service.price : 0,
        serviceTypeId: service.serviceType?.id ?? 0
    }
    return await request<BaseResponse>("/employee/service", "PUT", _service, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
}

export const storeProductDetails = async (product: ProductDetails): Promise<IResult<BaseResponse>> => {
    return await request<BaseResponse>(`/establishment/${product.establishmentId}/product`, "POST", product, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
}

export const updateProductDetails = async (product: ProductDetails): Promise<IResult<BaseResponse>> => {
    return await request<BaseResponse>(`/establishment/${product.establishmentId}/product`, "PUT", product, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
}

export const storeImage = async (urlPrefix: string, id: number, image: string, isMain: boolean): Promise<boolean> => {
    return (await request<BaseResponse>(`${urlPrefix}/${id}/images`, "POST", [{ data: image, main: isMain }], langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const replaceMainImage = async (urlPrefix: string, id: number, image: string): Promise<boolean> => {
    return (await request<BaseResponse>(`${urlPrefix}/${id}/images`, "PUT", { data: image, main: true }, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT)).success;
}

export const deleteService = async (id: number): Promise<boolean> => {
    return await request<BaseResponse>(`/service/${id}`, "DELETE", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT).then(response => response.success);
}

export const deleteProduct = async (id: number, establishmentId: number): Promise<boolean> => {
    return await request<BaseResponse>(`/establishment/${establishmentId}/product/${id}`, "DELETE", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT).then(response => response.success);
}

export const getEstablishmentEmployees = async (establishmentId: number): Promise<EmployeeEntity[]> => {
    return getItemsFromRequest(await request<EmployeeEntity[]>(`establishment/${establishmentId}/staff`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST));
}

export const getEstablishmentServiceEmployees = async (establishmentId: number, establishmentServiceId: number, date?: string, time?: string): Promise<EmployeeEntity[]> => {
    const dateTime = `${date}T${time}`;
    return getItemsFromRequest(await request<EmployeeEntity[]>(`establishment/${establishmentId}/service/${establishmentServiceId}/employees${(date && time) ? "?date=" + dateTime : ""}`, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST));
}

export const getEstablishmentServices = async (establishementId: number, establishmentStaffId?: number): Promise<ServiceInfo[]> => {
    let params: { establishmentEmployeeId?: number } = {};
    if (establishmentStaffId !== undefined) {
        params.establishmentEmployeeId = establishmentStaffId;
    }
    return getItemsFromRequest(await request<ServiceInfo[]>(parsePathParams(`establishment/${establishementId}/services/list`, params), "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.LIST));
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

export const getDynamicSlots = async (establishmentId: number, serviceId: number, employeeId: number | undefined, year: number, month: number): Promise<string[]> => {
    const params = {
        establishmentId: establishmentId,
        establishmentServiceId: serviceId,
        ...((!employeeId || employeeId == 0) ? {} : { establishmentStaffId: employeeId }),
        future: true
    };
    const url = parsePathParams(`service/list/dynamicprices/year/${year}/month/${month}`, params);
    const result = await request<string[]>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest<string[]>(result);
}

export const getAvailability = async (establishmentId: number, serviceId: number, employeeId: number | undefined, date: string, startHour: string): Promise<TimeSlots> => {
    const params = {
        establishmentId: establishmentId,
        establishmentServiceId: serviceId,
        from: date,
        startHour: startHour,
        ...((!employeeId || employeeId == 0) ? {} : { establishmentStaffId: employeeId }),
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

export const getNowHourAndMinutes = () => {
    const today = new Date();
    return twoDigits(today.getHours()) + ":" + twoDigits(today.getMinutes());
}

export const getStartingHour = (today: Date, date: string): string => {
    const todayHourAndMinute = getNowHourAndMinutes();
    return date == today.toISOString().split('T')[0] ? todayHourAndMinute : "00:01";
}

export const getUnavailableDates = async (establishmentId: number, serviceId: number, employeeId: number | undefined, year: number, month: number, startHour: string): Promise<string[]> => {
    const params = {
        establishmentId: establishmentId,
        establishmentServiceId: serviceId,
        ...((!employeeId || employeeId == 0) ? {} : { establishmentStaffId: employeeId }),
        available: false,
        startHour: startHour,
    };
    const url = parsePathParams(`schedules/availabledays/year/${year}/month/${month}`, params);
    const result = await request<string[]>(url, "GET", null, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    return getItemsFromRequest<string[]>(result);
}

export const hasDynamicPrice = async (serviceId: number, employeeId: number, date: string, time: string): Promise<number> => {
    const params = {
        establishmentServiceId: serviceId,
        establishmentStaffId: employeeId,
        date: date,
        time: time
    };
    const url = parsePathParams("dynamicprice/validate", params);
    const result = await request<number>(url, "GET", params, langs.apiMessages.success, langs.apiMessages.failed, ResponseType.OBJECT);
    try {
        return getItemsFromRequest<number>(result);
    } catch (e) {
        return 0;
    }
}

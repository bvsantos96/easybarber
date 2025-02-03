declare module '*.svg' {
    import React from 'react';
    import { SvgProps } from 'react-native-svg';
    const content: React.FC<SvgProps>;
    export default content;
}

declare type StackInfo = {
    title: string;
    component: React.FC<any>;
    hasHeader: boolean;
    containerizedComponent: boolean;
    secondHeader?: React.FC<any>;
    noGoBack?: boolean;
}

declare type TabsInfo = {
    title: string;
    hasHeader: boolean;
    component: React.FC<any>;
    tabicon: React.FC<any>;
    requiresAuth?: boolean;
    rightIcon?: React.FC<any>;
    rightAction?: (navigation: NavigationProp<any, any>) => void;
    rightText?: string;
    leftIcon?: React.FC<any>;
    leftAction?: (navigation: NavigationProp<any, any>) => void;
    leftText?: string;
}

declare interface Pageable<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            empty: boolean;
            sorted: boolean;
            unsorted: boolean;
        },
        offset: number;
        paged: boolean;
        unpaged: boolean;
    },
    last: boolean;
    totalElements: number;
    totalPages: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    },
    first: boolean;
    size: number;
    number: number;
    numberOfElements: number;
    empty: boolean;
}


declare interface IPage<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    currentPage: number;
    pageSize: number;
    hasNextPage: boolean;
    hasPreviousPage: boolean;
}

declare interface IResult<T> {
    success: boolean;
    message: string;
    items?: Pageable<T>;
    data?: T;
}

declare interface ITimedRequest<T extends Identifiable> {
    page: IPage<T>;
    lastRequest: number;
    loadingMore?: boolean;
    pathParams?: {};
    request(func: (page: IPage<T>) => Promise<IPage<T> | undefined>): Promise<boolean>;
}

declare interface ICategory {
    id: number;
    name: string;
    description: string;
    imageURL: string;
}

declare interface IImage {
    id: number;
    data: string;
    isMain?: boolean;
}

interface Identifiable {
    id: string | number;
}

declare interface EstablishmentInfo extends Identifiable {
    id: number;
    name: string;
    description: string;
    address: string;
    latitude: number;
    longitude: number;
    distance?: number;
    nvotes: number;
    sumVotes: number;
    images: IImage[];
    load?: boolean;
    favorite?: boolean;
}

declare interface AppointmentFilter extends Record<string, string | number | boolean> {
    employeeId?: number;
    establishmentId?: number;
    serviceId?: numbe;
    date?: string;
    time?: string;
    endTime?: string;
    userView?: boolean;
    future?: boolean;
    activeOnly?: boolean;
    favorite?: boolean;
}

declare interface AppointmentInfo extends Identifiable {
    id: number;
    serviceName: string;
    entityId: number;
    entityName: string;
    establishmentId: number;
    establishmentName: string;
    establishmentAddress: string;
    latitude: number;
    longitude: number;
    date: string;
    time: string;
    confirmed: boolean;
    cancelled: boolean;
    photo: string;
    feedback: number;
}

declare interface IFilterRequest {
    serviceType?: string | null;
    rating?: number;
    availableFrom?: Timespan;
    availableTo?: Timespan;
    partialName?: string;
    from?: string;
    to?: string;
}

declare interface IAPIResponse {
    success: boolean;
    message: string;
    data?: any;
    items?: any;
}

declare interface ILocation extends Identifiable {
    latitude: number;
    longitude: number;
    address: string;
    country: string;
    city: string;
    name: string | null;
}

declare interface ISmallLocation {
    latitude: number;
    longitude: number;
    address: String;
}

declare interface IAddress {
    historic: string;
    house_number: string;
    road: string;
    neighbourhood: string;
    suburb: string;
    borough: string;
    city: string;
    'ISO3166-2-lvl4': string;
    postcode: string;
    country: string;
    country_code: string;
}

declare interface IAddressSuggestion {
    place_id: number;
    licence: string;
    osm_type: string;
    osm_id: number;
    lat: string;
    lon: string;
    class: string;
    type: string;
    place_rank: number;
    importance: number;
    addresstype: string;
    name: string;
    display_name: string;
    address: IAddress;
    boundingbox: string[];
}

declare interface EmployeeInfo extends Identifiable {
    name: string;
    mobileNumber: string;
    description: string;
    availableServices: Identifiable[];
    rating: number;
    nvotes: number;
    images: IImage[];
}

declare interface EstablishmentInfo extends Identifiable {
    id: number;
    name: string;
    description: string;
    address: string;
    latitude: number;
    longitude: number;
    distance?: number;
    nvotes: number;
    sumVotes: number;
    images: IImage[];
    load?: boolean;
    favorite?: boolean;
}

declare interface EstablishmentDetail extends Identifiable {
    name: string;
    description: string;
    address: string;
    latitude: number;
    longitude: number;
    distance?: number
    availableServices: number[];
    sumVotes: number;
    rating: number;
    nvotes: number;
    images: IImage[];
}

declare interface ServiceInfo extends Identifiable {
    name: string;
    description: string;
    serviceTypeId: number;
    price: number;
    image: IImage;
}

declare interface ImageEntity extends Identifiable {
    name: string;
    image: string;
}

declare interface TimeSlots {
    slots: TimeSlot[];
}

declare interface TimeSlot {
    start: string;
    end: string;
    employeeIds: number[];
}

declare interface AppointmentCreate extends Identifiable {
    establishmentStaffId: number;
    establishmentId: number;
    establishmentServiceId: number;
    date: string;
    time: string;
}

declare interface Appointment extends Identifiable {
    employeeId: number;
    establishmentId: number;
    serviceId: number;
    date: string;
    time: string;
}

declare interface AppointmentCounts {
    upcomming: number;
    past: number;
}

declare type PropNavigation = {
    navigation: NavigationProp<any, any>
};

declare interface LoginInfo {
    countryCode: string;
    phone: string;
    password: string;
}

declare interface Feedback extends Identifiable {
    employeeName: string;
    establishmentName: string;
}

declare interface RegisterInfo {
    countryCode: string;
    phone: string;
    password: string;
    confirmPassword: string;
    name: string;
}

declare interface ImageEntity extends Identifiable {
    image: string;
}

declare interface ProductEntity extends ImageEntity {
    name: string;
    description: string;
    price: number;
}

declare interface TimeSheetItem extends Identifiable {
    employeeId?: number,
    establishmentId?: number,
    day?: string,
    days: number[],
    startHour: string,
    endHour: string
}

declare interface DailyAppointments {
    date: Date;
    occupancy: number;
}

declare interface BaseResponse extends Identifiable {
    responseMessage: string;
}

declare interface CalendarDay {
    disabled: boolean;
    hasSchedules: boolean;
    availability: DayOccupancyType;
    date: Date;
}

declare interface MonthCalendar {
    [key: string]: CalendarDay;
}

declare interface Absence {
    establishmentId?: number;
    startHour: string;
    endHour: string;
    dateFrom: string;
    dateTo: string;
    title: string;
    message?: string;
}

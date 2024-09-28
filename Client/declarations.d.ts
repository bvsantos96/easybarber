import { SvgProps } from "react-native-svg";

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

declare interface Image {
    id: number;
    data: string;
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
    distance: number;
    nvotes: number;
    sumVotes: number;
    images: Image[];
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
}

declare interface AppointmentInfo extends Identifiable {
    id: number;
    serviceName: string;
    entityName: string;
    establishmentName: string;
    latitude: number;
    longitude: number;
    date: string;
    time: string;
    confirmed: boolean;
    photo: string;
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


interface EmployeeInfo extends Identifiable {
    name: string;
    mobileNumber: string;
    description: string;
    availableServices: Identifiable[];
    rating: number;
    nvotes: number;
    images: Image[];
}

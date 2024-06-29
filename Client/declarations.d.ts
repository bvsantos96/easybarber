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

declare interface BarberInfo extends Identifiable {
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

declare type Appointment = {
    id: number;
    name: string;
    from: string;
    to: string;
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

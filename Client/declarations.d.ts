import { Timespan } from "react-native/Libraries/Utilities/IPerformanceLogger";

declare module 'react-native-stars';
declare module "*.svg" {
    import React from "react";
    import { SvgProps } from "react-native-svg";
    const content: React.FC<SvgProps>;
    export default content;
}

interface IPage<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    currentPage: number;
    pageSize: number;
    hasNextPage: boolean;
    hasPreviousPage: boolean;
}

interface IResult<T> {
    success: boolean;
    message: string;
    items?: IPageable<T>;
    data?: T;
}

interface ITimedRequest<T> {
    page: IPage<T>;
    lastRequest: number;
    loadingMore?: boolean;
    pathParams?: {};
    async request(func: (page: IPage<T>) => Promise<IPage<T> | undefined>): Promise<boolean>;
}

interface ICategory {
    id: number;
    name: string;
    description: string;
    imageURL: string;
}

interface Image {
    id: number;
    data: string;
}

interface BarberInfo {
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

type Appointment = {
    id: number,
    name: string,
    from: string,
    to: string,
    photo: string,
}

interface IFilterRequest {
    serviceType?: string;
    rating?: number;
    availableFrom?: Timespan;
    availableTo?: Timespan;
    name?: string;
}

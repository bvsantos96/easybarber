import { NavigationProp } from "@react-navigation/native";
import texts from "@lang/en.json";
import { DateData } from "react-native-calendars";

export const getDateData = (date: Date): DateData => {
    return {
        month: date.getMonth() + 1,
        year: date.getFullYear(),
        day: date.getDate(),
        dateString: getCalendarReadyDate(date),
        timestamp: date.getTime(),
    };
}

export const getDateTimeAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)} ${twoDigits(date.getHours())}:${twoDigits(date.getMinutes())}`;
}

export const getTimeAsString = (date: Date) => {
    return `${twoDigits(date.getHours())}:${twoDigits(date.getMinutes())}`;
}

export const getFullDateAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)}/${date.getFullYear()}`;
}

export const getCalendarReadyDate = (date: Date) => {
    return `${date.getFullYear()}-${twoDigits(date.getMonth() + 1)}-${twoDigits(date.getDate())}`;
}

export const getCalendarReadyTime = (date: Date) => {
    return {
        hour: date.getHours(),
        minutes: date.getMinutes(),
    };
}

export const getDateAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)}${date.getFullYear() === new Date().getFullYear() ? '' : '/' + date.getFullYear().toString().substring(2, 4)}`;
}

export const buildCurrencyString = (value: number | string) => {
    return `${parseFloat(value.toString()).toFixed(2)} ${texts.currency}`;
}

export const twoDigits = (value: number) => {
    return minDigits(value, 2);
}

export const minDigits = (value: number, digits: number) => {
    return value.toString().padStart(digits, '0');
}

export const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
    navigation.reset({
        index: 0,
        routes: [{ name: route }],
    });
}

export const getTimeDifferenceInMinutesAndSeconds = (start: Date, end: Date): string => {
    const diffInMs = end.getTime() - start.getTime();
    const totalSeconds = Math.floor(diffInMs / 1000);

    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;

    return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
};

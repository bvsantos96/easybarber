import { NavigationProp } from "@react-navigation/native";
import texts from "@lang/en.json";
import { DateData } from "react-native-calendars";
import { ViewStyle, StyleSheet, RegisteredStyle, Falsy, RecursiveArray } from "react-native";

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
    if (!date) {
        return '';
    }
    return `${date.getFullYear()}-${twoDigits(date.getMonth() + 1)}-${twoDigits(date.getDate())}`;
}

export const getDateFromCalendarReadyStringDate = (date: string) => {
    const parts = date.split('-');
    return new Date(parseInt(parts[0]), parseInt(parts[1]) - 1, parseInt(parts[2]));
}

export const getCalendarReadyTime = (date: Date) => {
    return {
        hour: date.getHours(),
        minutes: date.getMinutes(),
    };
}

export const sumTime = (time: string, minutes: number) => {
    const parts = time.split(':');
    const hours = parseInt(parts[0]);
    const newMinutes = parseInt(parts[1]) + minutes;
    return `${twoDigits(hours + Math.floor(newMinutes / 60))}:${twoDigits(newMinutes % 60)}`;
}

export const parseServerTime = (time: string) => {
    const parts = time.split(':');
    return `${twoDigits(parseInt(parts[0]))}:${twoDigits(parseInt(parts[1]))}`;
}

export const getDateAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)}${date.getFullYear() === new Date().getFullYear() ? '' : '/' + date.getFullYear().toString().substring(2, 4)}`;
}

export const getCalendarDateTime = (date: Date): string => {
    return `${getCalendarReadyDate(date)}T${twoDigits(date.getHours())}:${twoDigits(date.getMinutes())}:00`;
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

export const getClientDayOfWeekFromString = (dayOfWeek: string): number => {
    for (let i = 0; i < 7; i++) {
        if (texts.weekdays[i].toUpperCase() === dayOfWeek.toUpperCase()) {
            return i;
        }
    }
    return -1;
}

export const getServerDayOfWeek = (index: number) => {
    switch (index) {
        case 6:
            return 0;
        default:
            return index + 1;
    }
}

export const getClientDayOfWeek = (index: number) => {
    switch (index) {
        case 0:
            return 6;
        default:
            return index - 1;
    }
}

export const parseCountryCode = (countryCode: string): string => {
    if (countryCode.startsWith('+'))
        return countryCode;
    return `+${countryCode}`;
}

export const parsePhoneNumber = (countryCode: string, phone: string) => {
    phone = phone.trim();
    phone = phone.replace(/\s/g, '');
    const _countryCode = parseCountryCode(countryCode);
    return `${_countryCode}${phone}`;
}

export const fromMinutesToTime = (minutes: number) => {
    const hours = Math.floor(minutes / 60);
    const remaining = minutes % 60;
    return `${twoDigits(hours)}:${twoDigits(remaining)}`;
}

export const getStyleValue = (value: keyof ViewStyle, style: false | ViewStyle | RegisteredStyle<ViewStyle> | RecursiveArray<ViewStyle | Falsy | RegisteredStyle<ViewStyle>> | null): string | undefined => {
    if (!style) return undefined;
    const flattenedStyle = StyleSheet.flatten(style);
    return flattenedStyle?.[value] as string | undefined;
};

export const getDisabledColor = (color?: string): string | undefined => {
    if (!color) return undefined;

    let r = 0, g = 0, b = 0, a = 1;
    if (color.startsWith("#")) {
        if (color.length === 7) {
            r = parseInt(color.slice(1, 3), 16);
            g = parseInt(color.slice(3, 5), 16);
            b = parseInt(color.slice(5, 7), 16);
        } else if (color.length === 9) {
            r = parseInt(color.slice(1, 3), 16);
            g = parseInt(color.slice(3, 5), 16);
            b = parseInt(color.slice(5, 7), 16);
            a = parseInt(color.slice(7, 9), 16) / 255;
        }
    } else {

        const rgbaMatch = color.match(/^rgba\((\d+),\s*(\d+),\s*(\d+),\s*[\d.]+\)$/);
        const rgbMatch = color.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);

        if (rgbMatch || rgbaMatch) {
            r = +(rgbMatch?.[1] ?? rgbaMatch?.[1] ?? 0);
            g = +(rgbMatch?.[2] ?? rgbaMatch?.[2] ?? 0);
            b = +(rgbMatch?.[3] ?? rgbaMatch?.[3] ?? 0);
        }

        const greyR = 128;
        const greyG = 128;
        const greyB = 128;
        const opacity = 0.4

        const blendedR = Math.round(r * (1 - opacity) + greyR * opacity);
        const blendedG = Math.round(g * (1 - opacity) + greyG * opacity);
        const blendedB = Math.round(b * (1 - opacity) + greyB * opacity);

        return `rgb(${blendedR}, ${blendedG}, ${blendedB})`;
    }
};


import { NavigationProp } from "@react-navigation/native";

export const getDateTimeAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)} ${twoDigits(date.getHours())}:${twoDigits(date.getMinutes())}`;
}

export const getTimeAsString = (date: Date) => {
    return `${twoDigits(date.getHours())}:${twoDigits(date.getMinutes())}`;
}

export const getFullDateAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)}/${date.getFullYear()}`;
}

export const getDateAsString = (date: Date) => {
    return `${twoDigits(date.getDate())}/${twoDigits(date.getMonth() + 1)}${date.getFullYear() === new Date().getFullYear() ? '' : '/' + date.getFullYear().toString().substring(2, 4)}`;
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

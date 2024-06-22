import { getAllCountries, FlagType, Country } from 'react-native-country-picker-modal';
import {getLocales} from 'expo-localization';
import { DEFAULT_COUNTRY } from './EnvVariables';

export const getDefaultCountryAsync = async (): Promise<Country | undefined> => {
    try {
        const deviceCountryCode = getLocales()[0].regionCode;
        if (!deviceCountryCode) {
            throw new Error("Device country code is undefined.");
        }

        const allCountries = await getAllCountries(FlagType.FLAT);
        
        const filteredCountry = allCountries.find((c) => c.cca2 === deviceCountryCode);

        return filteredCountry;
    } catch (error) {
        console.error("Error fetching default country:", error);
        return undefined;
    }
};

export const getDefaultCountryString = (): string => {
    return DEFAULT_COUNTRY || "PT";
};

export const LOCATIONS_STORAGE_KEY = "locations";
export const TOKEN_STORAGE_KEY = "token";
export const CATEGORY_STORAGE_KEY = "categories";

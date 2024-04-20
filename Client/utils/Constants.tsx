import { getAllCountries, FlagType, Country } from 'react-native-country-picker-modal';

let defaultCountryType = process.env.EXPO_PUBLIC_DEFAULT_COUNTRY;

export const getDefaultCountryAsync = async (): Promise<Country | undefined> => {
    const defaultCountry = await getAllCountries(FlagType.FLAT);

    const filteredCountry = defaultCountry?.filter(
        (c) => c.cca2 === defaultCountryType || c.cca2 === "PT"
    )[0];

    return filteredCountry;
};

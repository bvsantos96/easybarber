import React from 'react';
import { SetStateAction, Dispatch } from 'react';
import Input from '../components/Input';
import CountryPicker, { Country } from 'react-native-country-picker-modal';

interface NationSelectionProps {
    setNation: Dispatch<SetStateAction<Country | null | undefined>>;
    nation: Country | null | undefined;
}

interface PhoneInputProps extends NationSelectionProps {
    setPhone: Dispatch<SetStateAction<string>>;
}

const PhoneInput: React.FC<PhoneInputProps> = ({ setPhone, setNation, nation }) => {
    const texts = require("@lang/en.json");

    return (
        <Input
            icon={<NationPicker {...{ setNation, nation }} />}
            placeholder={texts.phoneNumber}
            type="tel"
            onInputChange={setPhone}
        />
    );
};

export const NationPicker: React.FC<NationSelectionProps> = ({ nation, setNation }) => {
    return (
        <CountryPicker
            countryCode={nation ? nation.cca2 : "PT"}
            withFilter
            withFlag
            withCallingCode
            withEmoji
            onSelect={(country: Country) => { country && setNation(country) }}
        />
    );
};

export default PhoneInput;

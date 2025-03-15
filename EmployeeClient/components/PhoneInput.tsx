import texts from '@lang/en.json';
import React, { Dispatch, forwardRef, SetStateAction } from 'react';
import { TextInput, ViewStyle } from 'react-native';
import CountryPicker, { Country } from 'react-native-country-picker-modal';
import Input from '../components/Input';

interface NationSelectionProps {
    setNation: Dispatch<SetStateAction<Country | null | undefined>>;
    nation: Country | null | undefined;
}

interface PhoneInputProps extends NationSelectionProps {
    setPhone: Dispatch<SetStateAction<string>>;
    onFocus?: () => void,
    username?: boolean
    containerStyle?: ViewStyle;
}

const PhoneInput = forwardRef<TextInput, PhoneInputProps>(({ onFocus, setPhone, setNation, nation, username = false, containerStyle }, ref) => {
    return (
        <Input
            containerStyle={containerStyle}
            onFocus={onFocus}
            ref={ref}
            leftIcon={<NationPicker {...{ setNation, nation }} />}
            placeholder={texts.phoneNumber}
            username={username}
            type="tel"
            autoComplete="tel"
            onInputChange={setPhone}
        />
    );
});

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

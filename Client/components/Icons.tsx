import React from 'react';

import Phone from "../assets/icons/phone.svg";
import Password from "../assets/icons/password.svg";
import User from "../assets/icons/user.svg";
import { useTheme } from '../styles/ThemeContext';
import { AntDesign } from '@expo/vector-icons';
import { Entypo } from '@expo/vector-icons';

export const PhoneIcon = () => {
    const theme = useTheme();
    return (
        <Phone width={13 * theme.dimensions.absoluteWidth} height={23 * theme.dimensions.absoluteWidth} />
    );
};

export const PasswordIcon = () => {
    const theme = useTheme();
    return (
        <Password fill={"none"} width={18 * theme.dimensions.absoluteWidth} height={18 * theme.dimensions.absoluteWidth} />
    );
};

export const ShowPasswordIcon = () => {
    const theme = useTheme();
    return (
        <AntDesign name="eyeo" size={24 * theme.dimensions.absoluteWidth} color="black" />
    );
};

export const HidePasswordIcon = () => {
    const theme = useTheme();
    return (
        <Entypo name="eye-with-line" size={24 * theme.dimensions.absoluteWidth} color="black" />
    );
};

export const NameIcon = () => {
    const theme = useTheme();
    return (
        <User width={28 * theme.dimensions.absoluteWidth} height={28 * theme.dimensions.absoluteWidth} />
    );
};

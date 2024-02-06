import React from 'react';

import Phone from "../assets/icons/phone.svg";
import Password from "../assets/icons/password.svg";
import User from "../assets/icons/user.svg";
import { useTheme } from '../styles/ThemeContext';

const theme = useTheme(); 

export const PhoneIcon = () => {
    return (
        <Phone width={13 * theme.dimensions.absoluteWidth} height={23 * theme.dimensions.absoluteWidth} />
    );
};

export const PasswordIcon = () => {
    return (
        <Password fill={"none"} width={18 * theme.dimensions.absoluteWidth} height={18 * theme.dimensions.absoluteWidth} />
    );
};

export const NameIcon = () => {
    return (
        <User width={28 * theme.dimensions.absoluteWidth} height={28 * theme.dimensions.absoluteWidth} />
    );
};

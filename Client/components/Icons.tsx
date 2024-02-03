import React from 'react';
import { iconSize } from '../styles/Input';

import Phone from "../assets/icons/phone.svg";
import Password from "../assets/icons/password.svg";
import User from "../assets/icons/user.svg";
import { absoluteWidth } from '../styles/Main';

export const PhoneIcon = () => {
    return (
        <Phone width={iconSize * absoluteWidth} height={iconSize * absoluteWidth} />
    );
};

export const PasswordIcon = () => {
    return (
        <Password fill={"none"} width={iconSize * absoluteWidth} height={iconSize * absoluteWidth} />
    );
};

export const NameIcon = () => {
    return (
        <User width={iconSize * absoluteWidth} height={iconSize * absoluteWidth} />
    );
};

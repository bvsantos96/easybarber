import React from 'react';
import { Image } from 'react-native';
import { styles } from '../styles/Input';

export const PhoneIcon = () => {
    return (
        <Image
            style={styles.icon}
            source={require('../assets/icons/phone.png')}
        />
    );
};

export const PasswordIcon = () => {
    return (
        <Image
            style={styles.icon}
            source={require('../assets/icons/password.png')}
        />
    );
};

export const NameIcon = () => {
    return (
        <Image
            style={styles.icon}
            source={require('../assets/icons/user.png')}
        />
    );
};

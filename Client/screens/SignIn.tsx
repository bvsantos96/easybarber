import { View, Animated } from 'react-native';
import LogoSmall from "../assets/images/logo.svg";
import Login from '../components/Login';
import Register from '../components/Register';

import { getStyles } from '../styles/Sign';
import { useRef, useState } from 'react';
import { PropNavigation } from '../App';
import { NavigationProp } from '@react-navigation/native';
import React from 'react';

export type Props = {
    navigation: NavigationProp<any, any>,
    toggleNewUser?: () => void,
}

export default function SignIn({ navigation }: PropNavigation) {
    const styles = getStyles();
    const [newUser, setNewUser] = useState(false);
    const translateYAnimation = useRef(new Animated.Value(0)).current;

    const changeNewUser = () => {
        Animated.timing(translateYAnimation, {
            toValue: 1,
            duration: 400,
            useNativeDriver: true,
        }).start(() => {
            setNewUser(!newUser)
            Animated.timing(translateYAnimation, {
                toValue: 0,
                duration: 400,
                useNativeDriver: true,
            }).start();
        });
    }

    return (
        <View style={styles.container} >
            <View style={[styles.logoContainer, styles.shadow]} >
                <LogoSmall width={styles.logo.width} height={styles.logo.height} />
            </View>
            <Animated.View style={[styles.bottomTabContainer,
            {
                transform: [{
                    translateY: translateYAnimation.interpolate({
                        inputRange: [0, 1],
                        outputRange: [0, styles.bottomTabContainer.height],
                    })
                }],
            }]}>
                {!newUser ?
                    <Login toggleNewUser={changeNewUser} navigation={navigation} /> :
                    <Register toggleNewUser={changeNewUser} navigation={navigation} />
                }
            </Animated.View>
        </View >
    );
}

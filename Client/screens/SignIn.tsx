import React, { useEffect } from 'react';
import { View, Animated } from 'react-native';
import LogoSmall from '../assets/images/logo.svg';
import Login from '../components/Login';
import Register from '../components/Register';

import { getStyles } from '../styles/Sign';
import { useRef, useState } from 'react';
import { NavigationProp } from '@react-navigation/native';
import { useTheme } from '@styles/ThemeContext';

export type SignInProps = {
    navigation: NavigationProp<any, any>,
    toggleNewUser?: () => void,
    expand?: () => void,
    collapse?: () => void,
}

export default function SignIn({ navigation }: SignInProps) {
    const theme = useTheme();
    const styles = getStyles();
    const [newUser, setNewUser] = useState(false);
    const translateYAnimation = useRef(new Animated.Value(0)).current;
    const [expanded, setExpanded] = useState(false);
    const heightAnim = useRef(new Animated.Value(styles.bottomTabContainer.height)).current;

    const maxHeight = theme.dimensions.heightWithoutStatusBar;

    useEffect(() => { adjustHeight() }, [expanded]);

    const adjustHeight = () => {
        Animated.timing(heightAnim, {
            toValue: expanded ? maxHeight : styles.bottomTabContainer.height,
            duration: 300,
            useNativeDriver: false,
        }).start();
    }

    const changeNewUser = () => {
        expanded && setExpanded(false);
        Animated.timing(translateYAnimation, {
            toValue: 1,
            duration: 300,
            useNativeDriver: false,
        }).start(() => {
            setNewUser(!newUser)
            Animated.timing(translateYAnimation, {
                toValue: 0,
                duration: 300,
                useNativeDriver: false,
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
                height: heightAnim,
            },
            {
                transform: [{
                    translateY: translateYAnimation.interpolate({
                        inputRange: [0, 1],
                        outputRange: [0, theme.dimensions.height],
                    })
                }],
            }]}>
                {!newUser ?
                    <Login
                        expand={() => {
                            setExpanded(true);
                        }}
                        collapse={() => {
                            setExpanded(false);
                        }}
                        toggleNewUser={changeNewUser}
                        navigation={navigation}
                    /> :
                    <Register
                        expand={() => {
                            setExpanded(true);
                        }}
                        collapse={() => {
                            setExpanded(false);
                        }}
                        toggleNewUser={changeNewUser}
                        navigation={navigation}
                    />
                }
            </Animated.View>
        </View >
    );
}

import React, { useEffect, useRef } from 'react';
import { View, Text, Animated } from 'react-native';
import { getStyles } from '../styles/Alert';
import Button from './Button';
import Success from '@assets/images/success.svg';
import Info from '@assets/images/info.svg';
import Error from '@assets/images/error.svg';
import texts from '@lang/en.json';
import { useTheme } from '@styles/ThemeContext';

export enum AlertType {
    Success = 'success',
    Error = 'error',
    Info = 'info',
}

export interface AlertProps {
    message: string;
    onPress?: () => void;
    buttonText?: string;
    type?: AlertType;
    onPress2?: () => void;
    buttonText2?: string;
}

interface Props extends AlertProps {
    visible: boolean;
    setVisible: Function,
    children: React.ReactNode
}

export const CustomAlert: React.FC<Props> = ({
    children,
    visible,
    setVisible,
    message,
    onPress,
    buttonText,
    onPress2,
    buttonText2,
    type
}) => {
    const fadeAnim = useRef(new Animated.Value(0)).current;
    const scaleAnim = useRef(new Animated.Value(0.8)).current;

    const _type = type || AlertType.Success;
    const _buttonText = buttonText || texts.dismiss;
    const _buttonText2 = buttonText2 || texts.dismiss;

    useEffect(() => {
        if (visible) {
            Animated.parallel([
                Animated.timing(fadeAnim, {
                    toValue: 1,
                    duration: 300,
                    useNativeDriver: true,
                }),
                Animated.spring(scaleAnim, {
                    toValue: 1,
                    friction: 8,
                    tension: 40,
                    useNativeDriver: true,
                }),
            ]).start();
        } else {
            Animated.parallel([
                Animated.timing(fadeAnim, {
                    toValue: 0,
                    duration: 200,
                    useNativeDriver: true,
                }),
                Animated.spring(scaleAnim, {
                    toValue: 0.8,
                    friction: 8,
                    tension: 40,
                    useNativeDriver: true,
                }),
            ]).start();
        }
    }, [visible, fadeAnim, scaleAnim]);

    const handleClose = (func?: () => void) => {
        setVisible(false);
        if (func) func();
    };

    const theme = useTheme();
    const styles = getStyles();
    const _backgroundColor = () => {
        switch (_type) {
            case AlertType.Success:
                return theme.colors.successColor;
            case AlertType.Error:
                return theme.colors.mainColor;
            case AlertType.Info:
                return theme.colors.infoColor;
            default:
                return theme.colors.mainColor;
        }
    }
    const [backgroundColor] = React.useState(_backgroundColor());

    return (
        < >
            {children}
            {visible && (
                <Animated.View
                    style={[
                        styles.modalOverlay,
                        {
                            opacity: fadeAnim,
                        },
                    ]}
                >
                    <Animated.View
                        style={[
                            styles.alertBox,
                            {
                                transform: [{ scale: scaleAnim }],
                            },
                        ]}
                    >
                        {_type === AlertType.Success &&
                            <Success style={styles.image} />
                        }
                        {_type === AlertType.Error &&
                            <Error style={styles.image} />
                        }
                        {_type === AlertType.Info &&
                            <Info style={styles.image} />
                        }
                        <View style={styles.messageContainer}>
                            <Text style={styles.message}>{message}</Text>
                        </View>
                        <View style={styles.buttonContainer}>
                            <View style={styles.buttonWrapperLeft} >
                                <Button
                                    backgroundColor={backgroundColor}
                                    borderColor={backgroundColor}
                                    title={_buttonText}
                                    onPress={() => handleClose(onPress)}
                                    stylesInput={styles.button}
                                />
                            </View>
                            {onPress2 &&
                                <View style={styles.buttonWrapperRight} >
                                    <Button
                                        buttonTextColor={styles.button2.color}
                                        backgroundColor={backgroundColor}
                                        borderColor={backgroundColor}
                                        title={_buttonText2}
                                        onPress={() => handleClose(onPress2)}
                                        stylesInput={styles.button}
                                    />
                                </View>
                            }
                        </View>
                    </Animated.View>
                </Animated.View>
            )}
        </>
    );
};

export default CustomAlert;

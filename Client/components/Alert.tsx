import React, { useEffect, useRef, useState } from 'react';
import Stars from 'react-native-stars';
import { View, Text, Animated } from 'react-native';
import Ionicons from '@expo/vector-icons/Ionicons';

import { getStyles } from '../styles/Alert';
import Button from './Button';
import Success from '@assets/images/success.svg';
import Info from '@assets/images/info.svg';
import Error from '@assets/images/error.svg';
import Voting from '@assets/images/voting.svg';
import texts from '@lang/en.json';
import { useTheme } from '@styles/ThemeContext';
import Divider from './Divider';

export enum AlertType {
    Success = 'success',
    Error = 'error',
    Info = 'info',
    Voting = 'voting',
}

export interface AlertProps {
    message: string;
    message2?: string;
    onPress?: (param?: any) => void;
    buttonText?: string;
    type?: AlertType;
    onPress2?: () => void;
    buttonText2?: string;
    fontSize?: number;
}

interface Props extends AlertProps {
    visible: boolean;
    setVisible: Function,
    children: React.ReactNode
}

export const CustomAlert: React.FC<Props> = ({
    fontSize,
    children,
    visible,
    setVisible,
    message,
    message2,
    onPress,
    buttonText,
    buttonText2,
    onPress2,
    type
}) => {
    const fadeAnim = useRef(new Animated.Value(0)).current;
    const scaleAnim = useRef(new Animated.Value(0.8)).current;
    const [rating, setRating] = useState<number>(0);

    const setStarsSelected = (value: number) => {
        if (value === rating && value === 1) {
            setRating(0);
            return;
        }
        setRating(value);
    }

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

    const handleClose = (func?: (param?: any) => void) => {
        setVisible(false);
        if (type === AlertType.Voting) {
            if (func) func(rating);
        } else {
            if (func) func();
        }
    };

    const theme = useTheme();
    const styles = getStyles();
    const [starsSize] = useState<number>((((styles.alertBox.width * 0.72) / 5 + 40) * theme.dimensions.absoluteWidth) / 2);

    const _backgroundColor = (): string => {
        switch (_type) {
            case AlertType.Error:
                return theme.colors.mainColor;
            case AlertType.Info:
                return theme.colors.infoColor;
            case AlertType.Voting:
                return theme.colors.mainColor;
            case AlertType.Success:
            default:
                return theme.colors.mainColor;
        }
    }

    const _icon = (): React.JSX.Element => {
        switch (_type) {
            case AlertType.Error:
                return <Error style={styles.image} />
            case AlertType.Info:
                return <Info style={styles.image} />
            case AlertType.Voting:
                return <Voting style={styles.image} />
            case AlertType.Success:
            default:
                return <Success style={styles.image} />
        }
    }

    const [color, setColor] = useState<string>(_backgroundColor());
    const [icon, setIcon] = useState<React.JSX.Element>(_icon());

    useEffect(() => {
        setColor(_backgroundColor());
        setIcon(_icon());
    }, [visible]);

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
                        <View style={styles.alertView}>
                            <Divider size={20} horizontal={false} />
                            {icon}
                            <Divider size={30} horizontal={false} />
                            <Text style={[styles.message, fontSize ? { fontSize: fontSize } : {}]}>{message}</Text>
                            {_type === AlertType.Voting ? (
                                <>
                                    <Divider size={15} horizontal={false} />
                                    <View style={styles.votingContainer}>
                                        <Stars
                                            half={false}
                                            default={rating}
                                            update={setStarsSelected}
                                            spacing={10 * theme.dimensions.absoluteWidth}
                                            starSize={starsSize}
                                            count={5}
                                            zero={true}
                                            fullStar={<Ionicons name="star" size={starsSize} color={theme.colors.mainColor} />}
                                            emptyStar={<Ionicons name="star-outline" size={starsSize} color={theme.colors.text.lightGray} />}
                                        />
                                    </View>
                                    <Divider size={10} horizontal={false} />
                                </>
                            ) : (
                                <Divider size={10} horizontal={false} />
                            )}
                            {message2 &&
                                <>
                                    <Divider size={10} horizontal={false} />
                                    <Text style={[styles.message, styles.message2]}>{message2}</Text>
                                    <Divider size={10} horizontal={false} />
                                </>
                            }
                            <Divider size={10} horizontal={false} />
                            <View style={styles.buttonContainer} >
                                <View style={styles.buttonWrapperLeft} >
                                    <Button
                                        backgroundColor={color}
                                        borderColor={color}
                                        title={_buttonText}
                                        onPress={() => handleClose(onPress)}
                                        stylesInput={styles.button}
                                    />
                                </View>
                                {onPress2 &&
                                    <View style={[styles.buttonWrapperRight, theme.shadow]} >
                                        <Button
                                            buttonTextColor={color}
                                            backgroundColor={styles.button2.backgroundColor}
                                            borderColor={color}
                                            title={_buttonText2}
                                            onPress={() => handleClose(onPress2)}
                                            stylesInput={styles.button}
                                        />
                                    </View>
                                }
                            </View>
                            <Divider size={25} horizontal={false} />
                        </View>
                    </Animated.View>
                </Animated.View>
            )}
        </>
    );
};

export default CustomAlert;

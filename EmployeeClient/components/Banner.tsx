import React, { useEffect, useRef, useState } from 'react';
import { Animated, Dimensions, PanResponder, Text, TouchableOpacity } from 'react-native';

import { getStyles } from '@styles/Banner';
import useAlertStore from 'storage/stores/AlertStore';
import { AlertType } from './Alert';

export enum BannerType {
    Success = 'success',
    Error = 'error',
    Info = 'info',
    Warning = 'warning'
}

export interface BannerProps {
    message: string;
    type?: BannerType;
    duration?: number;
    onDismiss?: () => void;
    onPress?: () => void;
    showAlertOnPull?: boolean;
}

interface Props extends BannerProps {
    visible: boolean;
    setVisible: Function,
}

const CustomBanner: React.FC<Props> = ({
    message,
    type = BannerType.Info,
    duration = 3000,
    visible,
    setVisible,
    onDismiss,
    onPress,
    showAlertOnPull = false
}) => {
    const translateY = useRef(new Animated.Value(-100)).current;
    const [isPanning, setIsPanning] = useState(false);
    const { height } = Dimensions.get('window');
    const styles = getStyles();
    const {
        alert
    } = useAlertStore();

    const openAlert = () => {
        setVisible(false);
        alert({
            type: type === BannerType.Success ? AlertType.Success : type === BannerType.Error ? AlertType.Error : AlertType.Info,
            message: message,
        });
    }

    const _onPress = onPress ? onPress : openAlert;

    const panResponder = useRef(
        PanResponder.create({
            onStartShouldSetPanResponder: () => true,
            onMoveShouldSetPanResponder: () => true,
            onPanResponderGrant: () => {
                setIsPanning(true);
            },
            onPanResponderMove: (_, gestureState) => {
                if (gestureState.dy > -100 && gestureState.dy < height / 4) {
                    if (gestureState.dy > 0 && !showAlertOnPull) {
                        return;
                    }
                    translateY.setValue(gestureState.dy);
                }
            },
            onPanResponderRelease: (_, gestureState) => {
                setIsPanning(false);
                if (gestureState.dy < -50) {
                    hideBanner();
                } else if (gestureState.dy > height / 6) {
                    if (showAlertOnPull) {
                        hideBanner();
                        openAlert();
                    }
                } else {
                    Animated.spring(translateY, {
                        toValue: 0,
                        useNativeDriver: true,
                    }).start();
                }
            },
        })
    ).current;

    const showBanner = () => {
        Animated.spring(translateY, {
            toValue: 0,
            useNativeDriver: true,
        }).start();
    };

    const hideBanner = () => {
        Animated.timing(translateY, {
            toValue: -100,
            duration: 300,
            useNativeDriver: true,
        }).start(() => {
            setVisible(false);
            if (onDismiss) onDismiss();
        });
    };

    useEffect(() => {
        if (visible) {
            showBanner();
            if (!isPanning && duration > 0) {
                const timer = setTimeout(hideBanner, duration);
                return () => clearTimeout(timer);
            }
        }
    }, [visible, isPanning]);

    const getBannerColor = () => {
        switch (type) {
            case BannerType.Success:
                return '#4CAF50';
            case BannerType.Error:
                return '#F44336';
            case BannerType.Warning:
                return '#FF9800';
            case BannerType.Info:
            default:
                return '#2196F3';
        }
    };

    if (!visible) return null;

    return (
        <Animated.View
            style={[
                styles.banner,
                {
                    backgroundColor: getBannerColor(),
                    transform: [{ translateY }],
                },
            ]}
            {...panResponder.panHandlers}
        >
            <TouchableOpacity onPress={_onPress}>
                <Text style={styles.text}>{message}</Text>
            </TouchableOpacity>
        </Animated.View>
    );
};

export default CustomBanner;

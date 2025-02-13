import React, { useCallback, useEffect, useRef } from "react";
import { View, Text, Animated, PanResponder, LayoutChangeEvent, PanResponderGestureState, GestureResponderEvent } from "react-native";
import { Image } from "expo-image";

import ClockIcon from "@assets/icons/clock.svg";
import { getStyles } from "../styles/Appointments";
import { getDateAsString, getTimeAsString } from "../utils/Utils";
import Fontisto from '@expo/vector-icons/Fontisto';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import Divider from "./Divider";
import Pressable from "./Pressable";
import { gotoLocation } from "utils/Location";
import { cancelAppointment } from "utils/ApiRequest";
import texts from "@lang/en.json";
import useAlertStore from "storage/stores/AlertStore";
import { AlertType } from "./Alert";
import { useTheme } from "@styles/ThemeContext";

interface Props extends PropNavigation {
    appointment: AppointmentInfo;
    cancel?: (id: number) => void;
}

export default function AppointmentItem({ appointment, cancel }: Props) {
    const { alert } = useAlertStore();
    const theme = useTheme();
    const styles = getStyles();
    const date = new Date(`${appointment.date}T${appointment.time}`);
    const dateString = getDateAsString(date);
    const timeString = getTimeAsString(date);
    const movingRef = useRef(0);
    const position = useRef(new Animated.Value(0)).current;
    const [isMoving, setIsMoving] = React.useState(false);
    const value = useRef(0);
    const minValue = useRef(0);

    const setValue = (newValue: number) => {
        value.current = newValue;
    };

    const handleIconsLayout = (event: LayoutChangeEvent) => {
        const { width } = event.nativeEvent.layout;
        minValue.current = -width;
    }

    const move = (val = value.current) => {
        Animated.timing(position, {
            toValue: val,
            duration: 250,
            useNativeDriver: true,
        }).start(() => { setIsMoving(false); });
    }

    useEffect(() => {
        move();
    }, [value.current]);

    const handleSelection = useCallback((flip = true, left?: boolean) => {
        if (isMoving) return;
        setIsMoving(true);
        let newValue = flip ? (minValue.current - value.current) : left ? 0 : minValue.current;
        if (value.current === newValue) {
            move(value.current);
            return;
        }
        setValue(newValue);
    }, [isMoving, minValue]);

    const release = (evt: GestureResponderEvent, gestureState: PanResponderGestureState) => {
        let p = gestureState.x0 > movingRef.current ? gestureState.x0 - movingRef.current : gestureState.x0;
        let x = evt.nativeEvent.pageX - p;
        if (x <= minValue.current / 2) {
            handleSelection((gestureState.vx === 0 && gestureState.vy === 0), false);
        } else {
            handleSelection((gestureState.vx === 0 && gestureState.vy === 0), true);
        }
    }

    const panResponder = React.useRef(
        PanResponder.create({
            onStartShouldSetPanResponder: () => true,
            onStartShouldSetPanResponderCapture: () => true,
            onMoveShouldSetPanResponder: () => true,
            onMoveShouldSetPanResponderCapture: () => true,
            onPanResponderMove: (evt, gestureState) => {
                let p = gestureState.x0 > movingRef.current ? gestureState.x0 - movingRef.current : gestureState.x0;
                let x = value.current + evt.nativeEvent.pageX - p;
                if (x > 0) {
                    x = 0;
                } else if (x < minValue.current) {
                    x = minValue.current;
                }
                position.setValue(x);
            },
            onPanResponderTerminationRequest: () => true,
            onPanResponderRelease: release,
            onPanResponderTerminate: release,
            onShouldBlockNativeResponder: () => true
        }),
    ).current;

    return (
        <View style={{ position: "relative" }}>
            <Animated.View
                onLayout={(event) => {
                    const { width } = event.nativeEvent.layout;
                    movingRef.current = width;
                }}
                style={[styles.itemContainer, styles.shadow, styles.movingContainer, { transform: [{ translateX: position }] }]}
                {...panResponder.panHandlers} >
                <View style={styles.imageContainer}>
                    <Image
                        cachePolicy="memory"
                        source={{ uri: appointment.photo }} style={styles.imageStyle} />
                </View>
                <View style={styles.textContainer}>
                    <Text numberOfLines={1} style={styles.title}>{appointment.establishmentName}</Text>
                    <View style={styles.subTitleContainer}>
                        <Text numberOfLines={1} style={styles.locationText}>{appointment.entityName}</Text>
                    </View>
                    <View style={styles.infoContainer}>
                        <View style={styles.locationContainer}>
                            <ClockIcon width={styles.locationIcon.width} height={styles.locationIcon.height} style={styles.locationIcon} />
                        </View>
                        <Text style={styles.locationText}>{dateString} {texts.at} {timeString}</Text>
                    </View>
                </View>
                {appointment.cancelled &&
                    <View style={styles.cancelled} />
                }
            </Animated.View>
            <View style={[styles.itemContainer, styles.shadow, styles.backContainer, { flexDirection: "row" }]} >
                <View style={{ flexDirection: "row" }} onLayout={handleIconsLayout}>
                    <Divider size={15} horizontal={true} />
                    {cancel !== undefined &&
                        <>
                            <Pressable onPress={async () => {
                                alert({
                                    type: AlertType.Error,
                                    message: texts.appointments.cancel,
                                    buttonText: texts.yes,
                                    onPress: async () => {
                                        if (await cancelAppointment(appointment.id)) {
                                            cancel(appointment.id);
                                        }
                                    },
                                    onPress2: () => { },
                                    buttonText2: texts.no
                                });
                            }
                            } style={[styles.icon, styles.redIcon]}>
                                <Fontisto name="trash" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                            </Pressable>
                            <Divider size={15} horizontal={true} />
                        </>
                    }
                    {!appointment.cancelled &&
                        <>
                            <Pressable onPress={() => gotoLocation(appointment.establishmentName, appointment.establishmentAddress, appointment.latitude, appointment.longitude)} style={[styles.icon, styles.maps]}>
                                <MaterialCommunityIcons name="google-maps" size={styles.icon.fontSize} color={theme.colors.mainColor} />
                            </Pressable>
                            <Divider size={15} horizontal={true} />
                        </>
                    }
                </View>
            </View>
        </View >
    );
}

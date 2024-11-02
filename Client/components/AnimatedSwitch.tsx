import React, { useCallback, useEffect, useRef } from 'react';
import { Animated, View, Text, Pressable, PanResponder } from 'react-native';
import { getStyles } from '../styles/AnimatedSwitch';
import { useTheme } from '@styles/ThemeContext';

export default function AnimatedSwitch({ text1, text2, setSelected }: { text1: string, text2: string, setSelected: (selected: boolean) => void }) {
    const theme = useTheme();
    const maxVal = 155 * theme.dimensions.absoluteWidth;
    const position = useRef(new Animated.Value(0)).current;
    const value = useRef(0);
    const setValue = (newValue: number) => {
        value.current = newValue;
    };
    const styles = getStyles();
    const [selected, setSelectedState] = React.useState(true);
    const [isMoving, setIsMoving] = React.useState(false);
    const movingRef = useRef(0);

    const move = (val = value.current) => {
        Animated.timing(position, {
            toValue: val,
            duration: 250,
            useNativeDriver: true,
        }).start(() => { setIsMoving(false); setSelectedState(value.current === 0); setSelected(value.current === 0); });
    }

    const handleSelection = useCallback((flip = true, left?: boolean) => {
        if (isMoving) return;
        setIsMoving(true);
        let newValue = flip ? Math.abs(value.current - maxVal) : left ? 0 : maxVal;
        if (value.current === newValue) {
            move(value.current);
            return;
        }
        setValue(newValue);
    }, [isMoving, maxVal]);

    useEffect(() => {
        move();
    }, [value.current]);

    const panResponder = React.useRef(
        PanResponder.create({
            onStartShouldSetPanResponder: () => true,
            onStartShouldSetPanResponderCapture: () => true,
            onMoveShouldSetPanResponder: () => true,
            onMoveShouldSetPanResponderCapture: () => true,
            onPanResponderMove: (evt, gestureState) => {
                let p = gestureState.x0 > movingRef.current ? gestureState.x0 - movingRef.current : gestureState.x0;
                let x = evt.nativeEvent.pageX - p;
                if (x < 0) {
                    x = 0;
                } else if (x > maxVal) {
                    x = maxVal;
                }
                position.setValue(x);
            },
            onPanResponderTerminationRequest: () => true,
            onPanResponderRelease: (evt, gestureState) => {
                let p = gestureState.x0 > movingRef.current ? gestureState.x0 - movingRef.current : gestureState.x0;
                let x = evt.nativeEvent.pageX - p;
                if (x >= maxVal / 2) {
                    handleSelection((gestureState.vx === 0 && gestureState.vy === 0), false);
                } else {
                    handleSelection((gestureState.vx === 0 && gestureState.vy === 0), true);
                }
            },
            onPanResponderTerminate: () => { },
            onShouldBlockNativeResponder: () => true
        }),
    ).current;

    return (
        <View style={styles.container} onLayout={(event) => {
            const { width } = event.nativeEvent.layout;
            console.log(width/2);
        }}>
            <Pressable style={styles.box} onPress={() => handleSelection()} />
            <Animated.View
                onLayout={(event) => {
                    const { width } = event.nativeEvent.layout;
                    movingRef.current = width;
                }}
                style={[styles.movingBox, { transform: [{ translateX: position }] }]}
                {...panResponder.panHandlers} />
            <View style={styles.textContainer} pointerEvents='none'>
                <View style={styles.textLeftContainer}>
                    <Text style={selected ? styles.textSelected : styles.textUnselected}>{text1}</Text>
                </View>
                <View style={styles.textRightContainer}>
                    <Text style={selected ? styles.textUnselected : styles.textSelected}>{text2}</Text>
                </View>
            </View>
        </View>
    );
};

import React, { useEffect, useRef } from 'react';
import { Animated, View, Text, Pressable, PanResponder } from 'react-native';
import { getStyles } from '../styles/AnimatedSwitch';

export default function AnimatedSwitch({ text1, text2, setSelected }: { text1: string, text2: string, setSelected: (selected: boolean) => void }) {
    const maxVal = 150;
    const position = useRef(new Animated.Value(0)).current;
    const [value, setValue] = React.useState(0);
    const styles = getStyles();
    const [selected, setSelectedState] = React.useState(true);
    const [isMoving, setIsMoving] = React.useState(false);
    const movingRef = useRef(0);

    const handleSelection = (flip = true, left?: boolean) => {
        if (isMoving) return;
        setIsMoving(true);
        setValue(flip ? Math.abs(value - maxVal) : left ? 0 : maxVal);
    }

    useEffect(() => {
        Animated.timing(position, {
            toValue: value,
            duration: 250,
            useNativeDriver: true,
        }).start(() => { setIsMoving(false); setSelectedState(value === 0); setSelected(value === 0); });
    }, [value]);

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
                if (x >= movingRef.current / 2) {
                    handleSelection(false, false);
                } else {
                    handleSelection(false, true);
                }
            },
            onPanResponderTerminate: () => { },
            onShouldBlockNativeResponder: () => true
        }),
    ).current;

    return (
        <View style={styles.container}>
            <Pressable style={styles.box} onPress={() => handleSelection()} />
            <Animated.View
                onLayout={(event) => {
                    // Capture moving box width in ref
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

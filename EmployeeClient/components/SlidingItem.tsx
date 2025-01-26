import { ReactNode, useCallback, useEffect, useRef, useState } from "react";
import { Animated, PanResponder, View } from "react-native";
import { getStyles } from "@styles/SlidingItem";

const SlidingItem = ({ children, items }: {
    children: ReactNode | ReactNode[], items: ReactNode | ReactNode[]
}) => {
    const styles = getStyles();
    const movingRef = useRef(0);
    const position = useRef(new Animated.Value(0)).current;
    const [isMoving, setIsMoving] = useState(false);
    const value = useRef(0);
    const minValue = useRef(0);

    const setValue = (newValue: number) => {
        value.current = newValue;
    };

    const handleIconsLayout = (event: any) => {
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

    const release = (evt: any, gestureState: any) => {
        let p = gestureState.x0 > movingRef.current ? gestureState.x0 - movingRef.current : gestureState.x0;
        let x = evt.nativeEvent.pageX - p;
        if (x <= minValue.current / 2) {
            handleSelection((gestureState.vx === 0 && gestureState.vy === 0), false);
        } else {
            handleSelection((gestureState.vx === 0 && gestureState.vy === 0), true);
        }
    }

    const panResponder = useRef(
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
                {children}
            </Animated.View>
            <View style={[styles.itemContainer, styles.shadow, styles.backContainer, { flexDirection: "row" }]} >
                <View style={{ flexDirection: "row" }} onLayout={handleIconsLayout}>
                    {items}
                </View>
            </View>
        </View >
    );
}

export default SlidingItem;

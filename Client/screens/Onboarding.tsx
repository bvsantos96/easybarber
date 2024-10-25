import { useTheme } from "@styles/ThemeContext";
import { useRef } from "react";
import { Animated, PanResponder, View } from "react-native";
import Onboarding1 from "./Onboarding1";
import Onboarding2 from "./Onboarding2";

const OnBoarding = ({ navigation }: PropNavigation) => {
    const translateXAnimation = useRef(new Animated.Value(0)).current;
    const theme = useTheme();
    const currentPageRef = useRef(0);

    const panResponder = useRef(
        PanResponder.create({
            onStartShouldSetPanResponder: () => true,
            onMoveShouldSetPanResponder: (_, gestureState) => {
                return Math.abs(gestureState.dx) > Math.abs(gestureState.dy);
            },
            onPanResponderMove: (_, gestureState) => {
                const _value = currentPageRef.current + (-gestureState.dx / theme.dimensions.width);
                translateXAnimation.setValue(_value);
            },
            onPanResponderRelease: (_, gestureState) => {
                const threshold = 0.3;
                if (gestureState.dx > theme.dimensions.width * threshold) {
                    currentPageRef.current = 0;
                    Animated.spring(translateXAnimation, {
                        toValue: 0,
                        useNativeDriver: true,
                    }).start(() => {
                        currentPageRef.current = 0;
                    });
                } else if (gestureState.dx < -theme.dimensions.width * threshold) {
                    currentPageRef.current = 1;
                    Animated.spring(translateXAnimation, {
                        toValue: 1,
                        useNativeDriver: true,
                    }).start(() => {
                        currentPageRef.current = 1;
                    });
                } else {
                    Animated.spring(translateXAnimation, {
                        toValue: currentPageRef.current,
                        useNativeDriver: true,
                    }).start();
                }
            },
        })
    ).current;

    const gotoOnBoarding2 = () => {
        currentPageRef.current = 1;
        Animated.timing(translateXAnimation, {
            toValue: 1,
            duration: 400,
            useNativeDriver: true,
        }).start();
    }

    return (
        <View style={{ flexDirection: 'row', width: theme.dimensions.width, height: theme.dimensions.height }} {...panResponder.panHandlers}>
            <Animated.View style={{
                position: 'absolute',
                top: 0,
                left: 0,
                width: theme.dimensions.width,
                height: theme.dimensions.height,
                alignItems: 'center',
                transform: [{
                    translateX: translateXAnimation.interpolate({
                        inputRange: [0, 1],
                        outputRange: [0, -theme.dimensions.width],
                    })
                }],
            }}>
                <Onboarding1 nextPage={gotoOnBoarding2} />
            </Animated.View>

            <Animated.View style={{
                width: theme.dimensions.width,
                height: theme.dimensions.height,
                alignItems: 'center',
                transform: [{
                    translateX: translateXAnimation.interpolate({
                        inputRange: [0, 1],
                        outputRange: [theme.dimensions.width, 0],
                    })
                }],
            }}>
                <Onboarding2 navigation={navigation} />
            </Animated.View>
        </View>
    );
};

export default OnBoarding;

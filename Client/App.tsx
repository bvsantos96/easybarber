import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Platform, Linking, PanResponder } from 'react-native';
import { NavigationContainer, NavigationContainerRef, NavigationProp } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import * as Location from 'expo-location';

import { getToken } from './utils/ApiRequest';
import { getDefaultCountryString } from './utils/Constants';
import useLocationStore from './storage/stores/LocationStore';

SplashScreen.preventAutoHideAsync();

import { LogBox } from 'react-native';

LogBox.ignoreLogs([
    'Support for defaultProps will be removed from function components',
]);

//screens
import { ThemeProvider, useTheme } from './styles/ThemeContext';
import { Animated, View, Text } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import DismissKeyboard from './components/DismissKeyboard';
import { loadLongTermItems } from './storage/ApiLongTermStorage';
import Constants from 'expo-constants';
import { validateVersion } from './utils/VersionValidation';
import { UpdateType } from './enums';
import { DEBUG_AUTO_LOGIN } from './utils/EnvVariables';
import { setCountry } from './utils/Location';
import { ALERT_TYPE, AlertNotificationRoot } from 'react-native-alert-notification';
import { Alert } from './components/Alert';

export type PropNavigation = {
    navigation: NavigationProp<any, any>
};

export const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
    navigation.reset({
        index: 0,
        routes: [{ name: route }],
    });
}

const OnBoarding = ({ navigation }) => {
    const Onboarding1 = require("./screens/Onboarding1").default;
    const Onboarding2 = require("./screens/Onboarding2").default;
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

const Router = () => {
    // Tabs
    const Loading = require('./screens/Loading').default;
    const Tabs = require('./screens/Tabs').default;
    const AccountTypeSelection = require('./screens/AccountTypeSelection').default;
    const SignIn = require('./screens/SignIn').default;
    const LocationRequest = require('./screens/LocationRequest').default;
    const {
        requestingLocationPermission,
        setHasLocationPermission,
        hasLocationPermission
    } = useLocationStore();

    type StackParamList = {
        LocationRequest: undefined;
        OnBoarding: undefined;
        AccountTypeSelection: undefined;
        Sign: undefined;
        Tabs: undefined;
        Loading: undefined;
        Test: undefined;
    };

    const [isLoading, setIsLoading] = useState(true);
    const [defaultPage, setDefaultPage] = useState<keyof StackParamList>("Tabs");

    const Stack = createNativeStackNavigator<StackParamList>();
    const navigationRef = useRef<NavigationContainerRef<StackParamList> | null>(null);

    useEffect(() => {
        const loadInitialData = async () => {
            let defaultPage: keyof StackParamList = "OnBoarding";
            await Font.loadAsync({
                'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
                'Poppins': require('./assets/fonts/Poppins/Poppins-Regular.ttf'),
                'Nunito': require('./assets/fonts/Nunito/Nunito-VariableFont_wght.ttf'),
            });
            await loadLongTermItems();
            if (DEBUG_AUTO_LOGIN) {
                console.log("DEBUG AUTO LOGIN");
                defaultPage = "Sign";
                await waitAndNavigate("Sign");
            } else {
                defaultPage = await getToken() !== null ? "Tabs" : "OnBoarding";
                try {
                    await setCountry();
                } catch (error) {
                    console.error('Error setting country:', error);
                }
            }

            setDefaultPage(defaultPage);

            const { status } = await Location.getForegroundPermissionsAsync();
            if (status === 'granted') {
                setHasLocationPermission(true);
                navigationRef.current?.navigate(defaultPage);
            } else if (status === 'denied') {
                setHasLocationPermission(false);
                navigationRef.current?.navigate(defaultPage);
            }

            setIsLoading(false);
            await SplashScreen.hideAsync();
        };

        loadInitialData();
    }, []);

    const waitAndNavigate = async (page: keyof StackParamList) => {
        if (navigationRef.current) {
            navigationRef.current?.navigate(page);
            return;
        } else {
            setTimeout(async () => await waitAndNavigate(page), 500);
        }
    }

    useEffect(() => {
        const navigateToLocationRequest = () => {
            if (navigationRef.current) {
                if (requestingLocationPermission) {
                    const currentPage = navigationRef.current.getCurrentRoute()?.name as keyof StackParamList;
                    if (currentPage !== "Home" as keyof StackParamList && currentPage !== "Loading" as keyof StackParamList && currentPage !== "LocationRequest" as keyof StackParamList) {
                        setDefaultPage(currentPage);
                    }
                    navigationRef.current.navigate("LocationRequest");
                } else if (requestingLocationPermission === false) {
                    navigationRef.current?.navigate(defaultPage);
                }
            } else {
                setTimeout(navigateToLocationRequest, 500);
            }
        };

        if (!hasLocationPermission && requestingLocationPermission !== undefined) {
            navigateToLocationRequest();
        }
    }, [requestingLocationPermission]);


    if (isLoading) {
        return null;
    }

    const containerizedComponent = (component: JSX.Element) => {
        const theme = useTheme();
        return (
            <SafeAreaView style={{
                flex: 1,
                width: theme.dimensions.width,
                height: theme.dimensions.height,
                backgroundColor: theme.colors.backgroundColor,
                justifyContent: 'center',
                alignSelf: 'center',
                alignContent: 'center',
                alignItems: 'center',
            }}>
                {component}
            </SafeAreaView>
        );
    }

    return (
        <GestureHandlerRootView style={{ flex: 1 }} >
            <NavigationContainer ref={navigationRef}>
                <Stack.Navigator>
                    <Stack.Screen name="OnBoarding" options={{ headerShown: false }}>
                        {props => containerizedComponent(<OnBoarding {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="AccountTypeSelection" options={{ headerShown: false }} >
                        {props => containerizedComponent(<AccountTypeSelection {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Sign" options={{ headerShown: false, gestureEnabled: false }} >
                        {props => <SignIn {...props} />}
                    </Stack.Screen>
                    <Stack.Screen name="Tabs" options={{ headerShown: false }} >
                        {props => <Tabs {...props} />}
                    </Stack.Screen>
                    <Stack.Screen name="LocationRequest" options={{ headerShown: false }}>
                        {props => <LocationRequest {...props} />}
                    </Stack.Screen>
                    <Stack.Screen name="Loading" options={{ headerShown: false }} >
                        {props => containerizedComponent(<Loading {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Test" options={{ headerShown: false, gestureEnabled: false }}>
                        {_ => containerizedComponent(<View style={{ backgroundColor: "red", minHeight: 50, minWidth: 50 }}><Text>Test</Text></View>)}
                    </Stack.Screen>
                </Stack.Navigator>
            </NavigationContainer>
        </GestureHandlerRootView >
    );
}

export default function App() {
    const handleMajorUpdate = () => {
        const texts = require('./langs/en.json');
        const redirectToStore = () => {
            const iosBundle = Constants.expoManifest?.ios?.bundleIdentifier || '';
            const androidBundle = Constants.expoManifest?.android?.package || '';
            const url = Platform.OS === 'ios' ? `${texts.appStoreLink}${getDefaultCountryString().toLowerCase()}/${iosBundle}` : `${texts.playStoreLink}${androidBundle}`;
            Linking.openURL(url).catch((err) => console.error('An error occurred', err));
        };
        Alert({
            type: ALERT_TYPE.INFO,
            title: texts.updateRequired,
            message: texts.updateRequiredMessage,
            onPress: () => { redirectToStore(); retryVersionCheck(); },
            buttonText: texts.update
        });
    };

    const errorWhileUpdating = () => {
        const texts = require('./langs/en.json');
        Alert({
            type: ALERT_TYPE.DANGER,
            title: texts.errorWhileUpdating,
            message: texts.errorWhileUpdatingMessage,
            onPress: retryVersionCheck,
            buttonText: texts.retry
        });
    }
    const versionCheck = useCallback(async () => {
        switch (await validateVersion()) {
            case UpdateType.MAJOR:
                handleMajorUpdate();
                break;
            case UpdateType.FAILED:
                errorWhileUpdating();
                break;
            default:
                break;
        }
    }, []);

    const retryVersionCheck = useCallback(() => {
        versionCheck();
    }, [versionCheck]);

    useEffect(() => {
        versionCheck();
    }, [versionCheck]);

    return (
        <SafeAreaProvider style={{ flex: 1 }}>
            <ThemeProvider>
                <DismissKeyboard>
                    <AlertNotificationRoot>
                        <Router />
                    </AlertNotificationRoot>
                </DismissKeyboard>
            </ThemeProvider>
        </SafeAreaProvider>
    );
}

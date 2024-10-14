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
import usePermissionStore from './storage/stores/PermissionStore';

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
import CustomAlert, { AlertType } from './components/Alert';
import { Header } from '@screens/HomeNavigator';
import { getSelectedLocation } from 'utils/Location';
import useAlertStore from 'storage/stores/AlertStore';

export const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
    navigation.reset({
        index: 0,
        routes: [{ name: route }],
    });
}

const OnBoarding = ({ navigation }: PropNavigation) => {
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
    const MobileConfirmation = require('./screens/MobileConfirmation').default;
    const InsertPhone = require('./screens/InsertPhone').default;
    const ResetPwd = require('./screens/ResetPwd').default;
    const {
        requestingLocationPermission,
        setHasLocationPermission,
        hasLocationPermission
    } = usePermissionStore();

    type StackParamList = {
        LocationRequest: undefined;
        OnBoarding: undefined;
        AccountTypeSelection: undefined;
        Sign: undefined;
        Tabs: undefined;
        Loading: undefined;
        Test: undefined;
        MobileConfirmation: undefined;
        InsertPhone: undefined;
        ResetPwd: undefined;
    };

    const [isLoading, setIsLoading] = useState(true);
    const [defaultPage, setDefaultPage] = useState<keyof StackParamList>("Tabs");
    const [navigationReady, setNavigationReady] = useState(false);

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
                await waitAndNavigate(defaultPage);
                return;
            } else {
                defaultPage = await getToken() !== null ? "Tabs" : "OnBoarding";
            }

            setDefaultPage(defaultPage);

            const { status } = await Location.getForegroundPermissionsAsync();
            if (status === 'granted') {
                setHasLocationPermission(true);
            } else if (status === 'denied') {
                setHasLocationPermission(false);
            }

            await waitAndResetNavigation(defaultPage);
            setIsLoading(false);
            await SplashScreen.hideAsync();
        };

        loadInitialData();
    }, []);

    const waitAndResetNavigation = async (page: keyof StackParamList) => {
        if (navigationReady && navigationRef.current) {
            navigationRef.current.reset({
                index: 0,
                routes: [{ name: page }],
            });
            return;
        } else {
            setTimeout(async () => await waitAndResetNavigation(page), 500);
        }
    }

    const waitAndNavigate = async (page: keyof StackParamList) => {
        if (navigationReady && navigationRef.current) {
            navigationRef.current?.navigate(page);
            return;
        } else {
            setTimeout(async () => await waitAndNavigate(page), 500);
        }
    }

    useEffect(() => {
        const navigateToLocationRequest = async () => {
            const { status } = await Location.getForegroundPermissionsAsync();
            if (status == "granted" || status == "denied") {
                return;
            }
            if (navigationReady && navigationRef.current) {
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

        if (hasLocationPermission) {
            getSelectedLocation();
            waitAndNavigate(defaultPage);
            return;
        }

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
            <NavigationContainer ref={navigationRef} onReady={() => { setNavigationReady(true) }}>
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
                    <Stack.Screen name="MobileConfirmation" options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={"Verify Phone"} />
                        )
                    }}>
                        {props => <MobileConfirmation {...props} />}
                    </Stack.Screen>
                    <Stack.Screen name="InsertPhone" options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={"Forgot Password"} />
                        )
                    }}>
                        {props => <InsertPhone {...props} />}
                    </Stack.Screen>
                    <Stack.Screen name="ResetPwd" options={{
                        header: ({ navigation }) => (
                            <Header navigation={navigation} title={"New Password"} />
                        )
                    }}>
                        {props => <ResetPwd {...props} />}
                    </Stack.Screen>
                </Stack.Navigator>
            </NavigationContainer>
        </GestureHandlerRootView >
    );
}

export default function App() {
    const error = console.error;
    console.error = (...args: any) => {
        if (/defaultProps/.test(args[0])) return;
        error(...args);
    };

    const {
        alertVisible,
        alert,
        alertProps
    } = useAlertStore();

    const handleMajorUpdate = () => {
        const texts = require('./langs/en.json');
        const redirectToStore = () => {
            const iosBundle = Constants.expoManifest?.ios?.bundleIdentifier || '';
            const androidBundle = Constants.expoManifest?.android?.package || '';
            const url = Platform.OS === 'ios' ? `${texts.appStoreLink}${getDefaultCountryString().toLowerCase()}/${iosBundle}` : `${texts.playStoreLink}${androidBundle}`;
            Linking.openURL(url).catch((err) => console.error('An error occurred', err));
        };
        alert({ type: AlertType.Info, message: texts.updateRequiredMessage, onPress: () => { redirectToStore(); retryVersionCheck(); }, buttonText: texts.update });
    };

    const errorWhileUpdating = () => {
        const texts = require('./langs/en.json');
        alert({
            message: texts.errorWhileUpdatingMessage,
            onPress: retryVersionCheck,
            buttonText: texts.retry,
            type: AlertType.Error
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
                    <CustomAlert
                        {...alertProps}
                        visible={alertVisible}
                        setVisible={alert}
                    >
                        <Router />
                    </CustomAlert>
                </DismissKeyboard>
            </ThemeProvider>
        </SafeAreaProvider>
    );
}

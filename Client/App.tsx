import React, { useCallback, useEffect, useRef, useState } from 'react';
import { QueryClient, QueryClientProvider, useQueryClient } from '@tanstack/react-query';
import { Platform, Linking } from 'react-native';
import { NavigationContainer, NavigationContainerRef } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import * as Location from 'expo-location';

import { getToken, isFirstTime, refreshToken, removeData, requestFeedback } from './utils/ApiRequest';
import { TOKEN_STORAGE_KEY, getDefaultCountryString } from './utils/Constants';
import usePermissionStore from './storage/stores/PermissionStore';
import texts from "@lang/en.json";

SplashScreen.preventAutoHideAsync();

import { LogBox } from 'react-native';

LogBox.ignoreLogs([
    'Support for defaultProps will be removed from function components',
]);

//screens
import { ThemeProvider, useTheme } from './styles/ThemeContext';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import DismissKeyboard from './components/DismissKeyboard';
import { loadLongTermItems } from './storage/ApiLongTermStorage';
import Constants from 'expo-constants';
import { validateVersion } from './utils/VersionValidation';
import { UpdateType } from './enums';
import { DEBUG_AUTO_LOGIN } from './utils/EnvVariables';
import CustomAlert, { AlertType } from './components/Alert';
import Header from '@components/Header';
import { getSelectedLocation } from 'utils/Location';
import useAlertStore from 'storage/stores/AlertStore';
import RootNav from '@navigation/RootNavigator';
import { Params, Routes } from '@navigation/Router';
import { SetSelectedRef } from '@screens/EstablishmentDetails';
import useAuthStore from 'storage/stores/AuthStore';

const Router = () => {
    const {
        requestingLocationPermission,
        setHasLocationPermission,
        hasLocationPermission
    } = usePermissionStore();


    const setSelectedRef = useRef<SetSelectedRef>(null);
    const [favorite, setFavorite] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [defaultPage, setDefaultPage] = useState<keyof typeof Params>(Routes.Tabs);

    const Stack = createNativeStackNavigator<typeof Params>();
    const navigationRef = useRef<NavigationContainerRef<typeof Params> | null>(null);

    useEffect(() => {
        const loadInitialData = async () => {
            let defaultPage: keyof typeof Params;
            await Font.loadAsync({
                'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
                'Poppins': require('./assets/fonts/Poppins/Poppins-Regular.ttf'),
                'Nunito': require('./assets/fonts/Nunito/Nunito-VariableFont_wght.ttf'),
            });
            await loadLongTermItems();
            if (DEBUG_AUTO_LOGIN) {
                console.log("DEBUG AUTO LOGIN");
                defaultPage = Routes.Sign;
            } else {
                if (await isFirstTime()) {
                    defaultPage = Routes.Onboarding;
                } else if (await getToken() === null && !await refreshToken()) {
                    defaultPage = Routes.Sign;
                } else {
                    requestFeedback();
                    defaultPage = Routes.Tabs;
                }
            }

            setDefaultPage(defaultPage);

            const { status } = await Location.getForegroundPermissionsAsync();
            if (status === 'granted') {
                setHasLocationPermission(true);
            } else if (status === 'denied') {
                setHasLocationPermission(false);
            }

            if (defaultPage !== Routes.Onboarding) {
                await waitAndResetNavigation(defaultPage);
            }
            setIsLoading(false);
            await SplashScreen.hideAsync();
        };

        loadInitialData();
    }, []);

    const waitAndResetNavigation = async (page: keyof typeof Params, params?: any) => {
        if (navigationRef.current) {
            navigationRef.current.reset({
                index: 0,
                routes: [{ name: page as string, params: params }],
            });
            return;
        } else {
            setTimeout(async () => await waitAndResetNavigation(page), 500);
        }
    }

    const waitAndNavigate = async (page: keyof typeof Params, params?: any) => {
        if (navigationRef.current) {
            navigationRef.current.navigate(page, params);
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
            if (navigationRef.current) {
                if (requestingLocationPermission) {
                    const currentPage = navigationRef.current.getCurrentRoute()?.name as keyof typeof Params;
                    if (currentPage !== Routes.Tabs && currentPage !== Routes.Loading && currentPage !== Routes.LocationRequest) {
                        setDefaultPage(currentPage);
                    }
                    waitAndNavigate(Routes.LocationRequest);
                } else if (requestingLocationPermission === false) {
                    waitAndNavigate(defaultPage);
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

    const { doLogout } = useAuthStore();
    const queryClient = useQueryClient()

    useEffect(() => {
        if (doLogout === undefined) return;
        removeData(TOKEN_STORAGE_KEY);
        waitAndResetNavigation(Routes.Onboarding);
        queryClient.clear();
    }, [doLogout]);

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
                    {RootNav && Object.keys(RootNav).map((key) => {
                        const _key = key as keyof typeof Params;
                        const nav = RootNav[_key];
                        if (!nav) return null;
                        return (<Stack.Screen
                            key={_key}
                            name={_key}
                            options={nav.hasHeader ?
                                {
                                    header: ({ navigation }) => (
                                        <Header navigation={navigation} title={nav.title} hasGoBack={!nav.noGoBack} secondHeader={nav.secondHeader} secondHeaderFunction={setSelectedRef?.current?.setSelected} selected={favorite} />
                                    ),
                                }
                                :
                                {
                                    headerShown: false
                                }
                            }
                        >

                            {(props) => {
                                switch (_key) {
                                    case Routes.EstablishmentDetails:
                                        return <nav.component {...props} ref={setSelectedRef} setFavorite={setFavorite} />;
                                    default:
                                        return nav.containerizedComponent ? containerizedComponent(<nav.component {...props} />) : <nav.component {...props} />
                                }
                            }}
                        </Stack.Screen>);
                    })}
                </Stack.Navigator>
            </NavigationContainer>
        </GestureHandlerRootView >
    );
}

export default function App() {
    const queryClient = new QueryClient();
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
        const redirectToStore = () => {
            const iosBundle = Constants.expoManifest?.ios?.bundleIdentifier || '';
            const androidBundle = Constants.expoManifest?.android?.package || '';
            const url = Platform.OS === 'ios' ? `${texts.appStoreLink}${getDefaultCountryString().toLowerCase()}/${iosBundle}` : `${texts.playStoreLink}${androidBundle}`;
            Linking.openURL(url).catch((err) => console.error('An error occurred', err));
        };
        alert({ type: AlertType.Info, message: texts.updateRequiredMessage, onPress: () => { redirectToStore(); retryVersionCheck(); }, buttonText: texts.update });
    };

    const errorWhileUpdating = () => {
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
        <QueryClientProvider client={queryClient}>
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
        </QueryClientProvider>
    );
}

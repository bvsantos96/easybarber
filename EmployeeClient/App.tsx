import React, { useCallback, useEffect, useRef, useState } from 'react';
import Constants from 'expo-constants';
import { QueryClient, QueryClientProvider, useQueryClient } from '@tanstack/react-query';
import { Platform, Linking } from 'react-native';
import { NavigationContainer, NavigationContainerRef } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import { SafeAreaProvider } from 'react-native-safe-area-context';
SplashScreen.preventAutoHideAsync();
import { LogBox } from 'react-native';
LogBox.ignoreLogs([
    'Support for defaultProps will be removed from function components',
]);

import texts from '@lang/en.json';
import { Params, Routes } from '@navigation/Router';
import RootNav from '@navigation/RootNavigator';
import useAuthStore from 'storage/stores/AuthStore';
import Header from '@components/Header';
import { ThemeProvider } from '@styles/ThemeContext';
import CustomAlert, { AlertType } from '@components/Alert';
import { UpdateType, ServiceAction } from 'enums';
import { DEBUG_AUTO_LOGIN } from 'utils/EnvVariables';
import useAlertStore from 'storage/stores/AlertStore';
import { getDefaultCountryString } from 'utils/Constants';
import DismissKeyboard from '@components/DismissKeyboard';
import { deleteMobileInformation, deleteService, deleteToken, getServiceTypes, getToken, refreshToken } from 'utils/ApiRequest';
import { validateVersion } from 'utils/VersionValidation';
import SafeFullScreen from '@components/SafeFullScreen';
import useHeaderStore from 'storage/stores/HeaderStore';
import useServiceTypeStore from 'storage/stores/ServiceTypeStore';
import useUpdateStore from 'storage/stores/UpdateStore';

const Router = () => {
    const queryClient = useQueryClient()
    const { doLogout } = useAuthStore();
    const { reset: resetHeaderStore } = useHeaderStore();

    const [isLoading, setIsLoading] = useState(true);

    const Stack = createNativeStackNavigator<typeof Params>();
    const navigationRef = useRef<NavigationContainerRef<typeof Params> | null>(null);

    const { alert, setAlertVisible } = useAlertStore();
    const { setToUpdate } = useUpdateStore();
    const { setServiceTypes } = useServiceTypeStore();

    useEffect(() => {
        const loadInitialData = async () => {
            let defaultPage: keyof typeof Params;
            await Font.loadAsync({
                'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
                'Poppins': require('./assets/fonts/Poppins/Poppins-Regular.ttf'),
                'Nunito': require('./assets/fonts/Nunito/Nunito-VariableFont_wght.ttf'),
            });
            setServiceTypes(await getServiceTypes());
            //await loadLongTermItems();
            if (DEBUG_AUTO_LOGIN) {
                console.log("DEBUG AUTO LOGIN");
                defaultPage = Routes.Sign;
            } else {
                if (await getToken() === null && !await refreshToken()) {
                    defaultPage = Routes.Sign;
                } else {
                    defaultPage = Routes.Tabs;
                }
            }

            if (defaultPage !== Routes.Sign) {
                await waitAndResetNavigation(defaultPage);
            }

            setIsLoading(false);
            await SplashScreen.hideAsync();
        };

        loadInitialData();
    }, []);

    useEffect(() => {
        if (navigationRef.current) {
            navigationRef.current.addListener('state', (_) => {
                resetHeaderStore();
            });
        }
    }, [navigationRef.current])

    const waitAndResetNavigation = async (page: keyof typeof Params, params?: any) => {
        if (navigationRef.current) {
            navigationRef.current.reset({
                index: 0,
                routes: [{ name: page as string, params: params }],
            });
            return;
        } else {
            setTimeout(async () => await waitAndResetNavigation(page, params), 500);
        }
    }

    const waitAndNavigate = async (page: keyof typeof Params, params?: any) => {
        if (navigationRef.current) {
            navigationRef.current.navigate(page, params);
            return;
        } else {
            setTimeout(async () => await waitAndNavigate(page, params), 500);
        }
    }

    useEffect(() => {
        if (doLogout === undefined) return;
        deleteToken();
        deleteMobileInformation();
        waitAndResetNavigation(Routes.Sign);
        queryClient.clear();
    }, [doLogout]);

    if (isLoading) {
        return null;
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
                                    header: ({ navigation, route }) => {
                                        let id: number | undefined = undefined;
                                        if (nav.route) {
                                            const _params: RouteParams = (route?.params as RouteParams) ?? {};
                                            id = _params?.[nav.route]?.id;
                                        }
                                        return (
                                            <Header
                                                navigation={navigation}
                                                title={nav.title}
                                                hasGoBack={!nav.noGoBack}
                                                secondHeader={_key === Routes.Service ? (id ? nav.secondHeader : undefined) : nav.secondHeader}
                                                secondHeaderFunction={
                                                    _key === Routes.Service
                                                        ? async (_) => {
                                                            console.log("DELETE SERVICE");
                                                            if (id) {
                                                                alert({ type: AlertType.Loading, message: "" });
                                                                const deleted = await deleteService(id || -1);
                                                                if (deleted) {
                                                                    setAlertVisible(false);
                                                                    alert({
                                                                        message: texts.serviceDeleted, type: AlertType.Success, onPress: () => {
                                                                            setToUpdate({ action: ServiceAction.DELETE, id: id });
                                                                            navigation.goBack();
                                                                        }
                                                                    });
                                                                }
                                                                return true;
                                                            }
                                                            return false;
                                                        }
                                                        : undefined
                                                }
                                                hideSecondHeader={(route.params as any)?.hideSecondHeader} />
                                        );
                                    },
                                }
                                :
                                {
                                    headerShown: false
                                }
                            }
                        >
                            {(props) => (
                                nav.containerizedComponent ? (<SafeFullScreen><nav.component {...props} /></SafeFullScreen>) : (<nav.component {...props} />)
                            )}
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

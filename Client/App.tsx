import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Platform, Linking } from 'react-native';
import { NavigationContainer, NavigationProp } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import { getToken } from './utils/ApiRequest';
import { getDefaultCountryString } from './utils/Constants';

SplashScreen.preventAutoHideAsync();

//screens
import { ThemeProvider, useTheme } from './styles/ThemeContext';
import { Animated, View, Text, Alert } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import DismissKeyboard from './components/DismissKeyboard';
import { loadLongTermItems } from './storage/ApiLongTermStorage';
import Constants from 'expo-constants';
import { validateVersion } from './utils/VersionValidation';
import { UpdateType } from './enums';
import { DEBUG_AUTO_LOGIN } from './utils/EnvVariables';

export type PropNavigation = {
    navigation: NavigationProp<any, any>
};

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

    const changeNewUser = () => {
        Animated.timing(translateXAnimation, {
            toValue: 1,
            duration: 400,
            useNativeDriver: true,
        }).start();
    }

    return (
        <View style={{ flexDirection: 'row' }}>
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
                        outputRange: [0, -1 * theme.dimensions.height / 2],
                    })
                }],
            }}>
                <Onboarding1 nextPage={changeNewUser} />
            </Animated.View>

            <Animated.View style={{
                width: theme.dimensions.width,
                height: theme.dimensions.height,
                alignItems: 'center',
                transform: [{
                    translateX: translateXAnimation.interpolate({
                        inputRange: [0, 1],
                        outputRange: [theme.dimensions.height / 2, 0],
                    })
                }],
            }}>
                <Onboarding2 navigation={navigation} />
            </Animated.View>
        </View>
    );
}

const Router = () => {
    // Tabs
    const Loading = require('./screens/Loading').default;
    const Tabs = require('./screens/Tabs').default;
    const AccountTypeSelection = require('./screens/AccountTypeSelection').default;
    const SignIn = require('./screens/SignIn').default;

    const [isLoading, setIsLoading] = useState(true);
    const [defaultTab, setDefaultTab] = useState<string>("Tabs");

    const Stack = createNativeStackNavigator();

    useEffect(() => {
        const loadInitialData = async () => {
            await Font.loadAsync({
                'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
                'Poppins': require('./assets/fonts/Poppins/Poppins-Regular.ttf'),
                'Nunito': require('./assets/fonts/Nunito/Nunito-VariableFont_wght.ttf'),
            });
            await loadLongTermItems();
            if(DEBUG_AUTO_LOGIN) {
                console.log("DEBUG AUTO LOGIN");
                setDefaultTab("Sign");
            } else {
                setDefaultTab(await getToken() !== null ? "Tabs" : "OnBoarding");
            }
            setIsLoading(false);
            await SplashScreen.hideAsync();
        };

        loadInitialData();
    }, []);

    if (isLoading) {
        return null; // or render a loading indicator
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
            <NavigationContainer>
                    <Stack.Navigator
                        initialRouteName={defaultTab}
                    >
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
        Alert.alert(
            texts.updateRequired,
            texts.updateRequiredMessage,
            [{ text: texts.update, onPress: () => { redirectToStore(); retryVersionCheck(); } }],
            { cancelable: false }
        );
    };

    const errorWhileUpdating = () => {
        const texts = require('./langs/en.json');
        Alert.alert(
            texts.errorWhileUpdating,
            texts.errorWhileUpdatingMessage,
            [{ text: texts.retry, onPress: retryVersionCheck }],
        );
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
                    <Router />
                </DismissKeyboard>
            </ThemeProvider>
        </SafeAreaProvider>
    );
}

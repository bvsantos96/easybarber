import { NavigationContainer, NavigationProp } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useCallback } from 'react';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';

SplashScreen.preventAutoHideAsync();

//screens
import Signin from './screens/SignIn';
import { backgroundColor, height, width } from './styles/Main';

//components

export type PropNavigation = {
    navigation: NavigationProp<any, any>
};

export const resetNavigation = (navigation: NavigationProp<any, any>, route: string) => {
    navigation.reset({
        index: 0,
        routes: [{ name: route }],
    });
}

const LoginScreen = ({ navigation }: PropNavigation) => {
    const Login = require('./components/Login').default;

    return (
        <Signin page={<Login navigation={navigation} />} />
    );
}

const RegisterScreen = ({ navigation }: PropNavigation) => {
    const Register = require('./components/Register').default;

    return (
        <Signin page={<Register navigation={navigation} />} />
    );
}

export default function App() {
    // Tabs
    const Loading = require('./screens/Loading').default;
    const Onboarding1 = require("./screens/Onboarding1").default;
    const Onboarding2 = require("./screens/Onboarding2").default;
    const Tabs = require('./screens/Tabs').default;
    const AccountTypeSelection = require('./screens/AccountTypeSelection').default;

    const Stack = createNativeStackNavigator();
    const [fontsLoaded, fontError] = Font.useFonts({
        'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
        'Poppins': require('./assets/fonts/Poppins/Poppins-Regular.ttf'),
        'Nunito': require('./assets/fonts/Nunito/Nunito-VariableFont_wght.ttf'),
    });

    const onLayoutRootView = useCallback(async () => {
        if (fontsLoaded || fontError) {
            await SplashScreen.hideAsync();
        }
    }, [fontsLoaded, fontError]);

    if (!fontsLoaded && !fontError) {
        return null;
    }

    return (
        <GestureHandlerRootView style={{ flex: 1, width: width, height:height, backgroundColor: backgroundColor }} onLayout={onLayoutRootView} >
            <NavigationContainer>
                <Stack.Navigator initialRouteName="Onboarding1">
                    <Stack.Screen name="Onboarding1" component={Onboarding1} options={{ headerShown: false }} />
                    <Stack.Screen name="Onboarding2" component={Onboarding2} options={{ headerShown: false }} />
                    <Stack.Screen name="AccountTypeSelection" component={AccountTypeSelection} options={{ headerShown: false }} />
                    <Stack.Screen name="Login" component={LoginScreen} options={{ headerShown: false }} />
                    <Stack.Screen name="Register" component={RegisterScreen} options={{ headerShown: false }} />
                    <Stack.Screen name="Tabs" component={Tabs} options={{ headerShown: false }} />
                    <Stack.Screen name="Loading" component={Loading} options={{ headerShown: false }} />
                </Stack.Navigator>
            </NavigationContainer>
        </GestureHandlerRootView>
    );
}

import { NavigationContainer, NavigationProp } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useCallback } from 'react';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import * as Font from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';

SplashScreen.preventAutoHideAsync();

//screens
import Signin from './screens/SignIn';
import { ThemeProvider, useTheme } from './styles/ThemeContext';
import { View } from 'react-native';

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

const Router = () => {
    // Theme
    const theme = useTheme();
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

    const containerizedComponent = (component: JSX.Element) => {
        return (
            <View style={{ flex: 1, width: theme.dimensions.width, height: theme.dimensions.height, backgroundColor: "blue" }}>
                {component}
            </View>
        );
    }

    return (
        <GestureHandlerRootView style={{flex: 1}}onLayout={onLayoutRootView} >
            <NavigationContainer>
                <Stack.Navigator initialRouteName="Onboarding1">
                    <Stack.Screen name="Onboarding1" options={{ headerShown: false }}>
                        {props => containerizedComponent(<Onboarding1 {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Onboarding2" options={{ headerShown: false }}>
                        {props => containerizedComponent(<Onboarding2 {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="AccountTypeSelection" options={{ headerShown: false }} >
                        {props => containerizedComponent(<AccountTypeSelection {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Login" options={{ headerShown: false }} >
                        {props => containerizedComponent(<LoginScreen {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Register" options={{ headerShown: false }} >
                        {props => containerizedComponent(<RegisterScreen {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Tabs" options={{ headerShown: false }} >
                        {props => containerizedComponent(<Tabs {...props} />)}
                    </Stack.Screen>
                    <Stack.Screen name="Loading" options={{ headerShown: false }} >
                        {props => containerizedComponent(<Loading {...props} />)}
                    </Stack.Screen>
                </Stack.Navigator>
            </NavigationContainer>
        </GestureHandlerRootView>
    );
}

export default function App() {

    return (
        <ThemeProvider>
            <Router />
        </ThemeProvider>
    );
}

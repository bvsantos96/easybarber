import { NavigationContainer, NavigationProp } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useEffect } from 'react';

//screens
import Tabs from './screens/Tabs';
import AccountTypeSelection from './screens/AccountTypeSelection'
import Signin from './screens/SignIn';
import Onboarding1 from './screens/Onboarding1';
import Onboarding2 from './screens/Onboarding2';
import Appointments from './screens/Appointments';

//components
import Login from './components/Login';
import Register from './components/Register';

import * as Font from 'expo-font';

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
    return (
        <Signin page={<Login navigation={navigation} />} />
    );
}

const RegisterScreen = ({ navigation }: PropNavigation) => {
    return (
        <Signin page={<Register navigation={navigation}/>} />
    );
}

export default function App() {
    const Stack = createNativeStackNavigator();

    useEffect(() => {
    
        Font.loadAsync({
          'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
        });
      }, []);

    return (
        <NavigationContainer>
            <Stack.Navigator initialRouteName="Tabs">
                <Stack.Screen name="Onboarding1" component={Onboarding1} options={{ headerShown: false }}/>
                <Stack.Screen name="Onboarding2" component={Onboarding2} options={{ headerShown: false }}/>
                <Stack.Screen name="AccountTypeSelection" component={AccountTypeSelection} options={{ headerShown: false }}/>
                <Stack.Screen name="Login" component={LoginScreen} options={{ headerShown: false }}/>
                <Stack.Screen name="Register" component={RegisterScreen} options={{ headerShown: false }}/>
                <Stack.Screen name="Tabs" component={Tabs} options={{ headerShown: false }}/>
            </Stack.Navigator>
        </NavigationContainer>
    );
}

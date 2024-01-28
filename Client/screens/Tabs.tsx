import { Image } from 'react-native';
import Home from './Home';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { inputTextColor, mainColor } from '../styles/Main';
import Appointments from './Appointments';
import Filter from './Filter';

export default function Tabs() {
    const Tab = createBottomTabNavigator();
    const texts = require('../langs/en.json');

    return (
        <Tab.Navigator screenOptions={{
            tabBarActiveTintColor: mainColor,
            tabBarInactiveTintColor: inputTextColor,
        }}>
            <Tab.Screen name={texts.tabs.home} component={Home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <Image
                            source={require('../assets/icons/home.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    ) : (
                        <Image
                            source={require('../assets/icons/home-selected.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    )
                }} />
            <Tab.Screen name={texts.tabs.appointments} component={Appointments}
                options={{
                    tabBarIcon: ({ focused }) => !focused ? (
                        <Image
                            source={require('../assets/icons/appointments.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    ) : (
                        <Image
                            source={require('../assets/icons/appointments-selected.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    )
                }} />
            <Tab.Screen name={texts.tabs.alerts} component={Filter}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <Image
                            source={require('../assets/icons/alerts.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    ) : (
                        <Image
                            source={require('../assets/icons/alerts-selected.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    )
                }} />
            <Tab.Screen name={texts.tabs.chats} component={Home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <Image
                            source={require('../assets/icons/chats.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    ) : (
                        <Image
                            source={require('../assets/icons/chats-selected.png')}
                            style={{ width: 20, height: 20 }}
                        />
                    )
                }} />
        </Tab.Navigator>
    );
}

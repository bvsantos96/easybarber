import Home from './Home';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { absoluteWidth, inputTextColor, mainColor } from '../styles/Main';
import Appointments from './Appointments';
import HomeIcon from "@assets/icons/home.svg";
import AppointmentsIcon from "@assets/icons/appointments.svg";
import AlertsIcon from "@assets/icons/alerts.svg";
import ChatsIcon from "@assets/icons/chats.svg";

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
                        <HomeIcon width={20 * absoluteWidth} height={20 * absoluteWidth} />
                    ) : (
                        <HomeIcon width={20 * absoluteWidth} height={20 * absoluteWidth} fill={mainColor} />
                    )
                }} />
            <Tab.Screen name={texts.tabs.appointments} component={Appointments}
                options={{
                    tabBarIcon: ({ focused }) => !focused ? (
                        <AppointmentsIcon width={20 * absoluteWidth} height={20 * absoluteWidth} />
                    ) : (
                        <AppointmentsIcon width={20 * absoluteWidth} height={20 * absoluteWidth} fill={mainColor} />
                    )
                }} />
            <Tab.Screen name={texts.tabs.alerts} component={Home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <AlertsIcon width={20 * absoluteWidth} height={20 * absoluteWidth} />
                    ) : (
                        <AlertsIcon width={20 * absoluteWidth} height={20 * absoluteWidth} fill={mainColor} />
                    )
                }} />
            <Tab.Screen name={texts.tabs.chats} component={Home}
                options={{
                    headerShown: false,
                    tabBarIcon: ({ focused }) => !focused ? (
                        <ChatsIcon width={20 * absoluteWidth} height={20 * absoluteWidth} />
                    ) : (
                        <ChatsIcon width={20 * absoluteWidth} height={20 * absoluteWidth} fill={mainColor} />
                    )
                }} />
        </Tab.Navigator>
    );
}

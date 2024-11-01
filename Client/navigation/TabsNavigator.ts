import texts from "@lang/en.json";
import HomeNavigator from "@screens/HomeNavigator";
import Appointments from "@screens/Appointments";
import HomeIcon from "@assets/icons/home.svg";
import AppointmentsIcon from "@assets/icons/appointments.svg";
import { Params } from "@navigation/Router";
import Favorites from "@screens/Favorites";
import Heart from "@assets/icons/Heart";

const TabsNav: Partial<Record<keyof typeof Params, TabsInfo>> = {
    Home: {
        title: texts.navigation.tabs.homeNavigator.name,
        hasHeader: false,
        component: HomeNavigator,
        tabicon: HomeIcon
    },
    Appointments: {
        title: texts.navigation.tabs.appointments.name,
        hasHeader: true,
        component: Appointments,
        tabicon: AppointmentsIcon,
        requiresAuth: true
    },
    Favorites: {
        title: texts.navigation.tabs.favorites.name,
        hasHeader: true,
        component: Favorites,
        tabicon: Heart,
        requiresAuth: true
    },
};

export default TabsNav;

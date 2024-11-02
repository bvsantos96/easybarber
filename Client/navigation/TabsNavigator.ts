import texts from "@lang/en.json";
import HomeIcon from "@assets/icons/home.svg";
import AppointmentsIcon from "@assets/icons/appointments.svg";
import Heart from "@assets/icons/Heart";
import { Params } from "@navigation/Router";
import Favorites from "@screens/Favorites";
import Appointments from "@screens/Appointments";
import Home from "@screens/Home";

const TabsNav: Partial<Record<keyof typeof Params, TabsInfo>> = {
    Home: {
        title: texts.navigation.tabs.homeNavigator.name,
        hasHeader: false,
        component: Home,
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

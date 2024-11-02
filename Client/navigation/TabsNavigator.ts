import texts from "@lang/en.json";
import HomeNavigator from "@screens/HomeNavigator";
import AppointmentNavigator from "@screens/AppointmentNavigator";
import HomeIcon from "@assets/icons/home.svg";
import AppointmentsIcon from "@assets/icons/appointments.svg";
import Heart from "@assets/icons/Heart";
import { Params } from "@navigation/Router";
import FavoriteNavigator from "@screens/FavoritesNavigator";

const TabsNav: Partial<Record<keyof typeof Params, TabsInfo>> = {
    Home: {
        title: texts.navigation.tabs.homeNavigator.name,
        hasHeader: false,
        component: HomeNavigator,
        tabicon: HomeIcon
    },
    Appointments: {
        title: texts.navigation.tabs.appointments.name,
        hasHeader: false,
        component: AppointmentNavigator,
        tabicon: AppointmentsIcon,
        requiresAuth: true
    },
    Favorites: {
        title: texts.navigation.tabs.favorites.name,
        hasHeader: false,
        component: FavoriteNavigator,
        tabicon: Heart,
        requiresAuth: true
    },
};

export default TabsNav;

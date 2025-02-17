import texts from "@lang/en.json";
import EstablishmentsIcon from "@components/icons/EstablishmentsIcon";
import SchedulesIcon from "@components/icons/SchedulesIcon";
import SettingsIcon from "@components/icons/SettingsIcon";
import { Params, Routes } from "@navigation/Router";
import Appointments from "@screens/Appointments";
import Establishments from "@screens/Establishments";
import Schedules from "@screens/Schedules";
import AppointmentsIcon from "@components/icons/AppointmentsIcon";
import Settings from "@screens/Settings";
import NewIcon from "@components/icons/NewIcon";
import { NavigationProp } from "@react-navigation/native";
import Home from "@screens/Home";
import HomeIcon from "@components/icons/HomeIcon";
import { TabsVisibleConstraints } from "enums";
import ProductList from "@screens/ProductList";
import ProductListIcon from "@components/icons/ProductListIcon";

const TabsNav: Partial<Record<keyof typeof Params, TabsInfo>> = {
    Home: {
        title: texts.navigation.tabs.home.name,
        hasHeader: true,
        component: Home,
        tabicon: HomeIcon,
        requiresAuth: true,
        visibleConstraint: [TabsVisibleConstraints.HAS_ESTABLISHMENTS, TabsVisibleConstraints.AUTHENTICATED]
    },
    Establishments: {
        title: texts.navigation.tabs.establishments.name,
        hasHeader: true,
        component: Establishments,
        tabicon: EstablishmentsIcon,
        requiresAuth: true,
        leftIcon: EstablishmentsIcon,
        leftAction: (navigation) => {
            navigation.navigate(Routes.Settings);
        },
        leftText: texts.navigation.tabs.establishments.join,
        rightIcon: NewIcon,
        rightAction: (navigation: NavigationProp<any, any>) => {
            navigation.navigate(Routes.NewEstablishment);
        },
        rightText: texts.navigation.tabs.establishments.new,
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED]
    },
    Schedules: {
        title: texts.navigation.tabs.schedules.name,
        hasHeader: true,
        component: Schedules,
        tabicon: SchedulesIcon,
        requiresAuth: true,
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_ESTABLISHMENTS]
    },
    Appointments: {
        title: texts.navigation.tabs.appointments.name,
        hasHeader: true,
        component: Appointments,
        tabicon: AppointmentsIcon,
        requiresAuth: true,
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_ESTABLISHMENTS]
    },
    ProductList: {
        title: texts.navigation.tabs.products.name,
        hasHeader: true,
        component: ProductList,
        tabicon: ProductListIcon,
        requiresAuth: true,
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_ESTABLISHMENTS]
    },
    Settings: {
        title: texts.navigation.tabs.settings.name,
        hasHeader: true,
        component: Settings,
        tabicon: SettingsIcon,
        requiresAuth: true,
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_ESTABLISHMENTS]
    }
};

export default TabsNav;

import AppointmentsIcon from "@components/icons/AppointmentsIcon";
import EstablishmentsIcon from "@components/icons/EstablishmentsIcon";
import HomeIcon from "@components/icons/HomeIcon";
import NewEstablishment from "@components/icons/NewEstablishmentIcon";
import NewIcon from "@components/icons/NewIcon";
import ProductListIcon from "@components/icons/ProductListIcon";
import SchedulesIcon from "@components/icons/SchedulesIcon";
import ServiceIcon from "@components/icons/ServiceIcon";
import texts from "@lang/en.json";
import { Params, Routes } from "@navigation/Router";
import { NavigationProp } from "@react-navigation/native";
import Appointments from "@screens/Appointments";
import Establishments from "@screens/Establishments";
import Home from "@screens/Home";
import ProductList from "@screens/ProductList";
import SchedulesHome from "@screens/SchedulesHome";
import ServiceList from "@screens/ServiceList";
import { TabsVisibleConstraints } from "enums";

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
            navigation.navigate(Routes.EditEstablishment);
        },
        leftText: texts.navigation.tabs.establishments.join,
        rightIcon: NewEstablishment,
        rightAction: (navigation: NavigationProp<any, any>) => {
            navigation.navigate(Routes.NewEstablishment);
        },
        rightText: texts.navigation.tabs.establishments.new,
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED]
    },
    Services: {
        title: texts.navigation.tabs.services.name,
        hasHeader: true,
        component: ServiceList,
        tabicon: ServiceIcon,
        requiresAuth: true,
        rightIcon: NewIcon,
        rightAction: (navigation: NavigationProp<any, any>) => {
            navigation.navigate(Routes.Service, { service: undefined });
        },
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_ESTABLISHMENTS]
    },
    Schedules: {
        title: texts.navigation.tabs.schedules.name,
        hasHeader: false,
        component: SchedulesHome,
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
        visibleConstraint: [TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_ESTABLISHMENTS],
        rightIcon: NewIcon,
        rightAction: (navigation: NavigationProp<any, any>) => {
            navigation.navigate(Routes.EstablishmentSelection);
        }
    },
    ProductList: {
        title: texts.navigation.tabs.products.name,
        hasHeader: true,
        component: ProductList,
        tabicon: ProductListIcon,
        requiresAuth: true,
        visibleConstraint: [TabsVisibleConstraints.HAS_ESTABLISHMENTS, TabsVisibleConstraints.AUTHENTICATED, TabsVisibleConstraints.HAS_SELECTED_ESTABLISHMENT],
        rightIcon: NewIcon,
        rightAction: (navigation: NavigationProp<any, any>) => {
            navigation.navigate(Routes.Product, { product: undefined });
        },
    },
};

export default TabsNav;

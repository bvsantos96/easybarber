import texts from "@lang/en.json";
import Settings from "@screens/Settings";
import Availability from "@screens/Availability";
import EmployeeSelection from "@screens/EmployeeSelection";
import ServiceSelection from "@screens/ServiceSelection";
import EstablishmentDetails from "@screens/EstablishmentDetails";
import { Params } from "@navigation/Router";
import FavoriteHeader from "@components/FavoriteHeader";
import Appointments from "@screens/Appointments";

const AppointmentNav: Partial<Record<keyof typeof Params, StackInfo>> = {
    AppointmentsMain: {
        title: texts.navigation.tabs.homeNavigator.favorites.name,
        hasHeader: true,
        noGoBack: true,
        component: Appointments,
        containerizedComponent: false,
    },
    EstablishmentDetails: {
        title: texts.navigation.tabs.homeNavigator.establishmentDetails.name,
        hasHeader: true,
        component: EstablishmentDetails,
        containerizedComponent: false,
        secondHeader: FavoriteHeader
    },
    ServiceSelection: {
        title: texts.navigation.tabs.homeNavigator.serviceSelection.name,
        hasHeader: true,
        component: ServiceSelection,
        containerizedComponent: false,
    },
    EmployeeSelection: {
        title: texts.navigation.tabs.homeNavigator.employeeSelection.name,
        hasHeader: true,
        component: EmployeeSelection,
        containerizedComponent: false,
    },
    Availability: {
        title: texts.navigation.tabs.homeNavigator.schedule.name,
        hasHeader: true,
        component: Availability,
        containerizedComponent: false,
    },
    Settings: {
        title: texts.navigation.tabs.homeNavigator.settings.name,
        hasHeader: true,
        component: Settings,
        containerizedComponent: false
    }
};

export default AppointmentNav;

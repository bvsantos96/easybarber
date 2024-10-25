import texts from "@lang/en.json";
import Home from "@screens/Home";
import Settings from "@screens/Settings";
import Availability from "@screens/Availability";
import EmployeeSelection from "@screens/EmployeeSelection";
import ServiceSelection from "@screens/ServiceSelection";
import EstablishmentDetails from "@screens/EstablishmentDetails";
import { Params } from "@navigation/Router";


const HomeNav: Partial<Record<keyof typeof Params, StackInfo>> = {
    HomeMain: {
        title: texts.navigation.tabs.homeNavigator.home.name,
        hasHeader: false,
        component: Home,
        containerizedComponent: false,
    },
    EstablishmentDetails: {
        title: texts.navigation.tabs.homeNavigator.establishmentDetails.name,
        hasHeader: true,
        component: EstablishmentDetails,
        containerizedComponent: false,
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

export default HomeNav;

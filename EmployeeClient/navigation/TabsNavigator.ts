import texts from "@lang/en.json";
import EstablishmentsIcon from "@components/icons/EstablishmentsIcon";
import SchedulesIcon from "@components/icons/SchedulesIcon";
import SettingsIcon from "@components/icons/SettingsIcon";
import { Params } from "@navigation/Router";
import Appointments from "@screens/Appointments";
import Establishments from "@screens/Establishments";
import Schedules from "@screens/Schedules";
import AppointmentsIcon from "@components/icons/AppointmentsIcon";
import Settings from "@screens/Settings";

const TabsNav: Partial<Record<keyof typeof Params, TabsInfo>> = {
    Establishments: {
        title: texts.navigation.tabs.establishments.name,
        hasHeader: true,
        component: Establishments,
        tabicon: EstablishmentsIcon,
        requiresAuth: true
    },
    Schedules: {
        title: texts.navigation.tabs.schedules.name,
        hasHeader: true,
        component: Schedules,
        tabicon: SchedulesIcon,
        requiresAuth: true
    },
    Appointments: {
        title: texts.navigation.tabs.appointments.name,
        hasHeader: true,
        component: Appointments,
        tabicon: AppointmentsIcon,
        requiresAuth: true
    },
    Settings: {
        title: texts.navigation.tabs.settings.name,
        hasHeader: true,
        component: Settings,
        tabicon: SettingsIcon,
        requiresAuth: true
    }
};

export default TabsNav;

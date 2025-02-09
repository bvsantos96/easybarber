import { Route as MobileConfirmationRoute } from "@screens/MobileConfirmation";
import { Route as ResetPwdRoute } from "@screens/ResetPwd";
import { Route as EstablishmentRoute } from "@screens/Establishment";
import { Route as TimeSheetRoute } from "@screens/TimeSheet";

export const Params = {
    Sign: undefined,
    Loading: undefined,
    MobileConfirmation: {} as MobileConfirmationRoute,
    ForgotPwd: undefined,
    ResetPwd: {} as ResetPwdRoute,
    Tabs: undefined,
    Establishments: undefined,
    Schedules: undefined,
    Appointments: undefined,
    Settings: undefined,
    Establishment: {} as EstablishmentRoute,
    NewEstablishment: undefined,
    ProductList: undefined,
    TimeSheet: {} as TimeSheetRoute,
    WeekView: undefined,
    Employees: undefined,
} as const;

export const Routes = {
    Sign: "Sign",
    Loading: "Loading",
    MobileConfirmation: "MobileConfirmation",
    ForgotPwd: "ForgotPwd",
    ResetPwd: "ResetPwd",
    Tabs: "Tabs",
    Establishments: "Establishments",
    Schedules: "Schedules",
    Appointments: "Appointments",
    Settings: "Settings",
    Establishment: "Establishment",
    NewEstablishment: "NewEstablishment",
    ProductList: "ProductList",
    TimeSheet: "TimeSheet",
    WeekView: "WeekView",
    Employees: "Employees",
} as const;

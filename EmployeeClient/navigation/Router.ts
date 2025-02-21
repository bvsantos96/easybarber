import { Route as MobileConfirmationRoute } from "@screens/MobileConfirmation";
import { Route as ResetPwdRoute } from "@screens/ResetPwd";

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
    Establishment: undefined,
    NewEstablishment: undefined,
    ProductList: undefined,
    TimeSheet: undefined,
    Product: undefined,
    WeekView: undefined,
    Employees: undefined,
    Home: undefined,
    Services: undefined
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
    Product: "Product",
    WeekView: "WeekView",
    Employees: "Employees",
    Home: "Home",
    Services: "Services"
} as const;

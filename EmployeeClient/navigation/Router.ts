import { Route as MobileConfirmationRoute } from '@screens/MobileConfirmation';
import { Route as ResetPwdRoute } from '@screens/ResetPwd';

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
    Appointments: "Appointments"
} as const;

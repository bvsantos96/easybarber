import { Route as MobileConfirmationRoute } from '@screens/MobileConfirmation';
import { Route as ResetPwdRoute } from '@screens/ResetPwd';

export const Params = {
    Sign: undefined,
    Loading: undefined,
    MobileConfirmation: {} as MobileConfirmationRoute,
    ForgotPwd: undefined,
    ResetPwd: {} as ResetPwdRoute,
    Home: undefined,
} as const;

export const Routes = {
    Sign: "Sign",
    Loading: "Loading",
    MobileConfirmation: "MobileConfirmation",
    ForgotPwd: "ForgotPwd",
    ResetPwd: "ResetPwd",
    Home: "Home",
} as const;

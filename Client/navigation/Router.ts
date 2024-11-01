import { Route as MobileConfirmationRoute } from '@screens/MobileConfirmation';
import { Route as ResetPwdRoute } from '@screens/ResetPwd';
import { Route as EstablishmentDetailsRoute } from '@screens/EstablishmentDetails';
import { Route as ServiceSelectionRoute } from '@screens/ServiceSelection';
import { Route as EmployeeSelectionRoute } from '@screens/EmployeeSelection';
import { Route as AvailabilityRoute } from '@screens/Availability';

export const Params = {
    Onboarding: undefined,
    LocationRequest: undefined,
    AccountTypeSelection: undefined,
    Sign: undefined,
    Tabs: undefined,
    Loading: undefined,
    MobileConfirmation: {} as MobileConfirmationRoute,
    ForgotPwd: undefined,
    ResetPwd: {} as ResetPwdRoute,
    HomeMain: undefined,
    EstablishmentDetails: {} as EstablishmentDetailsRoute,
    ServiceSelection: {} as ServiceSelectionRoute,
    EmployeeSelection: {} as EmployeeSelectionRoute,
    Availability: {} as AvailabilityRoute,
    Settings: undefined,
    Home: undefined,
    Appointments: undefined,
    Favorites: undefined,
} as const;

export const Routes = {
    Onboarding: "Onboarding",
    LocationRequest: "LocationRequest",
    AccountTypeSelection: "AccountTypeSelection",
    Sign: "Sign",
    Tabs: "Tabs",
    Loading: "Loading",
    MobileConfirmation: "MobileConfirmation",
    ForgotPwd: "ForgotPwd",
    ResetPwd: "ResetPwd",
    Home: "Home",
    EstablishmentDetails: "EstablishmentDetails",
    ServiceSelection: "ServiceSelection",
    EmployeeSelection: "EmployeeSelection",
    Availability: "Availability",
    Settings: "Settings",
    HomeNavigator: "HomeNavigator",
    Appointments: "Appointments",
} as const;


// export const Routes = Object.keys(Params).reduce((acc, key) => {
//     acc[key as keyof typeof Params] = key as keyof typeof Params;
//     return acc;
// }, {} as Record<keyof typeof Params, keyof typeof Params>);

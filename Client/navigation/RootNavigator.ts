import texts from "@lang/en.json";
import Onboarding from "@screens/Onboarding";
import LocationRequest from "@screens/LocationRequest";
import AccountTypeSelection from "@screens/AccountTypeSelection";
import SignIn from "@screens/SignIn";
import Tabs from "@screens/Tabs";
import Loading from "@screens/Loading";
import MobileConfirmation from "@screens/MobileConfirmation";
import ForgotPwd from "@screens/ForgotPwd";
import ResetPwd from "@screens/ResetPwd";
import { Params } from "@navigation/Router";

const RootNav: Partial<Record<keyof typeof Params, StackInfo>> = {
    Onboarding: {
        title: texts.navigation.onBoarding.name,
        hasHeader: false,
        component: Onboarding,
        containerizedComponent: true,
    },
    LocationRequest: {
        title: texts.navigation.locationRequest.name,
        hasHeader: false,
        component: LocationRequest,
        containerizedComponent: false,
    },
    AccountTypeSelection: {
        title: texts.navigation.accountTypeSelection.name,
        hasHeader: false,
        component: AccountTypeSelection,
        containerizedComponent: true,
    },
    Sign: {
        title: texts.navigation.sign.name,
        hasHeader: false,
        component: SignIn,
        containerizedComponent: false,
    },
    Tabs: {
        title: texts.navigation.tabs.name,
        hasHeader: false,
        component: Tabs,
        containerizedComponent: false,
    },
    Loading: {
        title: texts.navigation.loading.name,
        hasHeader: false,
        component: Loading,
        containerizedComponent: true,
    },
    MobileConfirmation: {
        title: texts.navigation.mobileConfirmation.name,
        hasHeader: true,
        component: MobileConfirmation,
        containerizedComponent: false,
    },
    ForgotPwd: {
        title: texts.navigation.forgotPwd.name,
        hasHeader: false,
        component: ForgotPwd,
        containerizedComponent: false,
    },
    ResetPwd: {
        title: texts.navigation.resetPwd.name,
        hasHeader: false,
        component: ResetPwd,
        containerizedComponent: false,
    },
};

export default RootNav;

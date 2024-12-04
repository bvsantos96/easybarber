import texts from "@lang/en.json";
import SignIn from "@screens/SignIn";
import Loading from "@screens/Loading";
import MobileConfirmation from "@screens/MobileConfirmation";
import ForgotPwd from "@screens/ForgotPwd";
import ResetPwd from "@screens/ResetPwd";
import { Params } from "@navigation/Router";
import Tabs from "@screens/Tabs";
import Establishment from "@screens/Establishment";

const RootNav: Partial<Record<keyof typeof Params, StackInfo>> = {
    Sign: {
        title: texts.navigation.sign.name,
        hasHeader: false,
        component: SignIn,
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
        hasHeader: true,
        component: ForgotPwd,
        containerizedComponent: false,
    },
    ResetPwd: {
        title: texts.navigation.resetPwd.name,
        hasHeader: true,
        component: ResetPwd,
        containerizedComponent: false,
    },
    Tabs: {
        title: texts.navigation.tabs.name,
        hasHeader: false,
        component: Tabs,
        containerizedComponent: true,
    },
    Establishment: {
        title: texts.navigation.establishment.name,
        hasHeader: true,
        component: Establishment,
        containerizedComponent: true,
    },
};

export default RootNav;

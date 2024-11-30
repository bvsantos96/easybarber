import texts from "@lang/en.json";
import SignIn from "@screens/SignIn";
import Loading from "@screens/Loading";
import MobileConfirmation from "@screens/MobileConfirmation";
import ForgotPwd from "@screens/ForgotPwd";
import ResetPwd from "@screens/ResetPwd";
import { Params } from "@navigation/Router";

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
    Home: {
        title: texts.navigation.tabs.homeNavigator.home.name,
        hasHeader: true,
        component: Loading,
        containerizedComponent: true,
    },
};

export default RootNav;

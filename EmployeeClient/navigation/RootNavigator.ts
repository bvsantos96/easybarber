import texts from "@lang/en.json";
import SignIn from "@screens/SignIn";
import Loading from "@screens/Loading";
import MobileConfirmation from "@screens/MobileConfirmation";
import ForgotPwd from "@screens/ForgotPwd";
import ResetPwd from "@screens/ResetPwd";
import { Params } from "@navigation/Router";
import Tabs from "@screens/Tabs";
import Establishment from "@screens/Establishment";
import ProductList from "@screens/ProductList";
import Product from "@screens/Product";
import WeekView from "@screens/WeekView";
import TimeSheet from "@screens/TimeSheet";
import Employees from "@screens/Employees";
import AddButton from "@components/AddButton";

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
        containerizedComponent: false,
    },
    Establishment: {
        title: texts.navigation.establishment.name,
        hasHeader: true,
        component: Establishment,
        containerizedComponent: true,
    },
    NewEstablishment: {
        title: texts.navigation.establishment.newName,
        hasHeader: true,
        component: Establishment,
        containerizedComponent: true,
    },
    ProductList: {
        title: texts.navigation.product.name,
        hasHeader: true,
        component: ProductList,
        containerizedComponent: true,
    },
    Product: {
      title: texts.navigation.product.create,
      hasHeader: true,
      component: Product,
      containerizedComponent: true,
    },
    TimeSheet: {
        title: texts.navigation.timeSheet.name,
        hasHeader: true,
        component: TimeSheet,
        containerizedComponent: true,
    },
    WeekView: {
        title: texts.navigation.weekView.name,
        hasHeader: true,
        component: WeekView,
        containerizedComponent: true,
    },
    Employees: {
        title: texts.navigation.employees.name,
        hasHeader: true,
        component: Employees,
        containerizedComponent: true,
        secondHeader: AddButton
    }
};

export default RootNav;

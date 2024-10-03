import { getCategories } from "../utils/ApiRequest";
import { clearAll, getArray, store } from "./StorageUtils";
import { CATEGORY_STORAGE_KEY } from "../utils/Constants";
import { ALERT_TYPE } from "react-native-alert-notification";
import { Alert } from "@components/Alert";

// TODO: This function needs to validate if it gets a valid responses otherwise it needs to be retried
// if after that it fails we need to show an error message to the user, this is a critical function
export const loadLongTermItems = async (): Promise<Boolean> => {
    try {
        await clearAll();
        await store(CATEGORY_STORAGE_KEY, JSON.stringify(await getCategories()));
        return true;
    } catch (e) {
        if (e instanceof Error) {
            Alert({ type: ALERT_TYPE.DANGER, title: "Long term Items failed", message: e.message });
        } else if (typeof e === "string") {
            Alert({ type: ALERT_TYPE.DANGER, title: "Long term Items failed", message: e });
        } else if (typeof e === "object") {
            Alert({ type: ALERT_TYPE.DANGER, title: "Long term Items failed", message: JSON.stringify(e) });
        } else {
            Alert({ type: ALERT_TYPE.DANGER, title: "Long term Items failed", message: "Unknown error" });
        }
        return false;
    }
}

export const retrieveCategories = async (): Promise<ICategory[]> => {
    const categories = await getArray<ICategory>(CATEGORY_STORAGE_KEY);
    return categories;
}

export const loadOnLogin = async () => {
    try {
        // await store(LOCATIONS_STORAGE_KEY, JSON.stringify(await getLocations()));
    } catch (e) {
        console.error("Error loading login items", e);
    }
}

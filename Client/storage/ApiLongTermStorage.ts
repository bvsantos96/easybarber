import { ICategory } from "../declarations";
import { getCategories, getLocations } from "../utils/ApiRequest";
import { clearAll, getArray, store } from "./StorageUtils";
import { CATEGORY_STORAGE_KEY, LOCATIONS_STORAGE_KEY } from "../utils/Constants";

// TODO: This function needs to validate if it gets a valid responses otherwise it needs to be retried
// if after that it fails we need to show an error message to the user, this is a critical function
export const loadLongTermItems = async (): Promise<Boolean> => {
    try {
        await clearAll();
        await store(CATEGORY_STORAGE_KEY, JSON.stringify(await getCategories()));
        return true;
    } catch (e) {
        return false;
    }
}

export const retrieveCategories = async (): Promise<ICategory[]> => {
    const categories = await getArray<ICategory>(CATEGORY_STORAGE_KEY);
    return categories;
}

export const loadOnLogin = async () => {
    try {
        await store(LOCATIONS_STORAGE_KEY, JSON.stringify(await getLocations()));
    } catch (e) {
        console.error("Error loading login items", e);
    }
}

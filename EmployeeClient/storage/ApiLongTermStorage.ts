import { getServiceTypes } from "../utils/ApiRequest";
import { getArray, store } from "./StorageUtils";
import { CATEGORY_STORAGE_KEY } from "../utils/Constants";

// TODO: This function needs to validate if it gets a valid responses otherwise it needs to be retried
// if after that it fails we need to show an error message to the user, this is a critical function
export const loadLongTermItems = async (): Promise<Boolean> => {
    try {
        await store(CATEGORY_STORAGE_KEY, JSON.stringify(await getServiceTypes()));
        return true;
    } catch (e) {
        if (e instanceof Error) {
            console.error("Error loading long term items", e.message);
        } else if (typeof e === "string") {
            console.error("Error loading long term items", e);
        } else if (typeof e === "object") {
            console.error("Error loading long term items", JSON.stringify(e));
        } else {
            console.error("Error loading long term items.");
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
         //await store(LOCATIONS_STORAGE_KEY, JSON.stringify(await getLocations()));
    } catch (e) {
        console.error("Error loading login items", e);
    }
}

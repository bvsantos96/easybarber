import { ICategory } from "../declarations";
import { getCategories } from "../utils/ApiRequest";
import { clearAll, getArray, store } from "./StorageUtils";

// TODO: This function needs to validate if it gets a valid responses otherwise it needs to be retried
// if after that it fails we need to show an error message to the user, this is a critical function
export const loadLongTermItems = async (): Promise<Boolean> => {
    try {
        await clearAll();
        await store("categories", JSON.stringify(await getCategories()));
        return true;
    } catch (e) {
        return false;
    }
}

export const retrieveCategories = async (): Promise<ICategory[]> => {
    const categories = await getArray<ICategory>("categories");
    return categories;
}

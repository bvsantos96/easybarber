import { ICategory, ILocation } from "../declarations";
import { getCategories, getLocations, setNewLocation } from "../utils/ApiRequest";
import { clearAll, getArray, getArrayOrEmpty, store } from "./StorageUtils";
import { getLocation } from "../utils/Location";
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

export const getCurrentSelectedLocation = async (): Promise<ILocation | undefined> => {
    const locations = await getArrayOrEmpty<ILocation>(LOCATIONS_STORAGE_KEY);
    if (locations && locations.length === 0) {
        const location = await getLocation();
        return location
    }
    return undefined;
}

export const retrieveLocations = async (): Promise<ILocation[]> => {
    const locations = await getArrayOrEmpty<ILocation>(LOCATIONS_STORAGE_KEY);
    if (locations === null || locations === undefined || locations.length === 0) {
        locations.push(await getLocation());
        appendLocation(locations[0]);
    }
    return locations;
}

export const appendLocation = async (location: ILocation) => {
    try {
        await setNewLocation(location);
    } catch (e) {
        console.error("Error appending location", e);
        return;
    }

    let locations: ILocation[] = await getArrayOrEmpty<ILocation>(LOCATIONS_STORAGE_KEY);
    let index = -1;

    if (locations && locations.length > 0) {
        index = locations.findIndex((location) => location.latitude === location.latitude && location.longitude === location.longitude);
        locations.splice(index, 1);
    } else {
        locations = [];
    }

    locations = [location, ...locations];
    await store(LOCATIONS_STORAGE_KEY, JSON.stringify(locations));
}

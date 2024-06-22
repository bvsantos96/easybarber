import { LocationObject, LocationObjectCoords } from "expo-location";
import { ICategory, ILocation } from "../declarations";
import { getCategories, getLocations, setNewLocation } from "../utils/ApiRequest";
import { clearAll, getArray, getArrayOrEmpty, store } from "./StorageUtils";
import { getLocation } from "../utils/Location";

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

export const loadOnLogin = async () => {
    try {
        await store("locations", JSON.stringify(await getLocations()));
    } catch (e) {
        console.error("Error loading login items", e);
    }
}

export const retrieveLocations = async (): Promise<ILocation[]> => {
    const locations = await getArrayOrEmpty<ILocation>("locations");
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
        return;
    }

    let locations = await retrieveLocations();
    const index = locations.findIndex((location) => location.latitude === location.latitude && location.longitude === location.longitude);

    if (index !== -1)
        locations.splice(index, 1);

    locations = [location, ...locations];
    await store("locations", JSON.stringify(locations));
}

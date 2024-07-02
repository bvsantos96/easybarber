import * as Location from 'expo-location';
import { ILocation } from "../declarations";
import { getLocationsRequest, setNewLocation } from './ApiRequest';
import { getArrayFromPage, getArrayOrEmpty, store } from '../storage/StorageUtils';
import { LOCATIONS_STORAGE_KEY } from './Constants';

export async function getLocation(): Promise<ILocation> {
    try {
        const { status }: Location.PermissionResponse = await Location.requestForegroundPermissionsAsync();

        if (status !== 'granted') {
            throw new Error('Permission to access location was denied');
        }

        const coords = (await Location.getCurrentPositionAsync({ accuracy: Location.Accuracy.High })).coords;
        const address = await getAddressFromCoordinates(coords.latitude, coords.longitude);
        const location: ILocation = {
            id: 0,
            latitude: coords.latitude,
            longitude: coords.longitude,
            address: address ? address.name ?? "" : '',
            country: address ? address.country ?? "" : '',
            city: address ? address.city ?? "" : '',
            name: ""
        };
        return location;
    } catch (error) {
        console.error('Error getting location:', error);
        throw error;
    }
}

export const getAddressFromCoordinates = async (latitude: number, longitude: number): Promise<Location.LocationGeocodedAddress | undefined> => {
    try {
        const reverseGeocodedAddress = await Location.reverseGeocodeAsync({
            latitude,
            longitude,
        });

        return reverseGeocodedAddress[0];
    } catch (error) {
        console.error('Error reverse geocoding:', error);
        return undefined;
    }
};

export const selectLocation = async (location: ILocation): Promise<void> => {
    try {
        location.id = await setNewLocation(location);
        const locations = await getLocations();
        const index = locations.findIndex((l) => l.latitude === location.latitude && l.longitude === location.longitude);
        if (index !== -1) {
            locations.splice(index, 1);
        }
        locations.unshift(location);
        await store(LOCATIONS_STORAGE_KEY, JSON.stringify(locations));
    } catch (error) {
        console.error('Error selecting location:', error);
    }
}

export const saveLocation = async (location: ILocation, first = false): Promise<void> => {
    try {
        location.id = await setNewLocation(location);
        let locations: ILocation[] = [];
        if (!first) {
            locations = await getLocations();
            if (locations.length > 0) {
                const index = locations.findIndex((l) => l.latitude === location.latitude && l.longitude === location.longitude);
                if (index !== -1) {
                    locations.splice(index, 1);
                }
            }
        }
        locations.unshift(location);
        await store(LOCATIONS_STORAGE_KEY, JSON.stringify(locations));
    } catch (error) {
        console.error('Error saving location:', error);
    }
}

export const getLocations = async (): Promise<ILocation[]> => {
    let locations = await getArrayOrEmpty<ILocation>(LOCATIONS_STORAGE_KEY);
    if (locations.length > 0) {
        return locations;
    }

    locations = getArrayFromPage(await getLocationsRequest());
    if (locations.length > 0) {
        await store(LOCATIONS_STORAGE_KEY, JSON.stringify(locations));
        return locations;
    }

    locations[0] = await getLocation();
    saveLocation(locations[0], true);
    return locations;
}

export const getSelectedLocation = async (): Promise<ILocation> => {
    const locations = await getLocations();
    return locations[0];
}

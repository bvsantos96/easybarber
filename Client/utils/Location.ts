import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Location from 'expo-location';

export async function getLocation(): Promise<Location.LocationObject> {
    try {
        const { status }: Location.PermissionResponse = await Location.requestForegroundPermissionsAsync();

        if (status !== 'granted') {
            throw new Error('Permission to access location was denied');
        }

        return await Location.getCurrentPositionAsync({ accuracy: Location.Accuracy.High });
    } catch (error) {
        console.error('Error getting location:', error);
        throw error;
    }
}

export async function getCachedLocation(): Promise<Location.LocationObject> {
    try {
        if (cache.latitude && cache.longitude) {
            return {
                coords: {
                    latitude: cache.latitude,
                    longitude: cache.longitude,
                    altitude: 0,
                    accuracy: 0,
                    altitudeAccuracy: 0,
                    heading: 0,
                    speed: 0,
                },
                timestamp: Date.now(),
                mocked: false,
            };
        }
        else {
            return getLocation();
        }
    } catch (error) {
        console.error('Error getting location:', error);
        throw error;
    }
}

export const getCurrentAddress = async (): Promise<Location.LocationGeocodedAddress | undefined> => {
    try {
        const location = await getLocation();
        const { latitude, longitude } = location.coords;
        return await getAddressFromCoordinates(latitude, longitude);
    } catch (error) {
        console.error(error);
    }
}

interface CachedLocation {
    latitude: number;
    longitude: number;
    location: Location.LocationGeocodedAddress | undefined | null;
}

const cache: CachedLocation = {
    latitude: 0,
    longitude: 0,
    location: null,
};

const initializeCache = async () => {
    try {
        const cachedLatitude = await AsyncStorage.getItem('cachedLatitude');
        const cachedLongitude = await AsyncStorage.getItem('cachedLongitude');
        const cachedLocation = await AsyncStorage.getItem('cachedLocation');

        cache.latitude = cachedLatitude ? parseFloat(cachedLatitude) : 0;
        cache.longitude = cachedLongitude ? parseFloat(cachedLongitude) : 0;
        cache.location = cachedLocation ? JSON.parse(cachedLocation) : null;
    } catch (error) {
        console.error('Error initializing cache:', error);
    }
};

const saveCacheLocation = async (latitude: number, longitude: number, location: Location.LocationGeocodedAddress) => {
    cache.latitude = latitude;
    cache.longitude = longitude;
    cache.location = location;
    await AsyncStorage.setItem('cachedLatitude', cache.latitude.toString());
    await AsyncStorage.setItem('cachedLongitude', cache.longitude.toString());
    await AsyncStorage.setItem('cachedLocation', JSON.stringify(cache.location));
}

export const getCachedAddress = async (): Promise<Location.LocationGeocodedAddress | undefined> => {
    try {
        if (cache.latitude === 0 && cache.longitude === 0 && cache.location === null)
            await initializeCache();
        if (cache.location) {
            return cache.location;
        }

        const location = await getCachedLocation();
        const { latitude, longitude } = location.coords;
        return await getAddressFromCoordinates(latitude, longitude);
    } catch (error) {
        console.error('Error getting cached address:', error);
    }
}

export const getAddressFromCoordinates = async (latitude: number, longitude: number): Promise<Location.LocationGeocodedAddress | undefined> => {
    try {
        if (cache.latitude === 0 && cache.longitude === 0 && cache.location === null)
            await initializeCache();
        if (cache.latitude === latitude && cache.longitude === longitude && cache.location) {
            return cache.location;
        }

        const reverseGeocodedAddress = await Location.reverseGeocodeAsync({
            latitude,
            longitude,
        });

        saveCacheLocation(latitude, longitude, reverseGeocodedAddress[0]);
        return reverseGeocodedAddress[0];
    } catch (error) {
        console.error('Error reverse geocoding:', error);
        return undefined;
    }
};

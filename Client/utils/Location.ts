import * as Location from 'expo-location';
import { ILocation, IAddressSuggestion } from "../declarations";
import { getLocationsRequest, setNewLocation } from './ApiRequest';
import { getArrayFromPage, getArrayOrEmpty, store } from '../storage/StorageUtils';
import { LOCATIONS_STORAGE_KEY } from './Constants';
import useLocationStore from '../storage/stores/LocationStore';
import { Alert, Banner } from '../components/Alert';
import { ALERT_TYPE } from 'react-native-alert-notification';
import texts from '../langs/en.json';
import { Linking, Platform } from 'react-native';
import { Clipboard } from 'expo-clipboard';

export async function hasLocationPermission(): Promise<boolean> {
    try {
        const {
            hasLocationPermission,
            setRequestingLocationPermission,
        } = useLocationStore.getState();

        if (hasLocationPermission) {
            return hasLocationPermission;
        }

        setRequestingLocationPermission(true);
        return hasLocationPermission;
    } catch (error) {
        Alert({ type: ALERT_TYPE.INFO, title: "", message: texts.errors.locationPermissionError });
        console.error('Error getting location permission:', error);
        return false;
    }
}

export async function getLocation(): Promise<ILocation | null> {
    try {
        if (!(await hasLocationPermission())) {
            return null;
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
        Banner({ type: ALERT_TYPE.INFO, title: "", message: texts.errors.locationError });
        console.error('Error getting location:', error);
        throw error;
    }
}

export const setCountry = async (): Promise<void> => {
    const location: ILocation | null = await getLocation();
    if (location === null) {
        return;
    }
    useLocationStore.setState({ country: location.country });
}

export const getAddressFromCoordinates = async (latitude: number, longitude: number): Promise<Location.LocationGeocodedAddress | undefined> => {
    try {
        const reverseGeocodedAddress = await Location.reverseGeocodeAsync({
            latitude,
            longitude,
        });

        return reverseGeocodedAddress[0];
    } catch (error) {
        Banner({ type: ALERT_TYPE.WARNING, title: "", message: texts.errors.reverseGeocodingError });
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
        Banner({ type: ALERT_TYPE.DANGER, title: "", message: texts.errors.selectLocationError });
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
        Banner({ type: ALERT_TYPE.DANGER, title: "", message: texts.errors.saveLocationError });
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

    const location = await getLocation();
    if (location === null) {
        return [];
    }
    locations[0] = location;
    saveLocation(locations[0], true);
    return locations;
}

export const getSelectedLocation = async (): Promise<ILocation> => {
    const {
        getSelectedLocation
    } = useLocationStore.getState();
    const location = await getSelectedLocation();
    if (location === undefined) {
        throw new Error('No location selected');
    }
    return location;
}

const blacklist = [
    // Portuguese
    'rua', 'avenida', 'estrada', 'travessa', 'praça', 'alameda', 'largo', 'rodovia',
    // English
    'street', 'road', 'avenue', 'boulevard', 'lane', 'drive', 'way', 'court', 'place', 'square', 'circle', 'highway', 'parkway', 'plaza', 'trail', 'terrace',
    // German
    'straße', 'weg', 'platz', 'allee', 'gasse', 'ring', 'chaussee',
    // Spanish
    'calle', 'avenida', 'camino', 'plaza', 'carrer', 'bulevar', 'carretera', 'pasaje', 'ronda',
    // Italian
    'via', 'viale', 'piazza', 'corso', 'largo', 'strada', 'vicolo', 'rotatoria',
    // French
    'rue', 'avenue', 'boulevard', 'chemin', 'place', 'route', 'cours', 'voie', 'allée', 'impasse', 'quai',
    // Polish
    'ulica', 'plac', 'aleja', 'droga', 'osiedle',
    // Dutch
    'straat', 'laan', 'weg', 'plein', 'gracht', 'dreef', 'kade', 'pad',
    // Russian
    'улица', 'проспект', 'площадь', 'бульвар', 'переулок', 'шоссе',
    // Chinese (Simplified)
    '街道', '大道', '路', '广场', '巷', '胡同', '环路',
    // Japanese
    '通り', '道路', '街', '区', '丁目',
    // Arabic
    'شارع', 'طريق', 'ميدان', 'زقاق', 'حارة', 'جادة',
    // Hindi
    'सड़क', 'मार्ग', 'चौक', 'गल्ली', 'पथ', 'राजमार्ग',
    // Other
    'route', 'trunk', 'motorway', 'expressway', 'freeway'
    // Add more terms as needed
];

export const suggestionsInputValidation = (address: string): boolean => {
    if (address.length < 3) {
        return false;
    }

    let size = address.length
    const words = address.split(' ');
    for (const word of words) {
        if (blacklist.includes(word.toLowerCase())) {
            size -= word.length + 1;
        }
    }

    return size >= 3;
}

export const fetchSuggestions = async (address: string): Promise<ILocation[]> => {
    try {
        const {
            country
        } = useLocationStore.getState();

        let localSuggestions: IAddressSuggestion[] = [];

        const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(address)}&format=json&addressdetails=1`;
        if (country) {
            const response = await fetch(
                `${url}&countrycodes=${country}&limit=3`
            );
            localSuggestions = await response.json();
        }

        let suggestionsHash: Set<string> = new Set();
        let suggestions: ILocation[] = [];
        const globalSugestions: IAddressSuggestion[] = await fetch(`${url}&limit=${5 - localSuggestions.length}`).then(response => response.json())
        if (localSuggestions.length > 0) {
            appendUniqueSuggestions(suggestions, localSuggestions, suggestionsHash);
        }
        appendUniqueSuggestions(suggestions, globalSugestions, suggestionsHash);
        return suggestions
    } catch (error) {
        Banner({ type: ALERT_TYPE.DANGER, title: "", message: texts.errors.fetchSuggestionsError });
        console.error('Error fetching suggestions:', error);
        return [];
    }
}

const appendUniqueSuggestions = async (suggestions: ILocation[], newSuggestions: IAddressSuggestion[], hash: Set<string>): Promise<ILocation[]> => {
    for (const suggestion of newSuggestions) {
        const key = `${suggestion.address.road}$$$${suggestion.address.city}$$$${suggestion.address.country}`;
        if (hash.has(key)) {
            continue;
        }
        suggestions.push({
            id: hash.size,
            latitude: parseFloat(suggestion.lat),
            longitude: parseFloat(suggestion.lon),
            address: suggestion.address.road ?? suggestion.display_name,
            country: suggestion.address.country,
            city: suggestion.address.city,
            name: ""
        });
        hash.add(key);
    }
    return suggestions;
}

export const gotoLocation = async (address: string, lat: number, lng: number): Promise<void> => {
    const scheme = Platform.select({ ios: 'maps://0,0?q=', android: 'geo:0,0?q=' });
    const latLng = `${lat},${lng}`;
    const label = 'Custom Label';
    const url = Platform.select({
        ios: `${scheme}${label}@${latLng}`,
        android: `${scheme}${latLng}(${label})`
    });
    if (url) {
        Linking.openURL(url);
    } else {
        await Clipboard.setStringAsync(address);
        Alert({ type: ALERT_TYPE.INFO, title: "", message: texts.errors.openMapsError });
    }
}

import { create } from 'zustand';
import { ILocation } from '../../declarations';
import { getLocation } from '../../utils/Location';
import { getArrayFromPage } from '../StorageUtils';
import { getLocationsRequest, setNewLocation } from '../../utils/ApiRequest';

interface LocationState {
    hasMoreLocations: boolean;
    locations: ILocation[];
    selectedLocation: ILocation | undefined;
    setLocations: (locations: ILocation[]) => void;
    addLocations: (locations: ILocation[]) => void;
    selectLocation: (location: ILocation) => Promise<void>;
    selectLocationIdx: (idx: number) => void;
    getSelectedLocation: () => Promise<ILocation | undefined>;
    clearLocations: () => void;
    country: string;
}

const useLocationStore = create<LocationState>()(
    (set) => ({
        country: "",
        hasMoreLocations: true,
        locations: [],
        selectedLocation: undefined,
        setLocations: (locations) => {
            if (locations.length === 0)
                return set({ hasMoreLocations: true, locations: locations });
            return set({ locations: locations })
        },
        addLocations: (locations) => {
            set((state: LocationState) => ({
                locations: state.locations.concat(locations),
                selectedLocation: state.selectedLocation || locations[0]
            }));
        },
        selectLocation: async (location: ILocation) => {
            location.id = await setNewLocation(location);
            return set((state: LocationState) => {
                const idx = state.locations.findIndex(loc => loc.id === location.id);
                if (idx > -1) {
                    state.locations.splice(idx, 1);
                }
                state.locations.unshift(location);
                return {
                    locations: state.locations,
                    selectedLocation: location,
                };
            }
            )
        },
        selectLocationIdx: (idx: number) => set((state: LocationState) => {
            const location = state.locations.splice(idx, 1)[0];
            if (location === undefined) return state;
            state.locations.unshift(location);
            return {
                locations: state.locations,
                selectedLocation: location,
            };
        }),
        getSelectedLocation: async () => {
            const state: LocationState = useLocationStore.getState();
            if (state.selectedLocation === undefined) {
                let locations = getArrayFromPage(await getLocationsRequest());
                if (locations.length <= 0) {
                    locations[0] = await getLocation();
                    locations[0].id = await setNewLocation(locations[0]);
                }
                useLocationStore.setState({ locations: locations, selectedLocation: locations[0] });
                return locations[0];
            }
            return state.selectedLocation;
        },
        clearLocations: () => set({ hasMoreLocations: true, locations: [] }),
    }));

export default useLocationStore;

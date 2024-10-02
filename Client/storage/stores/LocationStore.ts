import { create } from 'zustand';
import { setNewLocation } from '../../utils/ApiRequest';

interface LocationState {
    hasMoreLocations: boolean;
    locations: ILocation[];
    selectedLocation: ILocation | undefined;
    setLocations: (locations: ILocation[]) => void;
    addLocations: (locations: ILocation[]) => void;
    selectLocation: (location: ILocation) => Promise<void>;
    selectLocationIdx: (idx: number) => void;
    clearLocations: () => void;
    country: string | undefined;
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
        clearLocations: () => set({ hasMoreLocations: true, locations: [] }),
    }));

export default useLocationStore;

import { create } from 'zustand';
import { getLocation } from '../../utils/Location';
import { getArrayFromPage } from '../StorageUtils';
import { getLocationsRequest, setNewLocation } from '../../utils/ApiRequest';
import { AlertProps, AlertType } from '@components/Alert';

interface LocationState {
    hasMoreLocations: boolean;
    locations: ILocation[];
    selectedLocation: ILocation | undefined;
    setLocations: (locations: ILocation[]) => void;
    addLocations: (locations: ILocation[]) => void;
    selectLocationIdx: (idx: number) => void;
    clearLocations: () => void;
    country: string | undefined;
    alertProps:AlertProps;
    alert: (alertProps:AlertProps) => void;
    alertVisible:boolean;
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
        alertProps: {
            message: "message", 
            onPress: ()=>{alert("Pressed")},
            buttonText: "Btn text",
            type: AlertType.Success
        },
        alert: (_alertProps:AlertProps) =>
            set((state: LocationState) => {
                return{ alertVisible: !state.alertVisible, alertProps: _alertProps };
            }),
        alertVisible:false
    }));
    

export default useLocationStore;

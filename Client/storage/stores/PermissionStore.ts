import { create } from 'zustand';

interface PermissionState {
    requestingLocationPermission: boolean | undefined;
    setRequestingLocationPermission: (requestingLocationPermission: boolean) => void;
    hasLocationPermission: boolean;
    setHasLocationPermission: (hasLocationPermission: boolean) => void;
}

const usePermissionStore = create<PermissionState>()(
    (set) => ({
        requestingLocationPermission: undefined,
        setRequestingLocationPermission: (requestingLocationPermission: boolean) => {
            return set({ requestingLocationPermission })
        },
        hasLocationPermission: false,
        setHasLocationPermission: (hasLocationPermission: boolean) => set({ hasLocationPermission }),
    })
);

export default usePermissionStore;

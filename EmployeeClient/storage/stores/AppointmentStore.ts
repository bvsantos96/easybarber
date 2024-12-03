import { create } from "zustand";

interface AppointmentStore {
    updateAppointments: boolean;
    resetUpdateAppointments: () => void;
    hash: string;
    validateHash: (hash: string) => void;
}

const useAppointmentStore = create<AppointmentStore>(
    (set) => ({
        updateAppointments: false,
        resetUpdateAppointments: () => set({ updateAppointments: false }),
        hash: '',
        validateHash: (_hash: string) => set((state: AppointmentStore) => (
            { updateAppointments: !(state.hash === '' || state.hash === _hash), hash: _hash }))
    })
);

export default useAppointmentStore;

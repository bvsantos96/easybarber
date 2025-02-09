import { create } from 'zustand';

interface EstablishmentState {
    establishments: EstablishmentBase[];
    selectedEstablishment: SelectedItem | undefined;
    setEstablishments: (establishments: EstablishmentBase[]) => void;
}

const useEstablishmentStore = create<EstablishmentState>()(
    (set) => ({
        establishments: [],
        selectedEstablishment: undefined,
        setEstablishments: (establishments: EstablishmentBase[]) =>
            set(() => {
                return { establishments: establishments, selectedEstablishment: establishments.length === 1 ? { id: establishments[0].id, idx: 0 } : undefined };
            })
    })
);

export default useEstablishmentStore;

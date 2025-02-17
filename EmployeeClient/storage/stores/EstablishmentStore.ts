import { create } from 'zustand';

interface EstablishmentState {
    establishments: EstablishmentBase[];
    selectedEstablishment: SelectedItem | undefined;
    setEstablishments: (establishments: EstablishmentBase[]) => void;
    setSelectedEstablishment: (selectedEstablishment: EstablishmentInfo) => void;
}

const useEstablishmentStore = create<EstablishmentState>()(
    (set, get) => ({
        establishments: [],
        selectedEstablishment: undefined,
        setEstablishments: (establishments: EstablishmentBase[]) =>
            set(() => {
                return {
                    establishments: establishments,
                    selectedEstablishment: establishments.length === 1
                        ? { id: establishments[0].id, idx: 0, admin: establishments[0].admin }
                        : undefined
                };
            }),
        setSelectedEstablishment: (selectedEstablishment: EstablishmentInfo) => {
            if (!selectedEstablishment) return;

            const idx = get().establishments.findIndex(
                est => est.id === selectedEstablishment.id
            );

            if (idx === -1) {
                console.warn('Establishment not found');
                return;
            }

            set(() => ({
                selectedEstablishment: {
                    id: selectedEstablishment.id,
                    idx,
                    admin: selectedEstablishment.admin
                }
            }));
        }
    })
);

export default useEstablishmentStore;

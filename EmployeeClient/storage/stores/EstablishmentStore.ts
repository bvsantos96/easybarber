import { create } from 'zustand';

interface EstablishmentState {
    establishments: EstablishmentBase[];
    selectedEstablishment: SelectedItem | undefined;
    setEstablishments: (establishments: EstablishmentBase[]) => void;
    setSelectedEstablishment: (selectedEstablishment: EstablishmentInfo | EstablishmentBase | SelectedItem | undefined) => void;
    clearSelectedEstablishment: () => void;
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
                        ? { id: establishments[0].id, idx: 0, admin: establishments[0].admin, name: establishments[0].name }
                        : undefined
                };
            }),
        setSelectedEstablishment: (selectedEstablishment: EstablishmentInfo | EstablishmentBase | SelectedItem | undefined) => {
            if (!selectedEstablishment)
                return ({ selectedEstablishment: undefined });

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
                    admin: selectedEstablishment.admin,
                    name: selectedEstablishment.name
                }
            }));
        },
        clearSelectedEstablishment: () => set(() => {
            if (get().establishments.length === 1) {
                const establishment = get().establishments[0];
                return ({ selectedEstablishment: { id: establishment.id, idx: 0, admin: establishment.admin, name: establishment.name } });
            }
            return ({ selectedEstablishment: undefined });
        })
    })
);

export default useEstablishmentStore;

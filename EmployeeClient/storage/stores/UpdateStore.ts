import { create } from 'zustand';

interface UpdateStore {
    toUpdate?: ServiceActionType;
    setToUpdate: (_toUpdate: ServiceActionType) => void;
    clearToUpdate: () => void;
}

const useUpdateStore = create<UpdateStore>(
    (set) => ({
        toUpdate: undefined,
        setToUpdate: (_toUpdate: ServiceActionType) => set({ toUpdate: _toUpdate }),
        clearToUpdate: () => set({
            toUpdate: undefined
        })
    })
);

export default useUpdateStore;

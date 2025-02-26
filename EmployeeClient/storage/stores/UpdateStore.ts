import { create } from 'zustand';

interface UpdateStore {
    toUpdate?: any;
    setToUpdate: (_toUpdate: any) => void;
    clearToUpdate: () => void;
}

const useUpdateStore = create<UpdateStore>(
    (set) => ({
        toUpdate: undefined,
        setToUpdate: (_toUpdate: any) => set({ toUpdate: _toUpdate }),
        clearToUpdate: () => set({
            toUpdate: undefined
        })
    })
);

export default useUpdateStore;

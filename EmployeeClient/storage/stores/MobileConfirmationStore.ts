import { create } from 'zustand';

interface MobileConfirmationStore {
    blockTime?: number | null;
    setBlockTime: (time?: number) => void;
    resetBlockTime: () => void;
}

const useMobileConfirmationStore = create<MobileConfirmationStore>()(
    (set) => ({
        blockTime: null,
        setBlockTime: (time?: number) => set({ blockTime: time }),
        resetBlockTime: () => set({ blockTime: null })
    })
);

export default useMobileConfirmationStore;

import { create } from 'zustand';

interface HeaderStore {
    pressed: boolean | undefined;
    onPress: () => void;
    reset: () => void;
}

const useHeaderStore = create<HeaderStore>(
    (set, get) => ({
        pressed: undefined,
        onPress: () => set({ pressed: !get().pressed }),
        reset: () => set({ pressed: undefined }),
    })
);

export default useHeaderStore;

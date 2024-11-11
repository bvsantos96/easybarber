import { create } from 'zustand';

interface AuthStore {
    doLogout: boolean;
    toggleDoLogout: () => void;
}

const useAuthStore = create<AuthStore>(
    (set, get) => ({
        doLogout: false,
        toggleDoLogout: () => set({ doLogout: !get().doLogout }),
    }));

export default useAuthStore;

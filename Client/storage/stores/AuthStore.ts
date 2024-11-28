import { create } from 'zustand';

interface AuthStore {
    token?: string;
    setToken: (token: string) => void;
    doLogout?: boolean;
    toggleDoLogout: () => void;
}

const useAuthStore = create<AuthStore>(
    (set, get) => ({
        token: undefined,
        setToken: (token: string) => set({ token }),
        doLogout: undefined,
        toggleDoLogout: () => set({ doLogout: !get().doLogout, token: undefined }),
    }));

export default useAuthStore;

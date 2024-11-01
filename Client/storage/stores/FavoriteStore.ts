import { create } from "zustand";

interface FavoriteStore {
    updateFavorites: boolean;
    setUpdateFavorites: (value: boolean) => void;
}

const useFavoriteStore = create<FavoriteStore>(
    (set) => ({
        updateFavorites: false,
        setUpdateFavorites: (value: boolean) => set({ updateFavorites: value }),
    }));

export default useFavoriteStore;

import { create } from "zustand";

interface FavoriteStore {
    updateFavorites: boolean;
    setUpdateFavorites: (value: boolean) => void;
    favorites?: number[];
    setFavorites: (value: number[]) => void;
}

const useFavoriteStore = create<FavoriteStore>(
    (set) => ({
        updateFavorites: false,
        setUpdateFavorites: (value: boolean) => set({ updateFavorites: value }),
        favorites: undefined,
        setFavorites: (value?: number[]) => set({ favorites: value }),
    }));

export default useFavoriteStore;

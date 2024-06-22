import AsyncStorage from '@react-native-async-storage/async-storage';
import * as FileSystem from 'expo-file-system';

export const store = async (id: string, data: string) => {
    await AsyncStorage.setItem(id, data);
}

export const downloadToDevice = async (id: string, url: string): Promise<string> => {
    const fileUri = FileSystem.documentDirectory + id;
    try {
        const fileInfo = await FileSystem.getInfoAsync(fileUri);
        if (fileInfo.exists) {
            await FileSystem.deleteAsync(fileUri);
        }
        const downloadResult = await FileSystem.downloadAsync(url, fileUri);
        return downloadResult.uri;
    } catch (error) {
        throw error;
    }
}

export const clearItem = async (id: string) => {
    await AsyncStorage.removeItem(id);
}

export const retrieve = async (id: string): Promise<string> => {
    const result = await AsyncStorage.getItem(id);
    if (result) {
        return JSON.parse(result);
    }
    throw new Error("Item not found");
}

export const clearAll = async () => {
    await AsyncStorage.clear();
}

export const getArrayOrEmpty = async <T>(id: string): Promise<T[]> => {
    try {
        return await getArray(id);
    } catch (error) {
        return [];
    }
}

export const getArray = async <T>(id: string): Promise<T[]> => {
    const result = await retrieve(id);
    if (Array.isArray(result)) {
        return result as T[];
    }
    // TODO: If the item is not present in the storage and it is part of the required long term items we should try and load it
    // await loadLongTermItems();
    // return getArray(id);
    throw new Error(`getArray: is not an array: ${result}`);
}

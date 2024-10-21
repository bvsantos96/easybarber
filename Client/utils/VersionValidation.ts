import AsyncStorage from '@react-native-async-storage/async-storage';
import Constants from 'expo-constants';
import { getApiVersion } from './ApiRequest';
import { loadLongTermItems } from '../storage/ApiLongTermStorage';
import { UpdateType } from '../enums';
import { AlertType } from '../components/Alert';
import texts from '../langs/en.json';
import useAlertStore from 'storage/stores/AlertStore';

export const expoVersion = Constants.expoConfig?.version;

export const saveVersion = async (version: string) => {
    await AsyncStorage.setItem('version', version);
}

const updateNeeded = (currentVersion: string, newVersion: string): UpdateType => {
    const [currentMajor, currentMinor, currentPatch] = currentVersion.split('.').map(Number);
    const [newMajor, newMinor, newPatch] = newVersion.split('.').map(Number);

    if (newMajor > currentMajor) {
        return UpdateType.MAJOR;
    } else if (newMinor > currentMinor) {
        return UpdateType.MINOR;
    } else if (newPatch > currentPatch) {
        return UpdateType.PATCH;
    } else {
        return UpdateType.NONE;
    }
}

const getLocalApiVersion = async (): Promise<string> => {
    return await AsyncStorage.getItem('version') || Constants.expoConfig?.version || '0.0.0';
}

export const validateVersion = async () => {
    const {
        alert
    } = useAlertStore.getState();

    let updateType: UpdateType;
    try {
        updateType = updateNeeded(await getLocalApiVersion(), await getApiVersion());
    } catch (e) {
        alert({ type: AlertType.Error, message: texts.errors.versionCheckFailed });
        console.error(e);
        return UpdateType.FAILED;
    }

    switch (updateType) {
        case UpdateType.PATCH:
            const updateSuccess = await loadLongTermItems();
            if (updateSuccess)
                saveVersion(await getApiVersion());
            else
                return UpdateType.FAILED;
            break;
    }
    return updateType;
}

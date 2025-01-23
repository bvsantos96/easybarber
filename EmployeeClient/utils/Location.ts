import { AlertType } from '../components/Alert';
import texts from '../langs/en.json';
import { Linking, Platform } from 'react-native';
import * as expoClipboard from 'expo-clipboard';
import useAlertStore from 'storage/stores/AlertStore';

export const gotoLocation = async (label: string, address: string, lat: number, lng: number): Promise<void> => {
    const {
        alert
    } = useAlertStore.getState();

    const scheme = Platform.select({ ios: 'maps://0,0?q=', android: 'geo:0,0?q=' });
    const latLng = `${lat},${lng}`;
    const url = Platform.select({
        ios: `${scheme}${label}@${latLng}`,
        android: `${scheme}${latLng}(${label})`
    });
    if (url) {
        Linking.openURL(url);
    } else {
        await expoClipboard.setStringAsync(address);
        alert({ type: AlertType.Info, message: texts.errors.openMapsError });
    }
}

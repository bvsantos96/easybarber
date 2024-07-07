import { ALERT_TYPE, Dialog, Toast } from 'react-native-alert-notification';

export const Alert = ({ type, title, message, onPress, buttonText }: { type: ALERT_TYPE, title?: string, message: string, onPress?: () => void, buttonText?: string }) => {
    const texts = require('../langs/en.json');
    return Dialog.show({
        type: type,
        title: title || type,
        textBody: message,
        onPressButton: onPress || Dialog.hide,
        button: buttonText || texts.close
    })
}

export const Banner = ({ type, title, message, onPress }: { type: ALERT_TYPE, title?: string, message: string, onPress?: () => void }) =>
    Toast.show({
        type: type,
        title: title || type,
        textBody: message,
        onPress: onPress || Toast.hide,
    });

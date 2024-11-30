import { AlertProps, AlertType } from '@components/Alert';
import { create } from 'zustand';

interface AlertState {
    alertProps: AlertProps;
    alert: (alertProps: AlertProps) => void;
    alertVisible: boolean;
}

const useAlertStore = create<AlertState>()(
    (set) => ({
        alertProps: {
            message: "message",
            onPress: () => { alert("Pressed") },
            buttonText: "Btn text",
            type: AlertType.Success
        },
        alert: (_alertProps: AlertProps) =>
            set((state: AlertState) => {
                return { alertVisible: !state.alertVisible, alertProps: _alertProps };
            }),
        alertVisible: false
    })
);

export default useAlertStore;

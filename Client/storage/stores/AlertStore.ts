import { AlertProps, AlertType } from '@components/Alert';
import { BannerProps } from '@components/Banner';
import { create } from 'zustand';

interface AlertState {
    alertProps: AlertProps;
    alert: (alertProps: AlertProps) => void;
    alertVisible: boolean;
    setAlertVisible: (visible: boolean) => void;
    bannerProps: BannerProps;
    banner: (bannerProps: BannerProps) => void;
    bannerVisible: boolean;
    setBannerVisible: (visible: boolean) => void;
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
        alertVisible: false,
        setAlertVisible: (visible: boolean) =>
            set(() => {
                return { alertVisible: visible };
            }),
        bannerProps: {
            message: "message",
            visible: false,
            setVisible: () => { },
        },
        banner: (_bannerProps: BannerProps) =>
            set(() => {
                return { bannerVisible: true, bannerProps: _bannerProps };
            }),
        bannerVisible: false,
        setBannerVisible: (visible: boolean) =>
            set(() => {
                return { bannerVisible: visible };
            })
    })
);

export default useAlertStore;

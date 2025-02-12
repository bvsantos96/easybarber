import { ReactNode, useEffect } from "react";
import { Keyboard, Platform } from "react-native";

export default function KeyboardAwareView({ children, onKeyboardHide, onKeyboardShow, setKeyboardHeight }: { children?: ReactNode, onKeyboardHide?: Function, onKeyboardShow?: Function, setKeyboardHeight?: Function }) {
    useEffect(() => {
        const _onKeyboardShow = (e: any) => {
            const height: number = e.endCoordinates.height;
            setKeyboardHeight && setKeyboardHeight(height);
            onKeyboardShow && onKeyboardShow();
        }

        const _onKeyboardHide = () => {
            setKeyboardHeight && setKeyboardHeight(0);
            onKeyboardHide && onKeyboardHide();
        };


        if (Platform.OS === "android") {
            const changeFrame = Keyboard.addListener('keyboardDidChangeFrame', _onKeyboardShow);
            const hideListener = Keyboard.addListener('keyboardDidHide', _onKeyboardHide);
            return () => {
                changeFrame.remove();
                hideListener.remove();
            };
        } else {
            const changeFrame = Keyboard.addListener('keyboardWillChangeFrame', _onKeyboardShow);
            const hideListener = Keyboard.addListener('keyboardWillHide', _onKeyboardHide);
            return () => {
                changeFrame.remove();
                hideListener.remove();
            };
        }

    }, []);
    return children ? children : null;
}

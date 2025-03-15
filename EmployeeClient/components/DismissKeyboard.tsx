import React from 'react';
import { Keyboard, TouchableWithoutFeedback, View } from 'react-native';

interface DismissKeyboardProps {
    children: React.ReactNode;
}

const DismissKeyboard: React.FC<DismissKeyboardProps> = ({ children }) => {
    const handlePress = () => {
        Keyboard.dismiss();
    };

    return (
        <TouchableWithoutFeedback onPress={handlePress}>
            <View style={{ flex: 1 }}>{children}</View>
        </TouchableWithoutFeedback>
    );
};

export default DismissKeyboard;

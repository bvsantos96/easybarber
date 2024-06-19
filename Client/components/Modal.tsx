import React, { useRef, useState } from 'react';
import { View, StyleSheet, TouchableWithoutFeedback, StyleProp, ViewStyle } from 'react-native';
import { BottomSheetBackdrop, BottomSheetModal, BottomSheetScrollView } from '@gorhom/bottom-sheet';
import { useSharedValue } from 'react-native-reanimated';
import { useTheme } from '../styles/ThemeContext';
import Pressable from './Pressable';

type CustomModalProps = {
    children: React.ReactNode;
    modalContent: React.ReactNode;
    modalHeight: number;
    buttonStyle?: StyleProp<ViewStyle>;
};

const CustomModal: React.FC<CustomModalProps> = ({ children, modalContent, modalHeight, buttonStyle }) => {
    const theme = useTheme();
    const [isVisible, setIsVisible] = useState(false);
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const sharedVal = useSharedValue(0);
    const modalBottomPadding = 10 * theme.dimensions.absoluteHeight;
    const [modalContentHeight, setModalContentHeight] = useState(modalHeight ? modalHeight + modalBottomPadding : 0);

    const toggleModal = () => {
        if (isVisible) {
            bottomSheetModalRef.current?.dismiss();
        } else {
            bottomSheetModalRef.current?.present();
        }
        setIsVisible(!isVisible);
    };

    const backdropProps = {
        closeOnPress: true,
        appearsOnIndex: 0,
        disappearsOnIndex: -1,
        enableTouchThrough: false,
    };

    return (
        <>
            <Pressable style={buttonStyle} onPress={toggleModal}>
                {children}
            </Pressable>
            <BottomSheetModal
                ref={bottomSheetModalRef}
                onDismiss={() => setIsVisible(false)}
                enableDynamicSizing
                backdropComponent={() => (
                    <BottomSheetBackdrop
                        animatedIndex={sharedVal}
                        animatedPosition={sharedVal}
                        {...backdropProps}
                    />
                )}
            >
                <BottomSheetScrollView >
                    <View style={{ "height": modalContentHeight }} >
                        {modalContent}
                    </View>
                </BottomSheetScrollView>
            </BottomSheetModal >
        </>
    );
};

export default CustomModal;

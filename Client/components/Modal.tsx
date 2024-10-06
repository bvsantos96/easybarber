import React, { useImperativeHandle, useRef, useState } from 'react';
import { View, StyleProp, ViewStyle } from 'react-native';
import { BottomSheetModal, BottomSheetBackdrop, BottomSheetView } from '@gorhom/bottom-sheet/src';
import { useSharedValue } from 'react-native-reanimated';
import { useTheme } from '../styles/ThemeContext';
import Pressable from './Pressable';

type CustomModalProps = {
    children: React.ReactNode;
    modalContent: React.ReactNode;
    modalHeight: number;
    buttonStyle?: StyleProp<ViewStyle>;
};

export interface CustomModalRef {
    toggleModal: () => void;
}

const CustomModal: React.ForwardRefRenderFunction<CustomModalRef, CustomModalProps> = (
    { children, modalContent, modalHeight, buttonStyle },
    ref
) => {
    const theme = useTheme();
    const [isVisible, setIsVisible] = useState(false);
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const sharedVal = useSharedValue(0);
    const modalBottomPadding = 20 * theme.dimensions.absoluteHeight;
    const [modalContentHeight] = useState(modalHeight ? modalHeight + modalBottomPadding : 0);

    useImperativeHandle(ref, () => ({
        toggleModal,
    }));

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
                snapPoints={[modalContentHeight + theme.dimensions.tabHeight, theme.dimensions.maxSnapPoint]}
                backdropComponent={() => (
                    <BottomSheetBackdrop
                        animatedIndex={sharedVal}
                        animatedPosition={sharedVal}
                        {...backdropProps}
                    />
                )}
            >
                <BottomSheetView>
                    <View style={{ "minHeight": modalContentHeight }} >
                        {modalContent}
                    </View>
                </BottomSheetView>
            </BottomSheetModal>
        </>
    );
};

export default React.forwardRef(CustomModal);

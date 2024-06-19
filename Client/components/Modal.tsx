import React, { useMemo, useRef, useState } from 'react';
import { View, TouchableOpacity, StyleSheet } from 'react-native';
import { BottomSheetBackdrop, BottomSheetModal, BottomSheetScrollView } from '@gorhom/bottom-sheet';
import { useSharedValue } from 'react-native-reanimated';

type CustomModalProps = {
    children: React.ReactNode;
    modalContent: React.ReactNode;
    _snapPoints?: (string | number)[];
};

const CustomModal: React.FC<CustomModalProps> = ({ children, modalContent }) => {
    const [isVisible, setIsVisible] = useState(false);
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const sharedVal = useSharedValue(0);

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
        <View>
            <TouchableOpacity onPress={toggleModal}>
                {children}
            </TouchableOpacity>
            <BottomSheetModal
                ref={bottomSheetModalRef}
                style={styles.modalContainer}
                enableDynamicSizing
                backdropComponent={() => (
                    <BottomSheetBackdrop
                        animatedIndex={sharedVal}
                        animatedPosition={sharedVal}
                        {...backdropProps}
                    />
                )}
            >
                <BottomSheetScrollView>
                    {modalContent}
                </BottomSheetScrollView>
            </BottomSheetModal>
        </View>
    );
};

const styles = StyleSheet.create({
    overlay: {
        flex: 1,
    },
    background: {
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
    },
    modalContainer: {
        backgroundColor: 'white',
        borderRadius: 40,
        zIndex: 20,
    },
});

export default CustomModal;

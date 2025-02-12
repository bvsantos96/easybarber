import React, { useEffect, useImperativeHandle, useRef, useState } from 'react';
import { View, StyleProp, ViewStyle } from 'react-native';
import { BottomSheetModal, BottomSheetBackdrop, BottomSheetView } from '@gorhom/bottom-sheet/src';
import { useSharedValue } from 'react-native-reanimated';
import { useTheme } from '../styles/ThemeContext';
import Pressable from './Pressable';

type CustomModalProps = {
    children?: React.ReactNode;
    modalContent: React.ReactNode;
    modalHeight: number;
    buttonStyle?: StyleProp<ViewStyle>;
    snapPoints?: number[];
    log?: number;
};

export interface CustomModalRef {
    toggleModal: () => void;
}

const CustomModal: React.ForwardRefRenderFunction<CustomModalRef, CustomModalProps> = (
    { children, modalContent, modalHeight, buttonStyle, snapPoints, log },
    ref
) => {
    const theme = useTheme();
    const [isVisible, setIsVisible] = useState(false);
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const sharedVal = useSharedValue(0);
    const modalBottomPadding = 20 * theme.dimensions.absoluteHeight;
    modalHeight = modalHeight ? modalHeight + modalBottomPadding : 0;
    //const [modalHeight] = useState(modalHeight ? modalHeight + modalBottomPadding : 0);

    const calculateSnapPoints = (): Array<string | number> => {
        return [modalHeight + theme.dimensions.tabHeight, theme.dimensions.maxSnapPoint];
    }

    const [_snapPoints, setSnapPoints] = useState(snapPoints || calculateSnapPoints());

    const prevSnapPointsRef = useRef(snapPoints);

    useEffect(() => {
        if (JSON.stringify(prevSnapPointsRef.current) !== JSON.stringify(snapPoints)) {
            const newSnapPoints = snapPoints || calculateSnapPoints();
            setSnapPoints(newSnapPoints);
            prevSnapPointsRef.current = snapPoints;
            if (isVisible && bottomSheetModalRef.current) {
                requestAnimationFrame(() => {
                    if (bottomSheetModalRef.current) {
                        bottomSheetModalRef.current.snapToIndex(0);
                    }
                });
            }
        }
    }, [snapPoints, isVisible, modalHeight]);

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
            {children && (
                <Pressable style={buttonStyle} onPress={toggleModal}>
                    {children}
                </Pressable>
            )}
            <BottomSheetModal
                ref={bottomSheetModalRef}
                onDismiss={() => setIsVisible(false)}
                snapPoints={_snapPoints}
                backdropComponent={() => (
                    <BottomSheetBackdrop
                        style={{ backgroundColor: theme.colors.modalBackdrop, position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}
                        animatedIndex={sharedVal}
                        animatedPosition={sharedVal}
                        {...backdropProps}
                    />
                )}
            >
                <BottomSheetView>
                    <View style={{ "minHeight": modalHeight }} >
                        {modalContent}
                    </View>
                </BottomSheetView>
            </BottomSheetModal>
        </>
    );
};

export default React.forwardRef(CustomModal);

import React, { forwardRef, useCallback, useImperativeHandle, useMemo, useRef } from 'react';
import { View, Text } from 'react-native';
import { BottomSheetBackdrop, BottomSheetModal, BottomSheetModalProvider } from '@gorhom/bottom-sheet';

import { styles } from '../styles/Filter';

interface FilterProps { }

export interface FilterRef {
    handlePresentModalPress: () => void;
}

const Filter = forwardRef<FilterRef, FilterProps>(({ }, ref) => {
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ["60%"], []);
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);
    const handleSheetChanges = useCallback((index: number) => {
        console.log('handleSheetChanges', index);
    }, []);

    useImperativeHandle(
        ref,
        () => ({
            handlePresentModalPress,
        }),
    );

    const backdropProps = useMemo(() => ({
        closeOnPress: true,
        appearsOnIndex: 0,
        disappearsOnIndex: -1,
        enableTouchThrough: false,
    }), []);

    return (
        <BottomSheetModalProvider>
            <BottomSheetModal
                ref={bottomSheetModalRef}
                style={styles.container}
                index={0}
                snapPoints={snapPoints}
                backdropComponent={() => (
                    <BottomSheetBackdrop
                        animatedIndex={{
                            value: 0
                        }} animatedPosition={{
                            value: 0
                        }} {...backdropProps} />
                )}
                onChange={handleSheetChanges} >
                <View>
                    <Text>Filter</Text>
                </View>
            </BottomSheetModal>
        </BottomSheetModalProvider>
    );
});

export default Filter;

import React, { useCallback, useMemo, useRef } from 'react';
import { View } from 'react-native';
import {
    BottomSheetModal,
    BottomSheetModalProvider
} from '@gorhom/bottom-sheet';
import { styles } from '../styles/Filter';


export default function Filter() {
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ["60%"], []);
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);
    const handleSheetChanges = useCallback((index: number) => {
        console.log('handleSheetChanges', index);
    }, []);

    return (
        <BottomSheetModalProvider>
            <BottomSheetModal
                ref={bottomSheetModalRef}
                index={0}
                snapPoints={snapPoints}
                onChange={handleSheetChanges}>
                <View style={styles.container}>
                    <Text>Filter</Text>
                </View>
            </BottomSheetModal>
        </BottomSheetModalProvider>
    );
}


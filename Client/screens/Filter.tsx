import React, { forwardRef, useCallback, useImperativeHandle, useMemo, useRef } from 'react';
import { View, Text } from 'react-native';
import { BottomSheetBackdrop, BottomSheetModal, BottomSheetModalProvider } from '@gorhom/bottom-sheet';
import Stars from 'react-native-stars';

import { styles } from '../styles/Filter';
import Button from '../components/Button';
import { absoluteWidth } from '../styles/Main';


interface FilterProps { }

export interface FilterRef {
    handlePresentModalPress: () => void;
}

const Filter = forwardRef<FilterRef, FilterProps>(({ }, ref) => {
    const [starsSelected, setStarsSelected] = React.useState(5);
    const texts = require("@lang/en.json");
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ["70%"], []);
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);
    const handleSheetChanges = useCallback((index: number) => {
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
                <View style={styles.topBarContainer}>
                    <Text style={styles.title}>{texts.findBarber}</Text>
                    <Text style={styles.clear}>{texts.clear}</Text>
                </View>
                <View style={styles.input} />
                <View style={styles.ratingTitleContainer}>
                    <Text style={styles.ratingTitle}>{texts.rating}</Text>
                    <Text style={styles.ratingStars}>{starsSelected} {texts.stars}</Text>
                </View>
                <View style={styles.starsContainer}>
                    <View style={{ alignItems: 'center' }}>
                        <Stars
                            half={false}
                            default={starsSelected}
                            update={setStarsSelected}
                            spacing={10 * absoluteWidth}
                            starSize={50 * absoluteWidth}
                            count={5}
                            fullStar={require('@assets/icons/star.png')}
                            emplyStar={require('@assets/icons/starEmpty.png')}
                        />
                    </View>
                </View>
                <Text style={styles.availableTimeTitle}>{texts.availableTime}</Text>
                <View style={styles.timeSelectionContainer} />
                <View style={styles.applyContainer}>
                    <Button title={texts.apply} />
                </View>
            </BottomSheetModal>
        </BottomSheetModalProvider>
    );
});

export default Filter;

import React, { forwardRef, useCallback, useEffect, useImperativeHandle, useMemo, useRef, useState } from 'react';
import { View, Text } from 'react-native';
import { BottomSheetBackdrop, BottomSheetModal, BottomSheetModalProvider } from '@gorhom/bottom-sheet';
import Stars from 'react-native-stars';

import { styles } from '../styles/Filter';
import Button from '../components/Button';
import Picker, { PickerItem } from '../components/Picker';
import { getCategories, getTimes } from '../utils/ApiRequest';
import { useTheme } from '../styles/ThemeContext';


interface FilterProps { }

export interface FilterRef {
    handlePresentModalPress: () => void;
}

const Filter = forwardRef<FilterRef, FilterProps>(({ }, ref) => {
    const theme = useTheme();
    const [categories, setCategories] = useState<PickerItem[]>([]);
    const [fromTimes, setFromTimes] = useState<PickerItem[]>([]);
    const [toTimes, setToTimes] = useState<PickerItem[]>([]);
    const [from, setFrom] = useState('');
    const [to, setTo] = useState('');
    const [category, setCategory] = useState('');
    const [starsSelected, setStarsSelected] = useState(4);
    const texts = require("@lang/en.json");
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ["70%"], []);
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
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

    const fromChange = async (value: string) => {
        setFrom(value);
        setToTimes(await getTimes({ from: value }));
    }

    const toChange = async (value: string) => {
        setTo(value);
        setFromTimes(await getTimes({ to: value }));
    }

    useEffect(() => {
        const fetchComboBoxes = async () => {
            const cats: PickerItem[] = await getCategories();
            setCategories(cats);
            const fromTime: PickerItem[] = await getTimes({});
            setFromTimes(fromTime);
            const toTime: PickerItem[] = await getTimes({ from: fromTime[0].value });
            setToTimes(toTime);
        }

        fetchComboBoxes();
    }, []);

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
                onChange={() => { }} >
                <View style={styles.topBarContainer}>
                    <Text style={styles.title}>{texts.findBarber}</Text>
                    <Text style={styles.clear}>{texts.clear}</Text>
                </View>
                <View style={styles.input}>
                    <Picker
                        placeholder={texts.category}
                        style={{ viewContainer: styles.picker }}
                        selectedValue={category}
                        onValueChange={setCategory}
                        items={categories} />
                </View>
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
                            spacing={10 * theme.dimensions.absoluteWidth}
                            starSize={50 * theme.dimensions.absoluteWidth}
                            count={5}
                            fullStar={require('@assets/icons/star.png')}
                            emplyStar={require('@assets/icons/starEmpty.png')}
                        />
                    </View>
                </View>
                <Text style={styles.availableTimeTitle}>{texts.availableTime}</Text>
                <View style={styles.timeSelectionContainer}>
                    <View style={styles.from}>
                        <Picker
                            placeholder={texts.from}
                            style={{ viewContainer: styles.picker }}
                            selectedValue={from}
                            onValueChange={fromChange}
                            items={fromTimes} />
                    </View>
                    <View style={styles.to}>
                        <Picker
                            placeholder={texts.to}
                            style={{ viewContainer: styles.picker }}
                            selectedValue={to}
                            onValueChange={toChange}
                            items={toTimes} />
                    </View>
                </View>
                <View style={styles.applyContainer}>
                    <Button title={texts.apply} />
                </View>
            </BottomSheetModal>
        </BottomSheetModalProvider>
    );
});

export default Filter;

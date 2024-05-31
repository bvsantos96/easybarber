import React, { forwardRef, useCallback, useEffect, useImperativeHandle, useMemo, useRef, useState } from 'react';
import { View, Text } from 'react-native';
import { BottomSheetBackdrop, BottomSheetModal, BottomSheetModalProvider } from '@gorhom/bottom-sheet';
import Stars from 'react-native-stars';

import { getStyles } from '../styles/Filter';
import Button from '../components/Button';
import Picker, { PickerItem } from '../components/Picker';
import { getTimes } from '../utils/ApiRequest';
import { useTheme } from '../styles/ThemeContext';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import { ICategory, IFilterRequest } from '../declarations';
import Pressable from '../components/Pressable';


interface FilterProps {
    filter?: IFilterRequest;
    setFilter: (filter: IFilterRequest) => void;
}

export interface FilterRef {
    hide: () => void;
    handlePresentModalPress: () => void;
    contructNewFilter: (filter?: IFilterRequest) => void;
}

const Filter = forwardRef<FilterRef, FilterProps>(({ filter, setFilter }, ref) => {
    const styles = getStyles();
    const theme = useTheme();
    const [categories, setCategories] = useState<PickerItem[]>([]);
    const [from, setFrom] = useState<string>(filter?.from || '');
    const [to, setTo] = useState<string>(filter?.to || '');
    const [category, setCategory] = useState<string>(filter?.serviceType || '');
    const [rating, setRating] = useState<number>(filter?.rating || 0);
    const [fromTimes, setFromTimes] = useState<PickerItem[]>([]);
    const [toTimes, setToTimes] = useState<PickerItem[]>([]);
    const texts = require("@lang/en.json");
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ["70%"], []);
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);

    const setStarsSelected = (value: number) => {
        if (value === rating && value === 1) {
            setRating(0);
            return;
        }
        setRating(value);
    }

    useImperativeHandle(
        ref,
        () => ({
            hide: () => bottomSheetModalRef.current?.dismiss(),
            handlePresentModalPress,
            contructNewFilter: constructNewFilter,
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
            const cats: ICategory[] = await retrieveCategories();
            setCategories(cats.map(cat => ({ label: cat.name, value: cat.id.toString() })));
            const fromTime: PickerItem[] = await getTimes({});
            setFromTimes(fromTime);
            const toTime: PickerItem[] = await getTimes({ from: fromTime[0].value });
            setToTimes(toTime);
        }

        fetchComboBoxes();
    }, []);

    const constructNewFilter = (filter?: IFilterRequest) => {
        setCategory(filter?.serviceType || '');
        setRating(filter?.rating || 0);
        setFrom(filter?.from || '');
        setTo(filter?.to || '');
    }

    const resetFilter = () => {
        setCategory('');
        setRating(0);
        setFrom('');
        setTo('');
    }

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
                    <Pressable onPress={resetFilter}>
                        <Text style={styles.clear}>{texts.clear}</Text>
                    </Pressable>
                </View>
                <View style={styles.input}>
                    <Picker
                        placeholder={texts.category}
                        style={{ viewContainer: styles.picker }}
                        selectedValue={`${category}`}
                        onValueChange={(value) => setCategory(value)}
                        items={categories} />
                </View>
                <View style={styles.ratingTitleContainer}>
                    <Text style={styles.ratingTitle}>{texts.rating}</Text>
                    <Text style={styles.ratingStars}>{rating} {texts.stars}</Text>
                </View>
                <View style={styles.starsContainer}>
                    <View style={{ alignItems: 'center' }}>
                        <Stars
                            half={false}
                            default={rating}
                            update={setStarsSelected}
                            spacing={10 * theme.dimensions.absoluteWidth}
                            starSize={50 * theme.dimensions.absoluteWidth}
                            count={5}
                            zero={true}
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
                            selectedValue={from || ""}
                            onValueChange={fromChange}
                            items={fromTimes} />
                    </View>
                    <View style={styles.to}>
                        <Picker
                            placeholder={texts.to}
                            style={{ viewContainer: styles.picker }}
                            selectedValue={to || ''}
                            onValueChange={toChange}
                            items={toTimes} />
                    </View>
                </View>
                <View style={styles.applyContainer}>
                    <Button onPress={
                        () => {
                            let filter: IFilterRequest = {};

                            if (category) {
                                filter.serviceType = category;
                            }

                            if (rating) {
                                filter.rating = rating;
                            }

                            if (from) {
                                filter.from = from;
                            }

                            if (to) {
                                filter.to = to;
                            }
                            setFilter(filter);
                        }
                    } title={texts.applyFilter} />
                </View>
            </BottomSheetModal>
        </BottomSheetModalProvider>
    );
});

export default Filter;

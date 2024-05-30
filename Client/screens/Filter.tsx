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
    resetFilter: () => void;
    filter?: IFilterRequest;
    setFilter: (filter: IFilterRequest) => void;
}

export interface FilterRef {
    handlePresentModalPress: () => void;
}

const Filter = forwardRef<FilterRef, FilterProps>(({ resetFilter, filter, setFilter }, ref) => {
    const styles = getStyles();
    const theme = useTheme();
    const [categories, setCategories] = useState<PickerItem[]>([]);
    const [fromTimes, setFromTimes] = useState<PickerItem[]>([]);
    const [toTimes, setToTimes] = useState<PickerItem[]>([]);
    const texts = require("@lang/en.json");
    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ["70%"], []);
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);

    const setStarsSelected = (value: number) => {
        if (value === filter?.rating && value === 1) {
            setFilter({ ...filter, rating: 0 });
            return;
        }
        setFilter({ ...filter, rating: value });
    }

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
        setFilter({ ...filter, from: value });
        setToTimes(await getTimes({ from: value }));
    }

    const toChange = async (value: string) => {
        setFilter({ ...filter, to: value });
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
                        selectedValue={`${filter?.serviceType || ""}`}
                        onValueChange={(value) => setFilter({ ...filter, serviceType: value })}
                        items={categories} />
                </View>
                <View style={styles.ratingTitleContainer}>
                    <Text style={styles.ratingTitle}>{texts.rating}</Text>
                    <Text style={styles.ratingStars}>{filter?.rating} {texts.stars}</Text>
                </View>
                <View style={styles.starsContainer}>
                    <View style={{ alignItems: 'center' }}>
                        <Stars
                            half={false}
                            default={filter?.rating}
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
                            selectedValue={filter?.from || ""}
                            onValueChange={fromChange}
                            items={fromTimes} />
                    </View>
                    <View style={styles.to}>
                        <Picker
                            placeholder={texts.to}
                            style={{ viewContainer: styles.picker }}
                            selectedValue={filter?.to || ''}
                            onValueChange={toChange}
                            items={toTimes} />
                    </View>
                </View>
                <View style={styles.applyContainer}>
                    <Button onPress={
                        () => {
                            setFilter(filter);
                        }
                    } title={texts.applyFilter} />
                </View>
            </BottomSheetModal>
        </BottomSheetModalProvider>
    );
});

export default Filter;

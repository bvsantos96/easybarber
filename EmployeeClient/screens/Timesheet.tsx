import { View, Text } from 'react-native';
import { getStyles } from '@styles/TimeSheet';
import texts from '@lang/en.json';
import { useEffect, useRef, useState } from 'react';
import Pressable from '@components/Pressable';
import PageList from '@components/PageList';
import { PageListType } from 'enums';
import Feather from '@expo/vector-icons/Feather';
import { useTheme } from '@styles/ThemeContext';
import Divider from '@components/Divider';
import CustomModal, { CustomModalRef } from '@components/CustomModal';
import DatePicker from 'react-native-date-picker';
import { getTimeAsString } from 'utils/Utils';

const SelectTimeShett = ({ day, save }: { day: number, save: (day: number, from: Date, to: Date) => void }) => {
    const styles = getStyles();
    const [openFrom, setOpenFrom] = useState(false);
    const [openTo, setOpenTo] = useState(false);
    const defaultStart = new Date();
    defaultStart.setHours(9);
    defaultStart.setMinutes(0);
    const defaultEnd = new Date();
    defaultEnd.setHours(18);
    defaultEnd.setMinutes(0);
    const [fromTime, setFromTime] = useState(defaultStart);
    const [toTime, setToTime] = useState(defaultEnd);
    const theme = useTheme();
    return (
        <View style={styles.timeSelectContainer}>
            <Text style={styles.timeSelectText}>{texts.timeSelect.title}</Text>
            <View style={styles.timeSelectItemContainer}>
                <DatePicker
                    modal
                    mode="time"
                    open={openFrom}
                    date={fromTime}
                    onConfirm={(date) => {
                        setOpenFrom(false)
                        setFromTime(date)
                    }}
                    onCancel={() => {
                        setOpenFrom(false)
                    }}
                />
                <DatePicker
                    modal
                    mode="time"
                    open={openTo}
                    date={toTime}
                    onConfirm={(date) => {
                        setOpenTo(false)
                        setToTime(date)
                    }}
                    onCancel={() => {
                        setOpenTo(false)
                    }}
                />
                <Pressable
                    onPress={() => setOpenFrom(true)}
                    style={styles.timeSelectItem}>
                    <Text style={styles.timeSelectItemTextTop}>{texts.from}</Text>
                    <Text style={styles.timeSelectItemTextBottom}>{getTimeAsString(fromTime)}</Text>
                </Pressable>
                <Feather name="arrow-right" size={styles.timeSheetItemIcon.width} color={theme.colors.mainColor} />
                <Pressable
                    onPress={() => setOpenTo(true)}
                    style={styles.timeSelectItem}>
                    <Text style={[styles.timeSelectItemTextTop, { alignSelf: "flex-end" }]}>{texts.to}</Text>
                    <Text style={[styles.timeSelectItemTextBottom, { alignSelf: "flex-end" }]}>{getTimeAsString(toTime)}</Text>
                </Pressable>
            </View>
            <Pressable useGradient={false} style={styles.timeSelectButton} onPress={() => save(day, fromTime, toTime)}>
                <Text style={styles.timeSelectButtonText}>{texts.timeSelect.button}</Text>
            </Pressable>
        </View >
    );
}

const TimeSheetDay = ({ text, selected = false, select }: { text: string, selected?: boolean, select: () => void }) => {
    const styles = getStyles();
    return (
        <Pressable useGradient={false} onPress={select} style={[styles.weekdayContainer, selected && styles.weekdaySelected]}>
            <Text style={[styles.weekdayText, selected && styles.weekdayTextSelected]}>{text}</Text>
        </Pressable>
    );
}

const TimeSheetComponent = ({ item, maxWidth }: { item: TimeSheetItem, maxWidth: number }) => {
    const styles = getStyles();
    const theme = useTheme();
    return (
        <View style={[styles.timeSheetItemContainer, { width: maxWidth }]} >
            <View style={styles.timeSheetItemInnerContainer}>
                <Feather name="calendar" size={styles.timeSheetItemIcon.width} color={theme.colors.backgroundColor} />
                <Divider horizontal size={maxWidth === styles.timeSheetItemContainer.width ? 25 : 10} />
                <View style={styles.timeSheetItemTextContainer}>
                    <Feather name="clock" size={styles.timeSheetItemSmallIcon.width} color={theme.colors.backgroundColor} />
                    <Divider horizontal size={5} />
                    <Text style={styles.timeSheetItemText}>{`${getTimeAsString(item.time.startTime)} - ${getTimeAsString(item.time.endTime)}`}</Text>
                </View>
            </View>
        </View>
    );
}

const TimeSheet = () => {
    const styles = getStyles();
    const theme = useTheme();
    const days = useRef([...Array(7).keys()]);
    const [selectedDay, setSelectedDay] = useState(0);
    const [refresh, setRefresh] = useState(false);
    const [timeSheets, setTimeSheets] = useState<{ [key: number]: TimeSheetItem[] }>({});
    const [maxWidth, setMaxWidth] = useState<number>(styles.timeSheetItemContainer.width);
    const filterModalRef = useRef<CustomModalRef>(null);

    const toggleRefresh = (day: number) => {
        setMaxWidth(timeSheets[day]?.length > 5 ? (styles.timeSheetItemContainer.width / 2 - 2 * theme.dimensions.absoluteWidth) : styles.timeSheetItemContainer.width);
        setRefresh(!refresh);
    }

    const updateSelectedDay = (newDay: number) => {
        if (newDay >= 0 && newDay <= days.current.length) {
            toggleRefresh(newDay);
            setSelectedDay(newDay);
        }
    }

    const load = async (_page?: IPage<TimeSheetItem>, _params?: Record<string, string | number | boolean>) => {
        return Promise.resolve(
            {
                content: timeSheets[selectedDay] || [],
                totalPages: 1,
                totalElements: timeSheets[selectedDay]?.length || 0,
                currentPage: 1,
                pageSize: timeSheets[selectedDay]?.length || 0,
                hasNextPage: false,
                hasPreviousPage: false,
            }
        );
    }

    const addNewTimesheet = (day: number, from: Date, to: Date) => {
        const newTimeSheet = timeSheets;
        if (!newTimeSheet[day]) {
            newTimeSheet[day] = [];
        }
        newTimeSheet[day].push({
            id: newTimeSheet[day].length,
            time: {
                startTime: from,
                endTime: to
            }
        });
        setTimeSheets(newTimeSheet);
        filterModalRef.current?.toggleModal();
        toggleRefresh(day);
    }

    return (
        <>
            <View style={styles.weekdaysContainer}>
                {days.current?.map((day, index) => (
                    <TimeSheetDay key={index} text={texts.weekday.short[day]} selected={selectedDay === day} select={() => { updateSelectedDay(index) }} />
                ))}
            </View>
            <Divider size={40} />
            <View style={styles.timeSheetListContainer}>
                <PageList<TimeSheetItem>
                    gap={5}
                    type={PageListType.MULTI_COL_LIST}
                    renderItem={({ item, index }: { item: TimeSheetItem, index: number }) =>
                        <View key={index}>
                            <TimeSheetComponent item={item} maxWidth={maxWidth} />
                        </View>
                    }
                    itemMaxWidth={maxWidth}
                    preload={false}
                    initialItems={timeSheets[selectedDay] || []}
                    requestFunction={load}
                    dontDisplayLoadMore
                />
            </View>
            <CustomModal
                ref={filterModalRef}
                buttonStyle={styles.addButton}
                modalContent={
                    <SelectTimeShett day={selectedDay} save={addNewTimesheet} />
                }
                snapPoints={[styles.timeSelectContainer.maxHeight]}
                modalHeight={styles.timeSelectContainer.maxHeight}
            />
            <Pressable
                useGradient={true}
                style={styles.addButton}
                shadow={true}
                onPress={() => filterModalRef.current?.toggleModal()}>
                <Feather name="plus" size={styles.timeSheetItemIcon.width} color={theme.colors.backgroundColor} />
            </Pressable>
        </>
    );
}

export default TimeSheet;

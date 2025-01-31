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
import { getClientDayOfWeekFromString, getServerDayOfWeek, getTimeAsString, parseServerTime } from 'utils/Utils';
import SlidingItem from '@components/SlidingItem';
import Fontisto from '@expo/vector-icons/Fontisto';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { Params } from '@navigation/Router';
import { deleteSchedule, getTimesheets, setTimesheet } from 'utils/ApiRequest';
import { createEmptyPage } from 'utils/PageHandling';

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

const TimeSheetComponent = ({ item, maxWidth, deleteItem }: { item: TimeSheetItem, maxWidth: number, deleteItem: () => void }) => {
    const styles = getStyles();
    const theme = useTheme();
    if (item.day) {
        item.days = [getClientDayOfWeekFromString(item.day)];
    }
    return (
        <SlidingItem
            items={
                <Pressable onPress={async () => { deleteItem(); }} style={[styles.icon, styles.redIcon]}>
                    <Fontisto name="trash" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                </Pressable>
            }
        >
            <View style={[styles.timeSheetItemContainer, { width: maxWidth }]} >
                <View style={styles.timeSheetItemInnerContainer}>
                    <Feather name="calendar" size={styles.timeSheetItemIcon.width} color={theme.colors.backgroundColor} />
                    <Divider horizontal size={maxWidth === styles.timeSheetItemContainer.width ? 25 : 10} />
                    <View style={styles.timeSheetItemTextContainer}>
                        <Feather name="clock" size={styles.timeSheetItemSmallIcon.width} color={theme.colors.backgroundColor} />
                        <Divider horizontal size={5} />
                        <Text style={styles.timeSheetItemText}>{`${parseServerTime(item.startHour)} - ${parseServerTime(item.endHour)}`}</Text>
                    </View>
                </View>
            </View>
        </SlidingItem>
    );
}


export type Route = {
    establishmentId?: number;
};

type Props = NativeStackScreenProps<typeof Params, 'TimeSheet'>;

export default function TimeSheet({ route, navigation }: Props) {
    let establishmentId: number | undefined = undefined;
    if (route.params) {
        const { establishmentId: _establishmentId } = route.params;
        establishmentId = _establishmentId;
    }
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

    const load = async (_page?: IPage<TimeSheetItem>, _params?: Record<string, string | number | boolean>): Promise<IPage<TimeSheetItem>> => {
        let items = await getTimesheets(undefined, undefined, establishmentId);
        if (!items) return Promise.resolve(createEmptyPage());
        return Promise.resolve(items);
    }

    const addNewTimesheet = async (day: number, from: Date, to: Date) => {
        let timeSheet: TimeSheetItem = {
            id: 0,
            establishmentId: establishmentId,
            days: [getServerDayOfWeek(day)],
            endHour: getTimeAsString(to),
            startHour: getTimeAsString(from)
        };
        const newTimesheet = await setTimesheet(timeSheet);
        if (!newTimesheet || !newTimesheet.id) return;
        timeSheet.id = +newTimesheet.id;
        const newTimeSheet = timeSheets;
        if (!newTimeSheet[day]) {
            newTimeSheet[day] = [];
        }
        newTimeSheet[day].push(timeSheet);
        setTimeSheets(newTimeSheet);
        filterModalRef.current?.toggleModal();
        toggleRefresh(day);
    }

    const deleteItem = async (day: number, id: number) => {
        if (!(await deleteSchedule(id))) return;
        const newTimeSheet = timeSheets;
        if (newTimeSheet[day]) {
            newTimeSheet[day] = newTimeSheet[day].filter((item) => item.id !== id);
            setTimeSheets(newTimeSheet);
        }
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
                        <TimeSheetComponent key={index} item={item} maxWidth={maxWidth} deleteItem={() => { deleteItem(selectedDay, +item.id) }} />
                    }
                    itemMaxWidth={maxWidth}
                    reset={refresh}
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

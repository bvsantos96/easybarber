import React, { useEffect, useState } from "react";
import { getStyles } from "../styles/Availability";
import Selection from "./Selection";
import { Calendar } from "react-native-calendars";
import { View, Text } from "react-native";
import { useTheme } from "@styles/ThemeContext";
import { MarkedDates } from "react-native-calendars/src/types";
import { Underline } from "@components/Underline";
import TimeSlotView from "@components/TimeSlotView";
import PagerView from "react-native-pager-view";
import { RouteProp, useRoute } from "@react-navigation/native";
import { getAvailability, getUnavailableDates, setAppointment } from "utils/ApiRequest";
import { twoDigits } from "utils/Utils";
import { Props } from "./Home";
import useAlertStore from "storage/stores/AlertStore";
import { AlertType } from "@components/Alert";
import texts from "@lang/en.json";

type RouteParams = {
    appointment: {
        establishmentId: number;
        serviceId: number;
        employeeId: number;
    };
};

export default function Availability({ navigation }: Props) {
    const pagerRef = React.useRef<PagerView>(null);
    const theme = useTheme();
    const styles = getStyles();
    const route = useRoute<RouteProp<RouteParams, 'appointment'>>();
    const { establishmentId, serviceId, employeeId } = route.params;
    const [date, setDate] = useState<string>("");
    const [time, setTime] = useState<TimeSlot>();
    const { alert } = useAlertStore();
    const disableDates = (dates: string[]): MarkedDates => {
        let disabled: MarkedDates = {};
        dates.forEach(d => {
            disabled[d] = { selected: false, disabled: true, disableTouchEvent: true };
        });
        return disabled;
    }

    const disableUntilToday = (): MarkedDates => {
        let disabled: MarkedDates = {};
        const today = new Date();
        let _month = month;
        let _year = year;
        if (month === undefined || month === 0 || year === undefined || year === 0) {
            _month = today.getMonth() + 1;
            _year = today.getFullYear();
        }

        for (let _date = new Date(_year, _month - 1, 1, 23, 59); _date.getMonth() === _month - 1 && _date < today; _date.setDate(_date.getDate() + 1)) {
            const dateString = _date.toISOString().split('T')[0];
            disabled[dateString] = { selected: false, disabled: true, disableTouchEvent: true };
        }
        return disabled;
    }

    const [unSelectable, setUnSelectable] = useState<MarkedDates>(disableUntilToday());
    const [calculatedMonth, setCalculatedMonth] = useState<Set<string>>(new Set());
    const [month, setMonth] = useState<number>(0);
    const [year, setYear] = useState<number>(0);

    const nLines = Math.floor(styles.calendar.width / (styles.slotContainer.width + 10));
    const nCols = Math.floor(styles.timeSlotsContainer.height / (styles.slotContainer.height + 10));
    const elemsPerPage: number = nLines * nCols;

    const buildTimeSlotViews = (slots: TimeSlot[]): TimeSlot[][][] => {
        let viewSlots: TimeSlot[][][] = [];
        for (let i = 0; i < slots.length; i += elemsPerPage) {
            let _vsl: TimeSlot[][] = [];
            for (let j = i; j < slots.length && j < i + elemsPerPage; j += nCols) {
                let _vsc: TimeSlot[] = [];
                for (let k = j; k < slots.length && k < j + nCols; k++) {
                    _vsc.push(slots[k]);
                }
                _vsl.push(_vsc);
            }
            viewSlots.push(_vsl);
        }
        return viewSlots;
    }
    const [timeSlots, setTimeSlots] = useState<TimeSlot[][][]>([]);

    const getStartingHour = (today: Date): string => {
        const todayHourAndMinute = twoDigits(today.getHours()) + ":" + twoDigits(today.getMinutes());
        return date == today.toISOString().split('T')[0] ? todayHourAndMinute : "00:01";
    }

    useEffect(() => {
        const today = new Date();
        const fetchAvailability = async () => {
            let availability: TimeSlots = await getAvailability(establishmentId, serviceId, employeeId, date, getStartingHour(today));
            setTimeSlots(buildTimeSlotViews(availability.slots));
        }

        if (date.length > 0) {
            fetchAvailability();
        }
    }, [date]);

    useEffect(() => {
        pagerRef?.current?.forceUpdate();
    }, [timeSlots]);

    useEffect(() => {
        const today = new Date();
        if (month === 0 || year === 0) {
            setMonth(today.getMonth() + 1);
            setYear(today.getFullYear());
            return;
        }
        const monthYearStr = `${month}-${year}`;
        if (month > 0 && year > 0 && !calculatedMonth.has(monthYearStr)) {
            setCalculatedMonth(new Set([...calculatedMonth, monthYearStr]));
            getUnavailableDates(establishmentId, serviceId, employeeId, year, month, getStartingHour(new Date())).then(dates => {
                const _disabledDates = { ...unSelectable, ...disableDates(dates) };
                setUnSelectable(_disabledDates);
            });
        }
    }, [month, year]);

    return (
        <Selection
            buttonText={texts.appointments.book}
            selectionText={texts.appointments.scheduleSelection}
            onButtonPress={
                async () => {
                    let _employeeId = (employeeId !== undefined && employeeId !== 0) ? employeeId : time?.employeeIds.length === 1 ? time?.employeeIds[0] : undefined;
                    if (_employeeId !== undefined) {
                        if (await setAppointment({
                            id: 0,
                            establishmentId: establishmentId,
                            serviceId: serviceId,
                            employeeId: _employeeId,
                            date: date,
                            time: time?.start || ""
                        })) {
                            alert({
                                type: AlertType.Success, message: texts.appointments.success, onPress: () => {
                                    navigation.reset({
                                        index: 0,
                                        routes: [{ name: 'Appointments' }],
                                    });
                                }
                            });
                            return;
                        } else {
                            alert({ type: AlertType.Error, message: texts.appointments.failed });
                        }
                        return;
                    }
                    navigation.navigate(texts.employees.title, { establishmentId, serviceId, date, startHour: time?.start, availableEmployees: time?.employeeIds });
                    return;
                }
            }
            selected={date.length > 0 && time !== undefined}
        >
            <View style={styles.calendar}>
                <Calendar
                    onMonthChange={date => { setYear(date.year); setMonth(date.month); }}
                    onDayPress={day => {
                        if (day.dateString !== date) {
                            setDate(day.dateString);
                            setTime(undefined);
                        }
                    }}
                    markedDates={{
                        [date]: { selected: true, disableTouchEvent: true },
                        ...unSelectable
                    }}
                    theme={{
                        arrowColor: theme.colors.text.lightBlack,
                        backgroundColor: theme.colors.backgroundColor,
                        calendarBackground: theme.colors.backgroundColor,
                        textSectionTitleColor: theme.colors.text.lightBlack,
                        selectedDayBackgroundColor: theme.colors.mainColor,
                        selectedDayTextColor: theme.colors.backgroundColor,
                        todayTextColor: theme.colors.mainColor,
                        dayTextColor: theme.colors.text.black,
                        textDisabledColor: theme.colors.text.lightGray,
                    }}
                />
            </View>
            <View style={styles.slotsContainer}>
                <View style={{ alignSelf: 'flex-start' }}>
                    <Text style={styles.slotsTitle}>{texts.appointments.slotsAvailable}</Text>
                    <Underline />
                </View>
                {timeSlots.length > 0 && (
                    <PagerView
                        ref={pagerRef}
                        style={styles.timeSlotsContainer}>
                        {timeSlots && timeSlots.map((item, index) => (
                            <TimeSlotView
                                key={index}
                                offset={index * elemsPerPage}
                                select={setTime}
                                selected={time}
                                slots={item}
                            />
                        ))}
                    </PagerView>
                )}
                {timeSlots.length === 0 && (
                    <View style={styles.noSlotsContainer}>
                        <Text style={styles.noSlots}>{texts.appointments.noSlots}</Text>
                    </View>
                )}
            </View>
        </Selection >
    );
}

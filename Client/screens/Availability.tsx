import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Calendar } from "react-native-calendars";
import { View, Text, ActivityIndicator } from "react-native";
import { useQuery } from "@tanstack/react-query";
import PagerView from "react-native-pager-view";

import { getStyles } from "../styles/Availability";
import Selection from "./Selection";
import { useTheme } from "@styles/ThemeContext";
import { MarkedDates } from "react-native-calendars/src/types";
import { Underline } from "@components/Underline";
import TimeSlotView from "@components/TimeSlotView";
import { getAvailability, getDynamicSlots, getStartingHour, getToken, getUnavailableDates, hasDynamicPrice, setAppointment } from "utils/ApiRequest";
import useAlertStore from "storage/stores/AlertStore";
import { AlertType } from "@components/Alert";
import texts from "@lang/en.json";
import { Params, Routes } from "@navigation/Router";
import { BannerType } from "@components/Banner";
import { buildCurrencyString } from "utils/Utils";

export type Route = {
    establishmentId: number;
    serviceId: number;
    employeeId: number;
};

type Props = NativeStackScreenProps<typeof Params, 'Availability'>;

export default function Availability({ route, navigation }: Props) {
    const pagerRef = React.useRef<PagerView>(null);
    const theme = useTheme();
    const styles = getStyles();
    const { establishmentId, serviceId, employeeId } = route.params;
    const [date, setDate] = useState<string>("");
    const [time, setTime] = useState<TimeSlot>();
    const { alert, setAlertVisible, banner } = useAlertStore();
    const [loading, setLoading] = useState<boolean>(false);
    const disableDates = (dates: string[]): MarkedDates => {
        let disabled: MarkedDates = {};
        dates.forEach(d => {
            disabled[d] = { selected: false, disabled: true, disableTouchEvent: true }
        });
        return disabled;
    }

    const markDates = (dates: string[]): MarkedDates => {
        let marked: MarkedDates = {};
        dates.forEach(d => {
            marked[d] = { startingDay: true, endingDay: true, color: theme.colors.dynamicPrice };
        });
        return marked;
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

    const [dynamicPrices, setDynamicPrices] = useState<MarkedDates>({});
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

    const fetchAvailability = async () => {
        const today = new Date();
        getAvailability(establishmentId, serviceId, employeeId, date, getStartingHour(today, date))
            .then((availability: TimeSlots) => {
                if (availability?.slots === null || availability?.slots === undefined) {
                    availability.slots = [];
                }
                setTimeSlots(buildTimeSlotViews(availability.slots));
                setLoading(false);
            }).catch((error) => {
                console.error(error);
                setTimeSlots(buildTimeSlotViews([]));
                setLoading(false);
            });
    }

    useEffect(() => {
        if (date.length > 0) {
            setLoading(true);
            fetchAvailability();
        }
    }, [date]);

    useEffect(() => {
        pagerRef?.current?.forceUpdate();
    }, [timeSlots]);

    const { data } = useQuery({
        queryKey: [`getUnavailableDates`, establishmentId, serviceId, employeeId, year, month],
        queryFn: async () => await getUnavailableDates(establishmentId, serviceId, employeeId, year, month, getStartingHour(new Date(), new Date().toISOString().split('T')[0])),
        enabled: month > 0 && year > 0 && !calculatedMonth.has(`${month}-${year}`),
        staleTime: 60000
    });

    const { data: dynamicSlots } = useQuery({
        queryKey: [`getDynamicSlots`, establishmentId, serviceId, employeeId, date],
        queryFn: async () => await getDynamicSlots(establishmentId, serviceId, employeeId, year, month),
        enabled: month > 0 && year > 0 && !calculatedMonth.has(`${month}-${year}`),
        staleTime: 60000
    });

    useEffect(() => {
        if (!!dynamicSlots) {
            const _dynamicSlots = { ...dynamicPrices, ...markDates(dynamicSlots) };
            setDynamicPrices(_dynamicSlots);
        }
    }, [dynamicSlots]);

    useEffect(() => {
        if (!!data) {
            const _disabledDates = { ...unSelectable, ...disableDates(data) };
            setUnSelectable(_disabledDates);
        }
    }, [data]);

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
        }
    }, [month, year]);

    const scheduleAppointment = async (_employeeId: number) => {
        alert({ type: AlertType.Loading, message: "" });
        const msg = await setAppointment({
            id: 0,
            establishmentId: establishmentId,
            establishmentServiceId: serviceId,
            establishmentStaffId: _employeeId,
            date: date,
            time: time?.start || ""
        });
        if (msg.length === 0) {
            setAlertVisible(false);
            alert({
                type: AlertType.Success,
                message: texts.appointments.success,
                buttonText: texts.dismiss,
                onPress: () => {
                    navigation.navigate(Routes.Appointments);
                }
            });
        } else {
            setAlertVisible(false);
            alert({
                type: AlertType.Error, message: msg,
                buttonText: texts.dismiss, onPress: () => {
                    fetchAvailability();
                }
            });
            return;
        }
        navigation.navigate(Routes.EmployeeSelection, { establishmentId, serviceId, date, startHour: time?.start, availableEmployees: time?.employeeIds });
        return;
    }

    return (
        <Selection
            buttonText={texts.appointments.book}
            selectionText={texts.appointments.scheduleSelection}
            onButtonPress={
                async () => {
                    let _employeeId = (employeeId !== undefined && employeeId !== 0) ? employeeId : time?.employeeIds.length === 1 ? time?.employeeIds[0] : undefined;
                    if (_employeeId !== undefined) {
                        if (await getToken() === null) {
                            alert({
                                type: AlertType.Error,
                                message: texts.login.required,
                                buttonText: texts.login.signIn,
                                onPress: () => {
                                    navigation.navigate(Routes.Sign);
                                },
                                buttonText2: texts.dismiss,
                            });

                            return;
                        }
                        const dynamicPrice = await hasDynamicPrice(serviceId, _employeeId, date, time?.start || "");
                        if (!!dynamicPrice) {
                            alert({
                                type: AlertType.Info,
                                message: texts.appointments.dynamicPriceConfirmation.replace("{price}", buildCurrencyString(dynamicPrice)),
                                onPress: async () => {
                                    scheduleAppointment(_employeeId || 0);
                                },
                                buttonText: texts.confirm,
                                onPress2: () => { }
                            });
                            return;
                        } else {
                            scheduleAppointment(_employeeId || 0);
                            return;
                        }
                    }
                    navigation.navigate(Routes.EmployeeSelection, { establishmentId, serviceId, date, startHour: time?.start, availableEmployees: time?.employeeIds });
                    return;
                }
            }
            selected={date.length > 0 && time !== undefined}
        >
            <View style={styles.calendar}>
                <Calendar
                    enableSwipeMonths={true}
                    onMonthChange={date => { setYear(date.year); setMonth(date.month); }}
                    onDayPress={day => {
                        if (day.dateString !== date) {
                            if (day.dateString in dynamicPrices) {
                                banner({
                                    message: texts.appointments.dynamicPrice,
                                    type: BannerType.Warning,
                                    showAlertOnPull: true,
                                });
                            }
                            setDate(day.dateString);
                            setTime(undefined);
                        }
                    }}
                    markingType={'period'}
                    markedDates={{
                        ...unSelectable,
                        ...dynamicPrices,
                        [date]: {
                            selected: true,
                            disableTouchEvent: true,
                            color: date in dynamicPrices ? theme.colors.dynamicPriceSelected : theme.colors.mainColor,
                            startingDay: true,
                            endingDay: true
                        },
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
                {loading ? (
                    <View style={[styles.noSlotsContainer, { backgroundColor: theme.colors.backgroundColor }]}>
                        <ActivityIndicator style={styles.noSlotsContainer} size="large" color={theme.colors.text.lightGray} />
                    </View>) : (
                    <>
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
                    </>
                )}
            </View>
        </Selection >
    );
}

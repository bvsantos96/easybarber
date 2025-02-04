import { View, Text, Pressable, TextInput } from 'react-native';
import { Calendar } from "react-native-calendars";
import { useTheme } from '@styles/ThemeContext';
import { useEffect, useRef, useState } from 'react';
import { MarkedDates } from 'react-native-calendars/src/types';
import { getCalendarReadyDate, getTimeAsString, parseServerTime, sumTime } from 'utils/Utils';
import { getStyles } from '@styles/SchedulesStyles';
import PageList from '@components/PageList';
import Entypo from '@expo/vector-icons/Entypo';
import texts from '@lang/en.json';
import Button from '@components/Button';
import CustomModal, { CustomModalRef } from '@components/CustomModal';
import Input from '@components/Input';
import DatePicker from 'react-native-date-picker';
import FontAwesome6 from '@expo/vector-icons/FontAwesome6';
import Divider from '@components/Divider';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { Params } from '@navigation/Router';
import { createException, fetchDayAppointments, fetchMonthAppointments } from 'utils/ApiRequest';
import { AlertType } from '@components/Alert';
import useAlertStore from 'storage/stores/AlertStore';

const AppointmentItem = ({ item }: { item: AppointmentInfo }) => {
    const styles = getStyles();
    console.log(item);
    return (
        <Pressable style={[styles.appointmentController, styles.shadow]}>
            <View style={styles.icon}>
                <Entypo name="dots-three-horizontal" size={styles.icon.width} color={styles.icon.color} />
            </View>
            <View style={styles.appoitmentTextContainer}>
                <Text style={styles.timeText}>{`${parseServerTime(item.time)} - ${sumTime(item.time, item.duration)}`}</Text>
                <Text style={styles.titleText}>{item.entityName}</Text>
                <Text style={styles.subTitleText}>{item.serviceName}</Text>
            </View>
        </Pressable >
    );
}

export type Route = {
    establishmentId?: number;
};

type Props = NativeStackScreenProps<typeof Params, 'Schedules'>;

export default function Schedules({ route, navigation }: Props) {
    const { alert } = useAlertStore();
    let establishmentId: number | undefined = undefined;
    if (route.params) {
        const { establishmentId: _establishmentId } = route.params;
        establishmentId = _establishmentId;
    }
    const theme = useTheme();
    const styles = getStyles();
    const [year, setYear] = useState(new Date().getFullYear());
    const [month, setMonth] = useState(new Date().getMonth() + 1);
    const [date, setDate] = useState<string>(new Date().toISOString().split('T')[0]);
    const [markedDates, setMarkedDates] = useState<MarkedDates>({});
    const [absenceDates, setAbsenceDates] = useState<MarkedDates>({});
    const [title, setTitle] = useState('');
    const [message, setMessage] = useState('');
    const [from, setFrom] = useState<Date | undefined>(undefined);
    const [to, setTo] = useState<Date | undefined>(undefined);
    const fromInputRef = useRef<TextInput>(null);
    const toInputRef = useRef<TextInput>(null);
    const [openFrom, setOpenFrom] = useState(false);
    const [openTo, setOpenTo] = useState(false);
    const modalRef = useRef<CustomModalRef>(null);

    const markDates = (days: MonthCalendar | undefined): void => {
        if (!days) {
            setAbsenceDates({});
            setMarkedDates({});
            return;
        }
        let marked: MarkedDates = {};
        let absences: MarkedDates = {};
        for (const dateStr in days) {
            if (Object.prototype.hasOwnProperty.call(days, dateStr)) {
                const _date = new Date(dateStr);
                const d: CalendarDay = days[dateStr];
                if (d.disabled || !d.hasSchedules) {
                    absences[getCalendarReadyDate(_date)] = {
                        startingDay: true,
                        endingDay: true,
                        textColor: theme.colors.text.black,
                        selectedColor: theme.colors.text.lightGray,
                        selected: true
                    };
                }
                marked[getCalendarReadyDate(_date)] = {
                    dots: Array(Math.min(d.availability, 3)).fill({ color: d.availability >= 3 ? theme.colors.errorColor : d.availability === 2 ? theme.colors.warningColor : theme.colors.mainColor }),
                };
            }
        }
        setAbsenceDates(absences);
        setMarkedDates(marked);
    }

    const loadAppointmentsByDay = async (_page?: IPage<AppointmentInfo>, _params?: AppointmentFilter): Promise<IPage<AppointmentInfo> | undefined> => {
        return await fetchDayAppointments(_page, _params, date, establishmentId);
    }

    const load = async () => {
        markDates(await fetchMonthAppointments(month, year, establishmentId));
    }

    useEffect(() => {
        load();
    }, [year, month]);

    useEffect(() => {
        setYear(parseInt(date.split('-')[0]));
        setMonth(parseInt(date.split('-')[1]));
    }, [date]);

    const resetAbsence = () => {
        setTitle('');
        setMessage('');
        const _from = new Date();
        _from.setHours(0);
        _from.setMinutes(0);
        setFrom(_from);
        const _to = new Date();
        _to.setHours(23);
        _to.setMinutes(59);
        setTo(_to);
    }

    const setAbsence = async () => {
        if (!title) {
            alert({
                type: AlertType.Error,
                message: `${texts.requiredField} ${texts.title}`,
                buttonText: texts.dismiss,
                onPress: () => { },
            });
            return;
        }
        if (!from) {
            alert({
                type: AlertType.Error,
                message: `${texts.requiredField} ${texts.from}`,
                buttonText: texts.dismiss,
                onPress: () => { },
            });
            return;
        }
        if (!to) {
            alert({
                type: AlertType.Error,
                message: `${texts.requiredField} ${texts.to}`,
                buttonText: texts.dismiss,
                onPress: () => { },
            });
            return;
        }
        const absence: Absence = {
            dateFrom: getCalendarReadyDate(new Date(date)),
            dateTo: getCalendarReadyDate(new Date(date)),
            startHour: from ? from.toISOString().split('T')[1].split('.')[0] : '',
            endHour: to ? to.toISOString().split('T')[1].split('.')[0] : '',
            establishmentId: establishmentId,
            title: title,
            message: message,
        }
        if (await createException(absence)) {
            load();
            resetAbsence();
            modalRef.current?.toggleModal();
        }
    }

    const openModal = () => {
        const _from = new Date(date);
        const today = new Date();
        if (_from.getFullYear() === today.getFullYear() && _from.getMonth() === today.getMonth() && _from.getDate() === today.getDate()) {
            setFrom(today);
        } else {
            _from.setHours(0);
            _from.setMinutes(0);
            setFrom(_from);
        }
        const _to = new Date(date);
        _to.setHours(23);
        _to.setMinutes(59);
        setTo(_to);
        modalRef.current?.toggleModal();
    }

    return (
        <View style={styles.container}>
            <Calendar
                style={styles.calendar}
                enableSwipeMonths={true}
                onMonthChange={date => { setYear(date.year); setMonth(date.month); }}
                onDayPress={day => {
                    if (day.dateString !== date) {
                        setDate(day.dateString);
                    }
                }}
                markingType={'multi-dot'}
                markedDates={{
                    ...markedDates,
                    ...absenceDates,
                    [date]: {
                        selected: true,
                        disableTouchEvent: true,
                        color: theme.colors.mainColor,
                        startingDay: true,
                        endingDay: true
                    },
                }}
                theme={{
                    arrowColor: theme.colors.text.lightBlack,
                    backgroundColor: theme.colors.backgroundColor,
                    textSectionTitleColor: theme.colors.text.lightBlack,
                    calendarBackground: theme.colors.backgroundColor,
                    selectedDayBackgroundColor: theme.colors.mainColor,
                    selectedDayTextColor: theme.colors.backgroundColor,
                    todayTextColor: theme.colors.mainColor,
                    dayTextColor: theme.colors.text.black,
                    textDisabledColor: theme.colors.text.lightGray,
                }}
            />
            <View style={styles.listContainer}>
                <PageList<AppointmentInfo>
                    key="upcomming"
                    renderItem={({ item, index }) => <AppointmentItem key={index} item={item} />}
                    requestFunction={loadAppointmentsByDay}
                />
            </View>

            <CustomModal
                ref={modalRef}
                modalHeight={styles.modal.height}
                snapPoints={[styles.modal.height]}
                modalContent={
                    <View style={styles.modal}>
                        <Text style={styles.modalTitle}>{texts.setAbsence}</Text>
                        <View style={styles.modalContent}>
                            <Input hideTitleIfNoValue title={texts.title} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.title} onInputChange={setTitle} />
                            <Divider size={20} />
                            <Input hideTitleIfNoValue title={texts.messageForClients} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.messageForClients} onInputChange={setMessage} />
                            <Divider size={20} />
                            <View style={styles.timeInput}>
                                <Input iconBackgroundColor={"transparent"} nInputs={2} leftIcon={<FontAwesome6 name="clock" size={styles.inputIcon.width} color={styles.inputIcon.color} />} preventPaste onFocus={() => { setOpenTo(false); setOpenFrom(true); fromInputRef.current?.blur() }} hideTitleIfNoValue title={texts.from} defaultValue={getTimeAsString(from || new Date())} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.absenceFrom} ref={fromInputRef} />
                                <Divider horizontal size={5} />
                                <Input iconBackgroundColor={"transparent"} nInputs={2} leftIcon={<FontAwesome6 name="clock" size={styles.inputIcon.width} color={styles.inputIcon.color} />} preventPaste onFocus={() => { setOpenFrom(false); setOpenTo(true); toInputRef.current?.blur() }} hideTitleIfNoValue title={texts.to} defaultValue={getTimeAsString(to || new Date())} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.absenceTo} ref={toInputRef} />
                            </View>
                            <DatePicker
                                modal
                                mode="time"
                                open={openFrom}
                                date={from || new Date()}
                                onConfirm={(date) => {
                                    setFrom(date);
                                    fromInputRef.current?.setNativeProps({ text: date.toISOString().split('T')[1].split('.')[0] });
                                    fromInputRef.current?.blur();
                                    setOpenFrom(false);
                                    if (to && date > to) {
                                        setTo(date);
                                        toInputRef.current?.setNativeProps({ text: date.toISOString().split('T')[1].split('.')[0] });
                                    }
                                }}
                                onCancel={() => {
                                    fromInputRef.current?.blur();
                                    setOpenFrom(false)
                                }}
                            />
                            <DatePicker
                                modal
                                mode="time"
                                open={openTo}
                                date={to || new Date()}
                                minimumDate={from}
                                onConfirm={(date) => {
                                    setTo(date);
                                    toInputRef.current?.setNativeProps({ text: date.toISOString().split('T')[1].split('.')[0] });
                                    if (from && date < from) {
                                        setFrom(date);
                                        fromInputRef.current?.setNativeProps({ text: date.toISOString().split('T')[1].split('.')[0] });
                                    }
                                    toInputRef.current?.blur();
                                    setOpenTo(false);
                                }}
                                onCancel={() => {
                                    toInputRef.current?.blur();
                                    setOpenTo(false)
                                }}
                            />
                        </View>
                        <View style={styles.modalButton}>
                            <Button title={texts.setAbsence} onPress={setAbsence} />
                        </View>
                    </View>
                }
            />
            <View style={styles.buttonContainer}>
                <Button title={texts.setAbsence} onPress={openModal} />
            </View>
        </View >
    );
}

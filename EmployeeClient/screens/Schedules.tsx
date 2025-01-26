import { View, Text, KeyboardAvoidingView, Pressable, TextInput } from 'react-native';
import { Calendar } from "react-native-calendars";
import { useTheme } from '@styles/ThemeContext';
import { useEffect, useRef, useState } from 'react';
import { MarkedDates } from 'react-native-calendars/src/types';
import { getCalendarReadyDate, getTimeAsString } from 'utils/Utils';
import { getStyles } from '@styles/SchedulesStyles';
import PageList from '@components/PageList';
import Entypo from '@expo/vector-icons/Entypo';
import texts from '@lang/en.json';
import Button from '@components/Button';
import CustomModal, { CustomModalRef } from '@components/CustomModal';
import Input from '@components/Input';
import DatePicker from 'react-native-date-picker';

const AppointmentItem = ({ item }: { item: AppointmentInfo }) => {
    const styles = getStyles();
    return (
        <Pressable style={[styles.appointmentController, styles.shadow]}>
            <View style={styles.icon}>
                <Entypo name="dots-three-horizontal" size={styles.icon.width} color={styles.icon.color} />
            </View>
            <View style={styles.appoitmentTextContainer}>
                <Text style={styles.timeText}>{item.time}</Text>
                <Text style={styles.titleText}>{item.entityName}</Text>
                <Text style={styles.subTitleText}>{item.serviceName}</Text>
            </View>
        </Pressable >
    );
}

const Schedules = () => {
    const theme = useTheme();
    const styles = getStyles();
    const [year, setYear] = useState(new Date().getFullYear());
    const [month, setMonth] = useState(new Date().getMonth() + 1);
    const [date, setDate] = useState<string>(new Date().toISOString().split('T')[0]);
    const [markedDates, setMarkedDates] = useState<MarkedDates>({});
    const [type, setType] = useState('');
    const [message, setMessage] = useState('');
    const [from, setFrom] = useState<Date | undefined>(undefined);
    const [to, setTo] = useState<Date | undefined>(undefined);
    const fromInputRef = useRef<TextInput>(null);
    const toInputRef = useRef<TextInput>(null);
    const [openFrom, setOpenFrom] = useState(false);
    const [openTo, setOpenTo] = useState(false);
    const modalRef = useRef<CustomModalRef>(null);

    const markDates = (dates: DailyAppointments[]): MarkedDates => {
        let marked: MarkedDates = {};
        dates.forEach(d => {
            marked[getCalendarReadyDate(d.date)] = {
                dots: Array(Math.min(d.occupancy, 3)).fill({ color: d.occupancy >= 3 ? theme.colors.errorColor : d.occupancy === 2 ? theme.colors.warningColor : theme.colors.mainColor }),
            };
        });
        return marked;
    }

    // const loadUpcomming = async (page?: IPage<AppointmentInfo>, params?: AppointmentFilter) => {
    //     return await getAppointments(page, { ...params, future: true, activeOnly: true });
    // }

    const loadUpcomming = async (_page?: IPage<AppointmentInfo>, _params?: AppointmentFilter) => {
        const nItems = 3;
        const arr = Array(nItems).fill(null).map(() => (
            {
                date: "2021-09-01",
                cancelled: false,
                confirmed: true,
                id: 1,
                entityId: 1,
                serviceName: 'Service Name',
                entityName: 'Entity Name',
                establishmentAddress: 'Establishment Address',
                establishmentId: 1,
                establishmentName: 'Establishment Name',
                latitude: 0,
                longitude: 0,
                time: '10:00',
                feedback: 0,
                photo: 'https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png'
            }));
        for (let i = 0; i < arr.length; i++) {
            arr[i].id = i;
        }
        const page = {
            content: arr,
            totalPages: 1,
            totalElements: nItems,
            currentPage: 1,
            pageSize: nItems,
            hasNextPage: false,
            hasPreviousPage: false,
        }
        return Promise.resolve(
            page
        );
    }

    const loadMoreAppointments = () => {
        // const appointments = await fetchAppointments(year, month);
        const yesterday = new Date();
        yesterday.setDate(yesterday.getDate() - 1);
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        const twoDaysFromNow = new Date();
        twoDaysFromNow.setDate(tomorrow.getDate() + 1);
        setMarkedDates(markDates([
            {
                date: new Date(),
                occupancy: 1
            },
            {
                date: yesterday,
                occupancy: 3
            },
            {
                date: tomorrow,
                occupancy: 1
            },
            {
                date: twoDaysFromNow,
                occupancy: 2
            }
        ]));
    }

    useEffect(() => {
        loadMoreAppointments();
    }, [year, month]);

    useEffect(() => {
        setYear(parseInt(date.split('-')[0]));
        setMonth(parseInt(date.split('-')[1]));
    }, [date]);

    const setAbsence = () => {
    }

    const openModal = () => {
        const selectedDate = new Date(date);
        const today = new Date();
        if (selectedDate.getFullYear() === today.getFullYear() && selectedDate.getMonth() === today.getMonth() && selectedDate.getDate() === today.getDate()) {
            setFrom(today);
        } else {
            selectedDate.setMinutes(0);
            selectedDate.setHours(0);
            setFrom(selectedDate);
        }
        selectedDate.setHours(23);
        selectedDate.setMinutes(59);
        setTo(selectedDate);
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
                    requestFunction={loadUpcomming}
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
                            <Input hideTitleIfNoValue title={texts.type} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.type} onInputChange={setType} />
                            <Input hideTitleIfNoValue title={texts.messageForClients} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.messageForClients} onInputChange={setMessage} />
                            <Input preventPaste onFocus={() => { setOpenTo(false); setOpenFrom(true); fromInputRef.current?.blur() }} hideTitleIfNoValue title={texts.from} defaultValue={getTimeAsString(from || new Date())} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.absenceFrom} ref={fromInputRef} />
                            <Input preventPaste onFocus={() => { setOpenFrom(false); setOpenTo(true); toInputRef.current?.blur() }} hideTitleIfNoValue title={texts.to} defaultValue={getTimeAsString(to || new Date())} placeholderTextColor={styles.modalInput.color} containerStyle={styles.modalInput} round={false} placeholder={texts.absenceTo} ref={toInputRef} />
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

export default Schedules;

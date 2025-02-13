import { View, Text, TouchableOpacity } from 'react-native';
import { useEffect, useState } from 'react';
import { CalendarProvider, DateData, ExpandableCalendar, Timeline, TimelineList, TimelineProps } from 'react-native-calendars';
import { getCalendarDateTime, getCalendarReadyDate, getCalendarReadyTime, getDateData } from 'utils/Utils';
import texts from '@lang/en.json';
import { getStyles } from '@styles/SchedulesStyles';
import Divider from '@components/Divider';
import { PackedEvent } from 'react-native-calendars/src/timeline/EventBlock';
import { DayProps } from 'react-native-calendars/src/calendar/day';
import { fetchAppointments } from 'utils/ApiRequest';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { Params } from '@navigation/Router';

const CustomDay = (props: DayProps & { date?: DateData; }) => {
    const {
        date,
        state,
        onPress, onLongPress } = props;
    const styles = getStyles();
    const dayOfWeek = date && new Date(date?.dateString).getDay() || new Date().getDay();

    return (
        <TouchableOpacity
            onPress={() => onPress?.(date)}
            onLongPress={() => onLongPress?.(date)}
            style={[styles.dayContainer, state === 'today' && styles.dayContainerSelected]}
        >
            <Text style={styles.dayNumber}>
                {date?.day}
            </Text>
            <Divider size={5} />
            <Text style={styles.dayStr}>
                {texts.smallWeekdays[dayOfWeek]}
            </Text>
        </TouchableOpacity>
    );
};

const CustomMonth = ({ date }: { date: DateData }) => {
    const styles = getStyles();
    return (
        <View style={styles.monthContainer}>
            <Text style={styles.monthText}>
                {texts.months[date?.month - 1 || 0]}
            </Text>
            <Text style={styles.yearText}>
                {date?.year.toString()}
            </Text>
        </View>
    );
}

const CustomEvent = (_event: PackedEvent) => {
    const styles = getStyles();
    return (
        <View style={styles.eventBlock} >

        </View>
    );
}



export type Route = {
    establishmentId?: number;
};

type Props = NativeStackScreenProps<typeof Params, 'WeekView'>;

export default function WeekView({ route, navigation }: Props) {
    let establishmentId: number | undefined = undefined;
    if (route.params) {
        const { establishmentId: _establishmentId } = route.params;
        establishmentId = _establishmentId;
    }
    const styles = getStyles();
    const [currentDate, setCurrentDate] = useState(getCalendarReadyDate(new Date()));
    const [eventsByDate, setEventsByDate] = useState<{ [date: string]: TimelineProps['events'] }>({});
    const [dateData, setDateData] = useState(getDateData(new Date()));
    const INITIAL_TIME = {
        hour: 9,
        minutes: 0,
    };
    const [initialTime, setInitialTime] = useState(INITIAL_TIME);

    // useEffect(() => {
    //     if (dateData && eventsByDate[dateData.dateString] === undefined) {
    //         const exampleEvent = {
    //             start: `${dateData.year}-${String(dateData.month).padStart(2, '0')}-${String(dateData.day).padStart(2, '0')}T09:00:00`,
    //             end: `${dateData.year}-${String(dateData.month).padStart(2, '0')}-${String(dateData.day).padStart(2, '0')}T10:00:00`,
    //             title: 'Example Event',
    //             summary: 'This is an example event',
    //             color: styles.main.color
    //         };
    //
    //         setEventsByDate((prev) => ({
    //             ...prev,
    //             [dateData.dateString]: [...(prev[dateData.dateString] || []), exampleEvent],
    //         }));
    //     }
    // }, [dateData]);

    const loadEvents = async (startDate: Date) => {
        let endDate: Date = new Date();
        endDate.setDate(startDate.getDate() + 7);

        let params: AppointmentFilter = {
            userView: false,
            date: startDate.toISOString().split('T')[0],
            endDate: endDate.toISOString().split('T')[0],
        }

        if (establishmentId) {
            params.establishmentId = establishmentId;
        }
        let events: { [date: string]: TimelineProps['events'] } = {};

        const appointments = (await fetchAppointments(undefined, params))?.content;
        if (appointments) {
            for (let i = 0; i < appointments.length; i++) {
                const appointment = appointments[i];
                const start = new Date(`${appointment.date}T${appointment.time}`);
                const startStr = getCalendarDateTime(start);
                let end = new Date(start);
                end.setMinutes(end.getMinutes() + appointment.duration);
                const endStr = getCalendarDateTime(end);
                if (!events[getCalendarReadyDate(start)]) {
                    events[getCalendarReadyDate(start)] = [];
                }
                events[getCalendarReadyDate(start)].push({
                    start: startStr,
                    end: endStr,
                    title: appointment.entityName,
                    summary: appointment.serviceName,
                    color: styles.main.color,
                });
            }
        }
        setEventsByDate(events);
    }

    useEffect(() => {
        loadEvents(new Date());
    }, []);

    const onDateChanged = (date: string) => {
        const today = new Date();
        if (date === getCalendarReadyDate(today)) {
            setInitialTime(getCalendarReadyTime(today));
        } else {
            setInitialTime(INITIAL_TIME);
        }
        setCurrentDate(date);
        loadEvents(new Date(date));
    };

    return (
        <CalendarProvider
            style={styles.weekDayContainer}
            date={currentDate}
            onDateChanged={onDateChanged}
            showTodayButton
            numberOfDays={7}
            onMonthChange={(date: DateData) => setDateData(date)}
        >
            <ExpandableCalendar
                customHeaderTitle={<CustomMonth date={dateData} />}
                hideDayNames
                hideArrows
                dayComponent={CustomDay}
            />
            <Divider size={40} />
            <TimelineList
                events={eventsByDate}
                showNowIndicator
                scrollToNow
                scrollToFirst
                initialTime={initialTime}
                renderItem={(timelineProps, info) => <Timeline
                    {...timelineProps}
                    {...info}
                    renderEvent={(event) => <CustomEvent {...event} />}
                />}
            />
        </CalendarProvider>
    );
};

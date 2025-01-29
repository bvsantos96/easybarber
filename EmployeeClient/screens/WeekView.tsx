import { View, Text, TouchableOpacity } from 'react-native';
import { useEffect, useState } from 'react';
import { CalendarProvider, DateData, ExpandableCalendar, TimelineList, TimelineProps } from 'react-native-calendars';
import { getCalendarReadyDate, getCalendarReadyTime, getDateData } from 'utils/Utils';
import texts from '@lang/en.json';
import { getStyles } from '@styles/SchedulesStyles';
import Divider from '@components/Divider';

const CustomDay = (props: any) => {
    const {
        date,
        /** The Day's state ('selected' | 'disabled' | 'inactive' | 'today' | '') */
        state,
        onPress, onLongPress } = props;
    const styles = getStyles();
    const dayOfWeek = new Date(date.dateString).getDay();

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

const WeekView = () => {
    const styles = getStyles();
    const [currentDate, setCurrentDate] = useState(getCalendarReadyDate(new Date()));
    const [eventsByDate, setEventsByDate] = useState<{ [date: string]: TimelineProps['events'] }>({});
    const [dateData, setDateData] = useState(getDateData(new Date()));
    const INITIAL_TIME = {
        hour: 9,
        minutes: 0,
    };
    const [initialTime, setInitialTime] = useState(INITIAL_TIME);

    useEffect(() => {
        if (dateData) {
            const exampleEvent = {
                start: `${dateData.year}-${String(dateData.month).padStart(2, '0')}-${String(dateData.day).padStart(2, '0')}T09:00:00`,
                end: `${dateData.year}-${String(dateData.month).padStart(2, '0')}-${String(dateData.day).padStart(2, '0')}T10:00:00`,
                title: " ",
                summary: undefined,
                color: styles.main.color
            };

            setEventsByDate((prev) => ({
                ...prev,
                [dateData.dateString]: [...(prev[dateData.dateString] || []), exampleEvent],
            }));
        }
    }, [dateData]);

    const onDateChanged = (date: string) => {
        const today = new Date();
        if (date === getCalendarReadyDate(today)) {
            setInitialTime(getCalendarReadyTime(today));
        } else {
            setInitialTime(INITIAL_TIME);
        }
        setCurrentDate(date);
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
            />
        </CalendarProvider>
    );
};

export default WeekView;

import { View, Text } from 'react-native';
import { getStyles } from '@styles/TimeSheet';
import texts from '@lang/en.json';
import { useRef, useState } from 'react';
import Pressable from '@components/Pressable';

const TimeSheetDay = ({ text, selected = false, select }: { text: string, selected?: boolean, select: () => void }) => {
    const styles = getStyles();
    return (
        <Pressable useGradient={false} onPress={select} style={[styles.weekdayContainer, selected && styles.weekdaySelected]}>
            <Text style={[styles.weekdayText, selected && styles.weekdayTextSelected]}>{text}</Text>
        </Pressable>
    );
}

const TimeSheet = () => {
    const styles = getStyles();
    const days = useRef([...Array(7).keys()]);
    const [selectedDay, setSelectedDay] = useState(0);

    const updateSelectedDay = (newDay: number) => {
        if (newDay >= 0 && newDay <= days.current.length) {
            setSelectedDay(newDay);
        }
    };
    return (
        <View style={styles.weekdaysContainer}>
            {days.current?.map((day, index) => (
                <TimeSheetDay key={index} text={texts.weekday.short[day]} selected={selectedDay === day} select={() => { updateSelectedDay(index) }} />
            ))}
        </View>
    );
}

export default TimeSheet;

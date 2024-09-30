import React, { useState } from "react";
import { getStyles } from "../styles/Availability";
import Selection from "./Selection";
import { ALERT_TYPE } from "react-native-alert-notification";
import { Banner } from "@components/Alert";
import { Calendar } from "react-native-calendars";
import { View, Text } from "react-native";
import { useTheme } from "@styles/ThemeContext";
import { MarkedDates } from "react-native-calendars/src/types";
import { Underline } from "@components/Underline";
import TimeSlotView from "@components/TimeSlotView";
import PagerView from "react-native-pager-view";

export default function Availability() {
    const theme = useTheme();
    const texts = require('@lang/en.json');
    const styles = getStyles();
    const [date, setDate] = useState<string>("");
    const [time, setTime] = useState<TimeSlot | null>(null);
    const disableDates = (dates: string[]): MarkedDates => {
        let disabled: MarkedDates = {};
        dates.forEach(d => {
            disabled[d] = { selected: false, disabled: true, disableTouchEvent: true };
        });
        return disabled;
    }
    const [unSelectable, setUnSelectable] = useState<MarkedDates>(disableDates(["2024-09-10"]));

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
    const [timeSlots, setTimeSlots] = useState<TimeSlot[][][]>(buildTimeSlotViews([
        { start: "09:00", end: "09:30" },
        { start: "09:30", end: "10:00" },
        { start: "10:00", end: "10:30" },
        { start: "10:30", end: "11:00" },
        { start: "11:00", end: "11:30" },
        { start: "11:30", end: "12:00" },
    ]));



    return (
        <Selection
            buttonText={texts.appointments.book}
            selectionText={texts.appointments.scheduleSelection}
            onButtonPress={
                () => {
                    Banner({ type: ALERT_TYPE.SUCCESS, message: "Booked Appointment" });
                }
            }
            selected={date.length > 0 && time !== null}
        >
            <View style={styles.calendar}>
                <Calendar
                    onDayPress={day => {
                        if (day.dateString !== date) {
                            setDate(day.dateString);
                            setTime(null);
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
                <PagerView style={styles.timeSlotsContainer}>
                    {timeSlots.map((item, index) => (
                        <TimeSlotView
                            key={index}
                            offset={index * elemsPerPage}
                            select={setTime}
                            selected={time}
                            slots={item}
                        />
                    ))}
                </PagerView>
            </View>
        </Selection>
    );
}

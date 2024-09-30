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

export default function Availability() {
    const theme = useTheme();
    const texts = require('@lang/en.json');
    const styles = getStyles();
    const [date, setDate] = useState<string>("");
    const [time, setTime] = useState<string>("");

    const disableDates = (dates: string[]): MarkedDates => {
        let disabled: MarkedDates = {};
        dates.forEach(d => {
            disabled[d] = { selected: false, disabled: true, disableTouchEvent: true };
        });
        return disabled;
    }
    const [unSelectable, setUnSelectable] = useState<MarkedDates>(disableDates(["2024-09-10"]));

    return (
        <Selection
            buttonText={texts.appointments.book}
            selectionText={texts.appointments.scheduleSelection}
            onButtonPress={
                () => {
                    Banner({ type: ALERT_TYPE.SUCCESS, message: "Booked Appointment" });
                }
            }
            selected={date.length > 0 && time.length > 0}
        >
            <View style={styles.calendar}>
                <Calendar
                    onDayPress={day => {
                        setDate(day.dateString);
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
            </View>
        </Selection>
    );
}

import { View } from "react-native";
import TimeSlot from "./TimeSlot";
import { getStyles } from "@styles/TimeSlotView";

export interface TimeSlotProps {
    slot: TimeSlot,
    select: (s: TimeSlot) => void,
    selected: boolean
}

interface Props {
    select: (s: TimeSlot) => void,
    selected: TimeSlot | undefined,
    slots: TimeSlot[][],
    offset: number;
}

export default function TimeSlotView({ slots = [], select, selected, offset }: Props) {
    const styles = getStyles();
    return (
        <View style={styles.container}>
            {slots.map((line, index) => (
                <View key={offset + index * line.length} style={styles.line}>
                    {line.map((slot, j) => {
                        return (
                            <TimeSlot key={offset + index * line.length + j} slot={slot} selected={selected === slot} select={select} />
                        )
                    })}
                </View>
            ))}
        </View>
    );
}

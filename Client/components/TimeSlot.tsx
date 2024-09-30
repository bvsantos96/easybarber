import { Text } from 'react-native';
import Pressable from "./Pressable";
import { getStyles } from "@styles/Availability";

interface TimeSlotProps {
    slot: TimeSlot,
    select: (s: TimeSlot) => void,
    selected: boolean
}

export default function TimeSlot({ select, slot, selected }: TimeSlotProps) {
    const styles = getStyles();
    const texts = require("@lang/en.json");
    return (
        <Pressable
            onPress={() => { select(slot) }}
            style={[styles.slotContainer, selected ? styles.selectedBorder : {}]}>
            <Text style={[styles.slotText, selected ? styles.selectedText : {}]}>{`${slot.start}AM ${texts.to} ${slot.end}AM`}</Text>
        </Pressable>
    );
}

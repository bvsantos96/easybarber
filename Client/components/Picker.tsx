import RNPickerSelect, { PickerStyle } from 'react-native-picker-select';
import { View, Text } from 'react-native';

import { getStyles } from '../styles/Filter';

export interface PickerItem {
    label: string;
    value: string;
}

export default function Picker({ 
    style = {},
    placeholder,
    selectedValue = "",
    onValueChange = () => { },
    items = []
}: {
    style?: PickerStyle,
    placeholder?: string,
    selectedValue: string,
    onValueChange: (value: string) => void,
    items: PickerItem[]
}
) {
    const styles = getStyles();
    return (
        <>
            <RNPickerSelect
                textInputProps={{ style: styles.pickerItem }}
                value={selectedValue}
                style={style}
                onValueChange={onValueChange}
                items={items}
            >
            </RNPickerSelect>
            <View style={styles.pickerLabelContainer}>
                <Text style={styles.pickerLabel}>{placeholder}</Text>
            </View>
        </>
    );
}

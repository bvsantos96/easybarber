import AntDesign from '@expo/vector-icons/AntDesign';
import { useEffect, useRef, useState } from 'react';
import { Text, View } from 'react-native';
import { Dropdown, IDropdownRef } from 'react-native-element-dropdown';

import texts from '@lang/en.json';
import { getStyles as getStylesCE } from "@styles/ChangeEstablishment";
import { getStyles } from '@styles/Input';
import React from 'react';
import Divider from './Divider';
import Input from './Input';

const AddressInput = ({ defaultValue, onInputChange }: { defaultValue?: string, onInputChange: Function }) => {
    const styles = getStyles();
    const stylesCE = getStylesCE();
    const [selected, setSelected] = useState<string>(defaultValue || "");
    const dropDownRef = useRef<IDropdownRef>(null);
    const [addressListBox, setAddressListBox] = useState<string[]>(["test"]);

    const renderItem = (item: { label: string, value: string }) => {
        const isSelected = item.value === selected;
        return (
            <View style={{ padding: 17, flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={[stylesCE.textItem, isSelected ? stylesCE.textItemSelected : undefined]}>{item.label}</Text>
                {isSelected && (
                    <AntDesign style={styles.icon} color={stylesCE.icon.color} name="check" size={styles.icon.width} />
                )}
            </View>
        );
    };

    useEffect(() => {
        onInputChange(selected);
    }, [selected]);

    const searchAddress = (value: string) => {
        if (value.length <= 0) {
            dropDownRef.current?.close();
            setAddressListBox([]);
            return;
        }
        dropDownRef.current?.open();
        setAddressListBox([value]);
        setSelected(value);
    }

    return (
        <>
            <Dropdown
                ref={dropDownRef}
                //style={styles.textInput}
                iconStyle={styles.hiddenInput}
                placeholderStyle={styles.hiddenInput}
                //selectedTextStyle={styles.hiddenInput}
                //inputSearchStyle={styles.hiddenInput}
                //containerStyle={styles.hiddenInput}
                data={addressListBox.map(est => ({ label: est, value: est }))}
                labelField="label"
                valueField="value"
                placeholder={(defaultValue && defaultValue.length > 0) ? defaultValue : texts.address}
                value={selected}
                search={false}
                mode="modal"
                onChange={item => {
                    setSelected(item.value);
                }}
                renderItem={renderItem}
            />
            <Input
                hideTitleIfNoValue
                placeholder={texts.address}
                containerStyle={styles.smallInput}
                title={texts.address}
                round={false}
                defaultValue={selected}
                onInputChange={searchAddress}
                onFocus={() => dropDownRef.current?.open()}
                onBlur={() => dropDownRef.current?.close()}
            />
            <Divider horizontal size={styles.iconView.margin + styles.iconView.marginRight} />
        </>
    );
}

export default AddressInput;

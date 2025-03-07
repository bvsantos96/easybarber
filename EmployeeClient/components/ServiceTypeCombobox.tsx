import AntDesign from '@expo/vector-icons/AntDesign';
import { useEffect, useRef, useState } from 'react';
import { Text, View } from 'react-native';
import { Dropdown, IDropdownRef } from 'react-native-element-dropdown';

import texts from '@lang/en.json';
import { getStyles as getStylesCE } from "@styles/ChangeEstablishment";
import { getStyles } from '@styles/Input';
import useServiceTypeStore from 'storage/stores/ServiceTypeStore';
import Divider from './Divider';
import Pressable from './Pressable';

const ServiceTypeCombobox = ({ defaultValue, onInputChange }: { defaultValue?: ICategory, onInputChange: Function }) => {
    const styles = getStyles();
    const stylesCE = getStylesCE();
    const { serviceTypes } = useServiceTypeStore();
    const [selected, setSelected] = useState<ICategory | undefined>(defaultValue);
    const [showTitle, setShowTitle] = useState(!!defaultValue);
    const dropDownRef = useRef<IDropdownRef>(null);

    const handleViewPress = () => {
        dropDownRef.current?.open();
    };

    const renderItem = (item: { label: string, value: EstablishmentBase }) => {
        const isSelected = item.value.id === selected?.id;
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
        setShowTitle(!!selected)
        onInputChange(selected);
    }, [selected]);

    return (
        <Pressable style={[styles.container]} onPress={handleViewPress}>
            <View style={[styles.inputView, styles.inputSmallBorderRadius, styles.smallInput]}>
                <Divider horizontal size={styles.iconView.margin + styles.iconView.marginRight} />
                {showTitle && <Text style={styles.title}>{texts.serviceType}</Text>}
                <Dropdown
                    ref={dropDownRef}
                    style={styles.textInput}
                    placeholderStyle={[stylesCE.placeholderStyle, selected ? stylesCE.selectedPlaceholderStyle : undefined]}
                    selectedTextStyle={stylesCE.selectedTextStyle}
                    inputSearchStyle={styles.textInputWithTwoIcons}
                    data={serviceTypes.map(est => ({ label: est.name, value: est }))}
                    labelField="label"
                    valueField="value"
                    placeholder={defaultValue?.name || texts.serviceType}
                    value={selected}
                    onChange={item => {
                        if (item.value.id === selected?.id)
                            return setSelected(undefined);
                        setSelected(item.value);
                    }}
                    renderItem={renderItem}
                />
                <Divider horizontal size={styles.iconView.margin + styles.iconView.marginRight} />
            </View>
        </Pressable>
    );
}

export default ServiceTypeCombobox;

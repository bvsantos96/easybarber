import AntDesign from '@expo/vector-icons/AntDesign';
import { useEffect, useRef, useState } from 'react';
import { Text, View } from 'react-native';
import { Dropdown, IDropdownRef } from 'react-native-element-dropdown';

import texts from '@lang/en.json';
import { getStyles as getStylesCE } from "@styles/ChangeEstablishment";
import { getStyles } from '@styles/Input';
import useEstablishmentStore from 'storage/stores/EstablishmentStore';
import Divider from './Divider';
import Pressable from './Pressable';

const EstablishmentCombobox = ({ defaultValue, onInputChange }: { defaultValue?: number, onInputChange: Function }) => {
    const styles = getStyles();
    const stylesCE = getStylesCE();
    const { selectedEstablishment, establishments } = useEstablishmentStore();
    const [selected, setSelected] = useState<SelectedItem | undefined>(undefined);
    const [showTitle, setShowTitle] = useState(!!defaultValue);
    const dropDownRef = useRef<IDropdownRef>(null);

    useEffect(() => {
        if (defaultValue) {
            for (let i = 0; i < establishments.length; i++) {
                if (establishments[i].id === defaultValue) {
                    setSelected({
                        id: establishments[i].id,
                        idx: i,
                        admin: establishments[i].admin,
                        name: establishments[i].name
                    });
                    return;
                }
            }
        } else {
            setSelected(selectedEstablishment);
        }
    }, []);

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
                {showTitle && <Text style={styles.title}>{texts.navigation.establishment.name}</Text>}
                <Dropdown
                    ref={dropDownRef}
                    style={styles.textInput}
                    placeholderStyle={[stylesCE.placeholderStyle, selected ? stylesCE.selectedPlaceholderStyle : undefined]}
                    selectedTextStyle={stylesCE.selectedTextStyle}
                    inputSearchStyle={styles.textInputWithTwoIcons}
                    data={establishments.map(est => ({ label: est.name, value: est }))}
                    labelField="label"
                    valueField="value"
                    placeholder={selected?.name || texts.navigation.establishment.name}
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

export default EstablishmentCombobox;

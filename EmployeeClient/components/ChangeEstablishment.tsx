import AntDesign from '@expo/vector-icons/AntDesign';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import { Text, View } from "react-native";
import { Dropdown } from 'react-native-element-dropdown';

import Divider from "./Divider";
import Pressable from "./Pressable";

import { getStyles } from "@styles/ChangeEstablishment";

import texts from "@lang/en.json";
import { useEffect, useRef, useState } from "react";
import useEstablishmentStore from "storage/stores/EstablishmentStore";
import Button from "./Button";
import CustomModal, { CustomModalRef } from "./CustomModal";

const SelectEstablishments = ({ selected: _selected, establishments, closeModal }: { selected?: SelectedItem, establishments: EstablishmentBase[], closeModal: () => void }) => {
    const styles = getStyles();
    const { setSelectedEstablishment } = useEstablishmentStore();
    const [selected, setSelected] = useState<SelectedItem | EstablishmentBase | undefined>(_selected || undefined);

    const selectEstablishment = () => {
        setSelectedEstablishment(selected);
        closeModal();
    }
    const renderItem = (item: { label: string, value: EstablishmentBase }) => {
        const isSelected = item.value.id === selected?.id;
        return (
            <View style={styles.item}>
                <Text style={[styles.textItem, isSelected ? styles.textItemSelected : undefined]}>{item.label}</Text>
                {isSelected && (
                    <AntDesign style={styles.icon} color={styles.icon.color} name="Safety" size={styles.icon.width} />
                )}
            </View>
        );
    };

    return (
        <View style={styles.modal}>
            <Dropdown
                dropdownPosition='top'
                style={styles.dropdown}
                placeholderStyle={[styles.placeholderStyle, selected ? styles.selectedPlaceholderStyle : undefined]}
                selectedTextStyle={styles.selectedTextStyle}
                inputSearchStyle={styles.inputSearchStyle}
                iconStyle={styles.iconStyle}
                data={establishments.map(est => ({ label: est.name, value: est }))}
                search
                maxHeight={styles.dropdownContainer.height}
                labelField="label"
                valueField="value"
                placeholder={selected?.name || texts.selectEstablishment}
                searchPlaceholder={texts.search}
                value={selected}
                onChange={item => {
                    if (item.value.id === selected?.id)
                        return setSelected(undefined);
                    setSelected(item.value);
                }}
                renderLeftIcon={() => (
                    <AntDesign style={styles.icon} color={styles.icon.color} name="Safety" size={styles.icon.width} />
                )}
                renderItem={renderItem}
            />
            <View style={styles.modalButton} >
                <Button title={texts.select} onPress={selectEstablishment} />
            </View>
        </View>
    );
}

export const ChangeEstablishment = ({ canClose = true, autoOpen = false }: { canClose?: boolean, autoOpen?: boolean }) => {
    const styles = getStyles();
    const { establishments, selectedEstablishment, clearSelectedEstablishment } = useEstablishmentStore();
    const modal = useRef<CustomModalRef>(null);
    const [modalVisible, setModalVisible] = useState(autoOpen);

    useEffect(() => {
        if (autoOpen && !modalVisible && !selectedEstablishment) {
            setModalVisible(true);
            modal.current?.showModal();
        }
    }, [selectedEstablishment]);

    return (
        <View style={styles.container}>
            <CustomModal
                autoOpen={autoOpen}
                ref={modal}
                modalContent={
                    <SelectEstablishments
                        establishments={establishments}
                        closeModal={() => { setModalVisible(false); modal.current?.hideModal(); }}
                        selected={selectedEstablishment}
                    />
                }
                snapPoints={[styles.modal.maxHeight]}
                modalHeight={styles.modal.maxHeight}
                modalClosed={() => {
                    if (!canClose && !selectedEstablishment) {
                        modal.current?.showModal();
                        setModalVisible(true);
                        return;
                    }
                    setModalVisible(false);
                }}
            />
            {(selectedEstablishment && !modalVisible) &&
                <View style={styles.selected}>
                    <View style={styles.container}>
                        {establishments.length > 1 &&
                            <Pressable style={styles.removeButton} onPress={clearSelectedEstablishment}>
                                <MaterialCommunityIcons name="close" size={styles.removeButton.minWidth} color={styles.removeButton.color} />
                            </Pressable>
                        }
                        <Text style={styles.selectedText}>{selectedEstablishment?.name}</Text>
                        <Divider horizontal size={50} />
                    </View>
                </View>
            }
            {(establishments.length > 1 && !modalVisible) &&
                <Pressable style={styles.button} onPress={() => {
                    modal?.current?.toggleModal();
                    if (!modalVisible) setModalVisible(true);
                }}>
                    <MaterialCommunityIcons name="home-switch-outline" size={styles.button.minWidth} color={styles.button.color} />
                </Pressable>
            }
        </View >
    );
}

// Libraries
import React, { useEffect, useRef, useState } from 'react';
import { View, Text, Linking } from 'react-native';
import { Image } from 'expo-image';
import Fontisto from '@expo/vector-icons/Fontisto';

// Requests
import { addEmployee, fireEmployee, getEmployee, getEmployees } from 'utils/ApiRequest';

// Components
import PageList, { PageListRef } from '@components/PageList';
import Pressable from '@components/Pressable';
import SlidingItem from '@components/SlidingItem';

// Styles
import { getStyles } from '@styles/Employees';
import { useTheme } from '@styles/ThemeContext';
import useAlertStore from 'storage/stores/AlertStore';
import { AlertType } from '@components/Alert';

// Texts
import texts from '@lang/en.json';
import { Routes } from '@navigation/Router';
import { NavigationProp, useRoute } from '@react-navigation/native';
import useEstablishmentStore from 'storage/stores/EstablishmentStore';
import Divider from '@components/Divider';
import useHeaderStore from 'storage/stores/HeaderStore';
import CustomModal, { CustomModalRef } from '@components/CustomModal';
import Button from '@components/Button';
import PhoneInput from '@components/PhoneInput';
import { Country } from 'react-native-country-picker-modal';
import { parsePhoneNumber } from 'utils/Utils';
import { getDefaultCountryAsync } from 'utils/Constants';
import CategoryList from '@components/CategoryList';
import useServiceTypeStore from 'storage/stores/ServiceTypeStore';

const Employee = ({ item, fireEmployee, navigation }: { item: EmployeeListInfo, fireEmployee: (id: number) => {}, navigation: NavigationProp<any, any> }) => {
    const styles = getStyles();
    const theme = useTheme();
    const { alert } = useAlertStore();

    const call = async () => {
        Linking.openURL(`tel:${item.mobileNumber}`).catch((err) =>
            console.error('Error opening dialer:', err)
        );
    }

    return (
        <>
            <Divider size={5} horizontal={false} />
            <View style={styles.slidingContainer} key={item.id}>
                <SlidingItem
                    items={[
                        <Pressable key={"calendar"} onPress={() => navigation.navigate(Routes.Schedules, { employeeId: item.id })} style={[styles.icon]} >
                            <Fontisto name="calendar" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                        </Pressable>,
                        <Pressable key={"phone"} onPress={call} style={[styles.icon]} >
                            <Fontisto name="phone" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                        </Pressable>,
                        <Pressable key={"trash"} onPress={async () => {
                            alert({
                                type: AlertType.Error,
                                message: texts.employee.fire,
                                buttonText: texts.yes,
                                onPress: () => {
                                    fireEmployee(+item.id);
                                },
                                onPress2: () => { },
                                buttonText2: texts.no
                            });
                        }} style={[styles.icon, styles.redIcon]} >
                            <Fontisto name="trash" size={styles.icon.fontSize} color={theme.colors.backgroundColor} />
                        </Pressable>
                    ]}
                >
                    <View style={styles.listItemContainer}>
                        <View style={styles.imageContainer} >
                            <Image
                                cachePolicy="memory"
                                source={{ uri: item.image }}
                                style={styles.imageStyle}
                            />
                        </View>
                        <View style={styles.textContainer}>
                            <Divider size={5} horizontal={false} />
                            <Text style={styles.titleText}>{item.name}</Text>
                            <Text style={styles.descriptionText}>{item.name}</Text>
                            <Divider size={5} horizontal={false} />
                        </View>
                        <Text style={styles.statusText}>{item.absent ? texts.status.absent : texts.status.active}</Text>
                    </View>
                </SlidingItem>
            </View>
            <Divider size={5} horizontal={false} />
        </>
    );
}

const DisplayEmployee = ({ employee, addFunc }: { employee?: EmployeeBase, addFunc: () => void }) => {
    const styles = getStyles();
    const { getServices } = useServiceTypeStore();
    const categories = useRef(getServices(employee?.serviceTypes || []));
    return (
        <View style={styles.displayEmployeeContainer}>
            <Image
                cachePolicy="memory"
                source={{ uri: employee?.image }}
                style={styles.displayEmployeeImageStyle} />
            <Text style={styles.displayEmployeeRating}>{`${employee?.rating}/${employee?.nvotes}`}</Text>
            <Text style={styles.displayEmployeeName}>{employee?.name}</Text>
            <Text style={styles.displayEmployeeDesc}>{employee?.description}</Text>
            <View style={styles.servicesContainer}>
                <CategoryList
                    categorySize={45}
                    categories={categories.current}
                    maxWidth={styles.inputWidth.width} />

            </View>
            <View style={styles.modalButton}>
                <Button title={texts.employee.add} onPress={addFunc} />
            </View>
        </View>
    );
}

const AddEmployee = ({ setEmployee }: { setEmployee: (employee: EmployeeBase) => void }) => {
    const styles = getStyles();
    const [phoneNumber, setPhoneNumber] = useState('');
    const [nation, setNation] = useState<Country | null>();
    const { alert } = useAlertStore();

    useEffect(() => {
        const fetchDefaultCountry = async () => {
            try {
                const DEFAULT_COUNTRY = await getDefaultCountryAsync();
                setNation(DEFAULT_COUNTRY);
            } catch (error) {
                console.error(texts.errors.defaultCountry, error);
            }
        };

        fetchDefaultCountry();
    }, []);

    const searchEmployee = async () => {
        const employee = await getEmployee(parsePhoneNumber(nation?.callingCode[0] || "", phoneNumber));
        if (employee) {
            setEmployee(employee);
        } else {
            alert({ type: AlertType.Error, message: texts.employee.notFound });
        }
    }

    return (
        <View style={styles.addEmployeeContainer}>
            <Text style={styles.addEmployeeText}>{texts.employee.add}</Text>
            <View style={styles.phoneNumberContainer}>
                <PhoneInput
                    username={true}
                    {...{
                        setPhone: setPhoneNumber,
                        setNation,
                        nation
                    }}
                />
            </View>
            <View style={styles.modalButton}>
                <Button
                    disabled={!nation || !phoneNumber || phoneNumber.length == 0}
                    title={texts.employee.search}
                    onPress={searchEmployee} />
            </View>
        </View>
    );
}

export default function Employees({ navigation }: PropNavigation) {
    const { pressed } = useHeaderStore();
    const { selectedEstablishment } = useEstablishmentStore();
    const styles = getStyles();
    const [resetList, setResetList] = useState(false);
    const pageListRef = useRef<PageListRef<EmployeeListInfo>>(null);
    const addEmployeeModal = useRef<CustomModalRef>(null);
    const displayEmployeeModal = useRef<CustomModalRef>(null);
    const [employee, setEmployee] = useState<EmployeeBase>();
    const route = useRoute();

    const loadEmployees = async (page?: IPage<EmployeeListInfo>, params?: EmployeeFilter) => {
        if (!selectedEstablishment?.id) {
            return;
        }
        return await getEmployees(+selectedEstablishment.id, page, params);
    }

    useEffect(() => {
        navigation.setParams({ hideSecondHeader: !(route.params as any)?.hideSecondHeader });
    }, [selectedEstablishment]);

    useEffect(() => {
        if (employee) {
            addEmployeeModal.current?.toggleModal();
            displayEmployeeModal.current?.toggleModal();
        }
    }, [employee]);

    useEffect(() => {
        if (pressed != undefined) {
            addEmployeeModal.current?.toggleModal();
        }
    }, [pressed]);

    const hireEmployee = async () => {
        if (!employee || !selectedEstablishment || employee.id == undefined || selectedEstablishment.id == undefined || employee.id == 0 || selectedEstablishment.id == 0) {
            return;
        }
        await addEmployee(+employee.id, +selectedEstablishment.id);
        displayEmployeeModal.current?.toggleModal();
    }

    const _fireEmployee = async (id: number) => {
        if (!selectedEstablishment?.id) return;
        await fireEmployee(+selectedEstablishment.id, id);
    }

    return (
        <View style={styles.container}>
            <CustomModal
                ref={displayEmployeeModal}
                modalContent={
                    <DisplayEmployee employee={employee} addFunc={hireEmployee} />
                }
                snapPoints={[styles.displayEmployeeContainer.height]}
                modalHeight={styles.displayEmployeeContainer.height}
            />
            <CustomModal
                ref={addEmployeeModal}
                modalContent={
                    <AddEmployee setEmployee={setEmployee} />
                }
                snapPoints={[styles.addEmployeeContainer.maxHeight]}
                modalHeight={styles.addEmployeeContainer.maxHeight}
            />
            <View style={styles.listContainer}>
                <PageList<EmployeeListInfo>
                    key={selectedEstablishment?.id}
                    reset={resetList}
                    ref={pageListRef}
                    renderItem={({ item }) => <Employee fireEmployee={_fireEmployee} item={item} navigation={navigation} />}
                    requestFunction={loadEmployees}
                />
            </View>
        </View>
    );
}

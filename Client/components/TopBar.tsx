import React, { useEffect, useState } from 'react';
import { StatusBar } from 'expo-status-bar';
import { View } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from './Pressable';
import { getStyles } from '../styles/TopBar';
import { useTheme } from '../styles/ThemeContext';
import { IFilterRequest, ILocation } from '../declarations';
import ModalTextButton from './ModalTextButton';
import { getAddressFromCoordinates } from '../utils/Location';

import FilterIcon from '@assets/icons/filter.svg';
import BellIcon from '@assets/icons/bell.svg';
import CustomModal, { CustomModalRef } from './Modal';
import Filter from '../screens/Filter';
import LocationModal from './LocationModal';

interface TopBarProps {
    name?: string;
    setFilter: (_filter: IFilterRequest) => void;
    setName: (partialName: string) => void;
    location?: ILocation,
    filter?: IFilterRequest;
}

export default function TopBar({ filter, setFilter, setName, location }: TopBarProps) {
    const styles = getStyles();
    const theme = useTheme();
    const texts = require('../langs/en.json');
    const [currentSelectedAddress, setCurrentSelectedAddress] = useState<string | undefined>(undefined);
    const locationModalRef = React.createRef<CustomModalRef>();
    const filterModalRef = React.createRef<CustomModalRef>();

    useEffect(() => {
        const _getAddress = async (_location: ILocation) => {
            if (_location.address === undefined || _location.address === null || _location.address === "" || _location.address !== currentSelectedAddress) {
                if (_location.address === undefined || _location.address === null || _location.address === "") {
                    _location.address = (await getAddressFromCoordinates(_location.latitude, _location.longitude))?.name ?? "";
                }
                setCurrentSelectedAddress(_location.address);
            }
        }
        if (location !== undefined && location !== null) {
            let _location = location;
            _getAddress(_location);
        }
    }, [location]);

    return (
        <View style={styles.container}>
            <StatusBar style={theme.colors.statusBarOnHome} />
            <View style={styles.elementsContainer}>
                <View style={styles.topElements}>
                    <CustomModal ref={locationModalRef} modalContent={
                        <LocationModal toggleModal={() => { locationModalRef?.current?.toggleModal(); }} />
                    }
                        modalHeight={theme.dimensions.height * 0.7}
                    >
                        <ModalTextButton buttonText={currentSelectedAddress} style={{ maxWidth: "65%" }} />
                    </CustomModal>
                    <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                        <BellIcon width={styles.bell.width} height={styles.bell.height} fill={"none"} />
                    </Pressable>
                    <ProfileImage />
                </View>
                <View style={styles.searchContainer}>
                    <View style={styles.searchBarContainer} >
                        <SearchBar placeholder={texts.search} search={() => setFilter({})} onTextChange={setName} />
                    </View>
                    <CustomModal
                        ref={filterModalRef}
                        buttonStyle={styles.filterView}
                        modalContent={<Filter filter={filter} setFilter={(filter: IFilterRequest) => { setFilter(filter); filterModalRef.current?.toggleModal() }} />}
                        modalHeight={422 * theme.dimensions.absoluteHeight + theme.dimensions.input.height} >
                        <FilterIcon width={styles.filter.width} height={styles.filter.height} />
                    </CustomModal>
                </View>
            </View>
        </View>
    );
}

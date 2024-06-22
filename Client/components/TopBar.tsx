import React, { useEffect, useState } from 'react';
import { StatusBar } from 'expo-status-bar';
import { View, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from './Pressable';
import { getStyles } from '../styles/TopBar';
import { useTheme } from '../styles/ThemeContext';
import { IFilterRequest } from '../declarations';
import ModalTextButton from './ModalTextButton';
import { getCachedAddress } from '../utils/Location';

import FilterIcon from '@assets/icons/filter.svg';
import BellIcon from '@assets/icons/bell.svg';
import { LocationGeocodedAddress } from 'expo-location';
import CustomModal from './Modal';
import Filter from '../screens/Filter';
import ChangeLocation from './ChangeLocation';

interface TopBarProps {
    name?: string;
    setFilter: (_filter: IFilterRequest) => void;
    setName: (partialName: string) => void;
    filter?: IFilterRequest;
}

export default function TopBar({ filter, setFilter, setName }: TopBarProps) {
    const styles = getStyles();
    const theme = useTheme();

    const [currentSelectedAddress, setCurrentSelectedAddress] = useState<LocationGeocodedAddress | undefined>(undefined);

    useEffect(() => {
        const fetchCachedAddress = async () => {
            try {
                const address = await getCachedAddress();
                setCurrentSelectedAddress(address);
            } catch (error) {
                console.error('Error fetching cached address:', error);
            }
        };

        fetchCachedAddress();
    }, []);

    return (
        <View style={styles.container}>
            <StatusBar style={theme.colors.statusBarOnHome} />
            <View style={styles.elementsContainer}>
                <View style={styles.topElements}>
                    <CustomModal modalContent={
                        <ChangeLocation />
                    }
                        modalHeight={theme.dimensions.height * 0.7}
                    >
                        <ModalTextButton buttonText={currentSelectedAddress?.name || ""} />
                    </CustomModal>
                    <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                        <BellIcon width={styles.bell.width} height={styles.bell.height} fill={"none"} />
                    </Pressable>
                    <ProfileImage />
                </View>
                <View style={styles.searchContainer}>
                    <SearchBar search={() => setFilter({})} onTextChange={setName} />
                    <CustomModal
                        buttonStyle={styles.filterView}
                        modalContent={<Filter filter={filter} setFilter={setFilter} />}
                        modalHeight={422 * theme.dimensions.absoluteHeight + theme.dimensions.input.height} >
                        <FilterIcon width={styles.filter.width} height={styles.filter.height} />
                    </CustomModal>
                </View>
            </View>
        </View>
    );
}

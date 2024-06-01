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

interface TopBarProps {
    name?: string;
    toggleFilter: () => void;
    setFilter: (_filter: IFilterRequest) => void;
    setName: (partialName: string) => void;
}

export default function TopBar({ name = "Jane Doe", toggleFilter, setFilter, setName }: TopBarProps) {
    const styles = getStyles();
    const texts = require("../langs/en.json");
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
                    <ModalTextButton buttonText={currentSelectedAddress?.name || ""} />
                    <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                        <BellIcon width={styles.bell.width} height={styles.bell.height} fill={"none"} />
                    </Pressable>
                    <ProfileImage />
                </View>
                <View style={styles.searchContainer}>
                    <SearchBar search={() => setFilter({})} onTextChange={setName} />
                    <Pressable style={styles.filterView} onPress={toggleFilter}>
                        <FilterIcon width={styles.filter.width} height={styles.filter.height} />
                    </Pressable>
                </View>
            </View>
        </View>
    );
}

import { StatusBar } from 'expo-status-bar';
import { View, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from './Pressable';
import { getStyles } from '../styles/TopBar';
import { useTheme } from '../styles/ThemeContext';
import { IFilterRequest } from '../declarations';

import FilterIcon from '@assets/icons/filter.svg';
import BellIcon from '@assets/icons/bell.svg';

interface TopBarProps {
    name?: string;
    toggleFilter: () => void;
    setFilter: (_filter: IFilterRequest) => void;
    setName: (partialName: string) => void;
}

export default function TopBar({ name = "Jane Doe", toggleFilter, setFilter, setName}: TopBarProps) {
    const styles = getStyles();
    const texts = require("../langs/en.json");
    const theme = useTheme();

    return (
        <View style={styles.container}>
            <StatusBar style={theme.colors.statusBarOnHome} />
            <View style={styles.elementsContainer}>
                <View style={styles.topElements}>
                    <Text style={styles.nameText}>{`${texts.hi}, ${name}`}</Text>
                    <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                        <BellIcon width={styles.bell.width} height={styles.bell.height} fill={"none"} />
                    </Pressable>
                    <ProfileImage />
                </View>
                <View style={styles.searchContainer}>
                    <SearchBar search={()=>setFilter({})} onTextChange={setName} />
                    <Pressable style={styles.filterView} onPress={toggleFilter}>
                        <FilterIcon width={styles.filter.width} height={styles.filter.height} />
                    </Pressable>
                </View>
            </View>
        </View>
    );
}

import { StatusBar } from 'expo-status-bar';
import { View, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from './Pressable';

import FilterIcon from '@assets/icons/filter.svg';
import BellIcon from '@assets/icons/bell.svg';

import { getStyles } from '../styles/TopBar';
import { useTheme } from '../styles/ThemeContext';

export default function TopBar({ name = "Jonh Doe", toggleFilter = () => { } }) {
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
                    <SearchBar onTextChange={(e: string) => { console.log(e) }} />
                    <Pressable style={styles.filterView} onPress={toggleFilter}>
                        <FilterIcon width={styles.filter.width} height={styles.filter.height} />
                    </Pressable>
                </View>
            </View>
        </View>
    );
}

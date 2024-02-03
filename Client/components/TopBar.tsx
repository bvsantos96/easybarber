import { StatusBar } from 'expo-status-bar';
import { View, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from  './Pressable';

import HamburgerIcon from '@assets/icons/hamburger.svg';
import FilterIcon from '@assets/icons/filter.svg';
import BellIcon from '@assets/icons/bell.svg';

import { styles } from '../styles/TopBar';
import { absoluteWidth, statusBarOnHome } from '../styles/Main';

export default function TopBar({ name = "Jonh Doe", toggleFilter = ()=>{} }) {
    const texts = require("../langs/en.json");

    return (
        <View style={styles.container}>
            <StatusBar style={statusBarOnHome} />
            <HamburgerIcon width={27 * absoluteWidth} height={27 * absoluteWidth} style={styles.hamburguer} />
            <Text style={styles.nameText}>{`${texts.hi}, ${name}`}</Text>
            <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                <BellIcon width={24 * absoluteWidth} height={24 * absoluteWidth} style={styles.bell} fill={"none"} />
            </Pressable>
            <ProfileImage />
            <SearchBar onTextChange={(e:string)=>{console.log(e)}}/>
            <Pressable style={styles.filterView} onPress={toggleFilter}>
                <FilterIcon width={31 * absoluteWidth} height={31 * absoluteWidth} style={styles.filter}/>
            </Pressable>
        </View>
    );
}

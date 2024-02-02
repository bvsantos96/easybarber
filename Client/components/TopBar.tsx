import { StatusBar } from 'expo-status-bar';
import { View, Image, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from  './Pressable';

import HamburgerIcon from '@assets/icons/hamburger.svg';
import FilterIcon from '@assets/icons/filter.svg';

import { styles } from '../styles/TopBar';
import { statusBarOnHome } from '../styles/Main';

export default function TopBar({ name = "Jonh Doe", toggleFilter = ()=>{} }) {
    const texts = require("../langs/en.json");

    return (
        <View style={styles.container}>
            <StatusBar style={statusBarOnHome} />
            <HamburgerIcon style={styles.hamburguer} />
            <Text style={styles.nameText}>{`${texts.hi}, ${name}`}</Text>
            <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                <Image source={require('../assets/icons/bell.png')} style={styles.bell} />
            </Pressable>
            <ProfileImage />
            <SearchBar onTextChange={(e:string)=>{console.log(e)}}/>
            <Pressable style={styles.filterView} onPress={toggleFilter}>
                <FilterIcon style={styles.filter}/>
            </Pressable>
        </View>
    );
}

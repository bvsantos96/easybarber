import { StatusBar } from 'expo-status-bar';
import { View, Image, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from  './Pressable';

import { styles } from '../styles/TopBar';
import { statusBarOnHome } from '../styles/Main';

export default function TopBar({ name = "Jonh Doe", toggleFilter = ()=>{} }) {
    const texts = require("../langs/en.json");

    return (
        <View style={styles.container}>
            <StatusBar style={statusBarOnHome} />
            <Image source={require('../assets/icons/hamburger.png')} style={styles.hamburguer} />
            <Text style={styles.nameText}>{`${texts.hi}, ${name}`}</Text>
            <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                <Image source={require('../assets/icons/bell.png')} style={styles.bell} />
            </Pressable>
            <ProfileImage />
            <SearchBar onTextChange={(e:string)=>{console.log(e)}}/>
            <Pressable style={styles.filterView} onPress={toggleFilter}>
                <Image source={require("../assets/icons/filter.png")} style={styles.filter}/>
            </Pressable>
        </View>
    );
}

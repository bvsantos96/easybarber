import { StatusBar } from 'expo-status-bar';
import { View, Image, Text } from 'react-native';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Pressable from  './Pressable';

import { styles } from '../styles/TopBar';
import { statusBarOnHome } from '../styles/Main';
import { NavigationProp } from '@react-navigation/native';
import Filter from '../screens/Filter';
import { useState } from 'react';

export default function TopBar({ navigation, name = "Jonh Doe" } : { navigation: NavigationProp<any, any>, name?: string }) {
    const texts = require("../langs/en.json");
    const [modalVisible, setModalVisible] = useState(false);

    return (
        <View style={styles.container}>
            <Filter modalVisible={modalVisible} setModalVisible={setModalVisible} />
            <StatusBar style={statusBarOnHome} />
            <Image source={require('../assets/icons/hamburger.png')} style={styles.hamburguer} />
            <Text style={styles.nameText}>{`${texts.hi}, ${name}`}</Text>
            <Pressable onPress={() => { alert("See notification") }} style={styles.bellContainer}>
                <Image source={require('../assets/icons/bell.png')} style={styles.bell} />
            </Pressable>
            <ProfileImage />
            <SearchBar onTextChange={(e:string)=>{console.log(e)}}/>
            <Pressable style={styles.filterView} onPress={()=>setModalVisible(!modalVisible)}>
                <Image source={require("../assets/icons/filter.png")} style={styles.filter}/>
            </Pressable>
        </View>
    );
}

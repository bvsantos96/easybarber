import { StatusBar } from 'expo-status-bar';
import { View, Image, Text, Pressable } from 'react-native';
import Divider from './Divider';
import ProfileImage from './ProfileImage';
import SearchBar from './SearchBar';
import Constants from 'expo-constants';

import { styles } from '../styles/TopBar';
import { mainColor, styles as mainStyles, statusBarOnHome } from '../styles/Main';

export default function TopBar({ name = "Jonh Doe" }) {
    const texts = require("../langs/en.json");
    return (
        <View style={styles.container}>
            <StatusBar style={statusBarOnHome} />
            <View>
                <Divider color={mainColor} height={Constants.statusBarHeight}/>
                <View style={[mainStyles.row, mainStyles.spaceBetween]}>
                    <View style={[mainStyles.row, mainStyles.w50c]} >
                        <Image source={require('../assets/icons/hamburger.png')} style={styles.icon} />
                        <Text style={[mainStyles.hPadding2, mainStyles.normalText, styles.textColor, mainStyles.hMargin5, mainStyles.fontSize19]}>{`${texts.hi}, ${name}`}</Text>
                    </View>
                    <View style={[mainStyles.row, mainStyles.alignCenter]}>
                        <Pressable onPress={() => { alert("See notification") }}>
                            <Image source={require('../assets/icons/bell.png')} style={[styles.icon, mainStyles.rMargin10]} />
                        </Pressable>
                        <ProfileImage />
                    </View>
                </View>
            </View>
            <View style={[mainStyles.row, mainStyles.spaceBetween]}>
                <SearchBar />
                <Pressable style={styles.filterView} onPress={()=>alert("Open filter")}>
                    <Image source={require("../assets/icons/filter.png")} style={styles.filter}/>
                </Pressable>
            </View>
        </View>
    );
}

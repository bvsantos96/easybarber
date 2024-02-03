import { View, ScrollView, StyleSheet } from 'react-native';
import Divider from '../components/Divider';

import { styles, mainColor, absoluteWidth, absoluteHeight, backgroundColor } from '../styles/Main';

import LogoSmall from "../assets/images/logo.svg";

export default function SignIn({ page = <></> }) {
    return (
        <ScrollView contentContainerStyle={[styles.container, styles.backgroundMainColor, styles.noOverflow]} keyboardShouldPersistTaps="handled">
            <Divider height={40} color={mainColor} />
            <View style={myStyles.logoContainer} >
                <LogoSmall width="95%" height="95%" />
            </View>
            <View style={[styles.loginContainer]}>
                <Divider color={mainColor} />
                {page}
                <Divider height={20} />
            </View>
        </ScrollView>
    );
}

const myStyles = StyleSheet.create({
    logoContainer: {
        width: 130 * absoluteWidth,
        height: 130 * absoluteHeight,
        backgroundColor: backgroundColor,
        borderRadius: 15,
        alignItems: 'center',
        justifyContent: 'center',
        shadowColor: '#000',
        shadowOffset: { width: 2, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 5,
        elevation: 5, 
    }
});


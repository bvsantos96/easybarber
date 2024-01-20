import { View, ScrollView, Image } from 'react-native';
import Divider from '../components/Divider';

import { styles, minDimention, mainColor } from '../styles/Main';

export default function SignIn({ page = <></> }) {
    return (
        <ScrollView contentContainerStyle={[styles.container, styles.backgroundMainColor, styles.noOverflow]} keyboardShouldPersistTaps="handled">
            <Image source={require("@assets/images/logoSmall.png")}
                style={{ width: minDimention * 0.5, height: minDimention * 0.5, flex: 1 }}
                resizeMode="contain"
            />
            <View style={[styles.loginContainer]}>
                <Divider color={mainColor} />
                {page}
                <Divider height={20} />
            </View>
        </ScrollView>
    );
}

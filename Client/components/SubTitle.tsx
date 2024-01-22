import { View, Text } from 'react-native';
import { styles } from '../styles/Title';

export default function SubTitle({ text = "" }) {
    return (
        <View style={styles.titleLine}>
            <Text style={styles.subtitle}>
                {text}
            </Text>
        </View>
    );
}

import { View, Text } from 'react-native';
import { getStyles } from '../styles/Title';

export default function SubTitle({ text = "" }) {
    const styles = getStyles();
    return (
        <View style={styles.titleLine}>
            <Text style={styles.subtitle}>
                {text}
            </Text>
        </View>
    );
}

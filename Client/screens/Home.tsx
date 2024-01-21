import { View } from 'react-native';
import TopBar from '../components/TopBar';

import { styles } from '../styles/Main';

export default function Home() {
    return (
        <View style={styles.container}>
            <TopBar />
        </View>
    );
}

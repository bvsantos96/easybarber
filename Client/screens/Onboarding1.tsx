import { View } from 'react-native';
import FirstLogin from '../components/Onboarding';

import { styles } from '../styles/Screens';
import { styles as mainStyle } from '../styles/Main';
import { PropNavigation } from '../App';
import FirstImage from '@assets/images/firstPage1.svg';
import { useTheme } from '../styles/ThemeContext';

export default function Onboarding1({ navigation }: PropNavigation) {
    const theme = useTheme();
    const texts = require("../langs/en.json");
    return (
        <View style={mainStyle.container}>
            <FirstLogin
                title={[
                    [{ text: texts.firstPage.title1, highlight: false }],
                    [{ text: texts.firstPage.title1_1, highlight: false }, { text: texts.firstPage.title1_2, highlight: true }]
                ]}
                subTitle={[
                    texts.firstPage.subTitle1,
                    texts.firstPage.subTitle1_1
                ]}
                image={<FirstImage style={styles.bigImage} />}
                pageSelect={[true, false]}
                button={{ title: texts.continue, func: () => navigation.navigate('Onboarding2') }}
            />
        </View>
    );
}

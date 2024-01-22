import { View, Image } from 'react-native';
import Onboarding from '../components/Onboarding';

import { styles } from '../styles/Screens';
import { styles as mainStyle } from '../styles/Main';
import { PropNavigation, resetNavigation } from '../App';

export default function Onboarding2({ navigation }: PropNavigation) {
    const texts = require("../langs/en.json");
    return (
        <View style={mainStyle.container}>
            <Onboarding
                title={[
                    [{ text: texts.firstPage.title2, highlight: false }],
                    [{ text: texts.firstPage.title2_1, highlight: true }, { text: texts.firstPage.title2_2, highlight: false }]
                ]}
                subTitle={[
                    texts.firstPage.subTitle2,
                    texts.firstPage.subTitle2_1
                ]}
                image={<Image style={styles.bigImage} source={require("@assets/images/firstPage2.png")} />}
                pageSelect={[false, true]}
                button={{ title: texts.getStarted, func: () => resetNavigation(navigation, 'AccountTypeSelection') }}
            />
        </View>
    );
}

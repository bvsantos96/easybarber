import OnBoarding from '../components/Onboarding';

import { getStyles } from '../styles/OnBoarding';
import { PropNavigation } from '../App';
import Image from '@assets/images/firstPage1.svg';

export default function Onboarding1({ navigation }: PropNavigation) {
    const styles = getStyles();
    const texts = require("../langs/en.json");
    return (
        <OnBoarding
            title={[
                [{ text: texts.firstPage.title1, highlight: false }],
                [{ text: texts.firstPage.title1_1, highlight: false }, { text: texts.firstPage.title1_2, highlight: true }]
            ]}
            subTitle={[
                texts.firstPage.subTitle1,
                texts.firstPage.subTitle1_1
            ]}
            image={<Image style={styles.image} />}
            pageSelect={[true, false]}
            button={{ title: texts.continue, func: () => navigation.navigate('Onboarding2') }}
        />
    );
}

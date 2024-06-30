import OnBoarding from '../components/Onboarding';

import { getStyles } from '../styles/OnBoarding';
import { PropNavigation, resetNavigation } from '../App';

import Image from '@assets/images/firstPage2.svg';
import React from 'react';

export default function Onboarding2({ navigation }: PropNavigation) {
    const styles = getStyles();
    const texts = require("../langs/en.json");
    return (
        <OnBoarding
            title={[
                [{ text: texts.firstPage.title2, highlight: false }],
                [{ text: texts.firstPage.title2_1, highlight: true }, { text: texts.firstPage.title2_2, highlight: false }]
            ]}
            subTitle={[
                texts.firstPage.subTitle2,
                texts.firstPage.subTitle2_1
            ]}
            image={<Image style={styles.image} />}
            pageSelect={[false, true]}
            button={{ title: texts.getStarted, func: () => resetNavigation(navigation, 'AccountTypeSelection') }}
        />
    );
}

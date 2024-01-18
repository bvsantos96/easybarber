import {useState} from 'react';
import {View, Image} from 'react-native';
import Title from '@components/Title';
import SubTitle from '@components/SubTitle';
import PageNumber from '@components/PageNumber';
import Button from '@components/Button';
import Divider from '@components/Divider';

import {styles} from '@styles/screens';
import {styles as mainStyle} from '@styles/main';

export default function FirstLogin() {
  const texts = require("@lang/en.json");
  return (
    <View style={mainStyle.container}>
      <View>
        <Divider height={60}/>
        <Title line={[{text: texts.firstPage.title1, highlight: false}]} />
        <Title line={[{text: texts.firstPage.title1_1, highlight: false},{text: texts.firstPage.title1_2, highlight: true}]} />
        <Divider height={20}/>
        <SubTitle text={texts.firstPage.subTitle1} />
        <SubTitle text={texts.firstPage.subTitle1_1} />
      </View>
      <View style={styles.imageContainer}>
        <View style={styles.roundBackground}/>
        <Image style={styles.bigImage} source={require("@assets/images/firstPage1.png")}/>
      </View>
      <View style={mainStyle.w100}>
        <View style={styles.pageSelectionContainer}>
          <PageNumber selected={true} />
          <PageNumber selected={false} />
        </View>
        <Button title={texts.continue}/>
        <Divider height={50}/>
      </View>
    </View>
  );
}

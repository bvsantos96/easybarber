import {useState} from 'react';
import {View} from 'react-native';
import Title from '@components/Title';
import SubTitle from '@components/SubTitle';
import PageNumber from '@components/PageNumber';
import Button from '@components/Button';
import Divider from '@components/Divider';

import {styles} from '@styles/screens';
import {styles as mainStyle} from '@styles/main';

export default function FirstLogin({
    title = [], 
    subTitle = [], 
    image = ()=>undefined, 
    pageSelect= [], 
    button = {title: undefined, func: undefined}
  }) {
  const texts = require("@lang/en.json");
  return (
    <>
      <View>
        <Divider height={60}/>
        {title.map((item, i) => (
          <Title key={i} line={item} />
        ))}
        <Divider height={20}/>
        {subTitle.map((item, i) => (
          <SubTitle key={i} text={item} />
        ))}
      </View>
      <View style={styles.imageContainer}>
        <View style={styles.roundBackground} />
        {image}
      </View>
      <View style={mainStyle.w100c}>
        <View style={styles.pageSelectionContainer}>
          {pageSelect.map((item, i)=>(
              <PageNumber key={i} selected={item} />
          ))}
        </View>
        <Button title={button.title} onPress={button.func} />
        <Divider height={50}/>
      </View>
    </>
  );
}

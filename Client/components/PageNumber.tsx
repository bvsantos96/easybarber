import {useState} from 'react';
import {Image} from 'react-native';
import {styles as inputStyle} from '@styles/input';
import {styles as mainStyle} from '@styles/main';

export default function PageNumber({selected = false}) {
  return (
    selected ? (
      <Image style={[inputStyle.icon, mainStyle.hMargin3]} source={require('@assets/icons/activePage.png')}/>
    ) : (
      <Image style={[inputStyle.icon, mainStyle.hMargin3]} source={require('@assets/icons/inactivePage.png')}/>
    )
  );
}

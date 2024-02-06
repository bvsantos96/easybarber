import React from 'react';
import { Text } from 'react-native';
import Pressable from '../components/Pressable';
import { getStyles } from '../styles/Button';
import { mainColor } from '../styles/Main';

export default function Button({backgroundColor=mainColor, buttonTextColor='white', borderColor=mainColor , stylesInput={}, onPress = ()=>{alert("onPress button function not passed in props")}, title = "No button tittle"}) {
  const styles = getStyles();
  return (
    <Pressable style={[styles.button, {backgroundColor:backgroundColor, borderWidth:1, borderColor:borderColor}, stylesInput]} onPress={onPress}>
      <Text style={[styles.textButton, {color:buttonTextColor}]}>{title}</Text>
    </Pressable>
  );
}

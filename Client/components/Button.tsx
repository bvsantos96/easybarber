import React from 'react';
import { Text } from 'react-native';
import Pressable from '../components/Pressable';
import { styles } from '../styles/Button';
import { mainColor } from '../styles/Main';

export default function Button({backgroundColor=mainColor, buttonTextColor='white', borderColor=mainColor ,onPress = ()=>{alert("onPress button function not passed in props")}, title = "No button tittle"}) {
  return (
    <Pressable style={[styles.button, {backgroundColor:backgroundColor, borderWidth:1, borderColor:borderColor}]} onPress={onPress}>
      <Text style={[styles.textButton, {color:buttonTextColor}]}>{title}</Text>
    </Pressable>
  );
}

export default function Button({ onPress = () => { alert("onPress button function not passed in props") }, title = "No button tittle" }: ButtonProps) {
    return (
        <Pressable style={styles.button} onPress={onPress}>
            <Text style={styles.textButton}>{title}</Text>
        </Pressable>
    );
}

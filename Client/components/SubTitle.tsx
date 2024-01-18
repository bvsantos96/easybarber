import {useState} from 'react';
import {View, Text} from 'react-native';
import {styles} from '@styles/title';

export default function SubTitle({text = ""}) {
  return (
   <View style={styles.titleLine}>
        <Text style={styles.subtitle}>
          {text}
        </Text>
   </View>
  );
}

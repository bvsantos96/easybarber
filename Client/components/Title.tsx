import React from 'react';
import { Text, View } from 'react-native';
import { styles } from '@styles/title';

export default function Title({ line = [] }) {
  return (
    <View style={styles.titleLine}>
      {line.map((item, i) => (
        <Text key={i} style={item.highlight ? styles.hText : styles.text}>
          {item.text}
        </Text>
      ))}
    </View>
  );
}

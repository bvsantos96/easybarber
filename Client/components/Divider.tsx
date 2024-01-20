import React from 'react';
import { View, StyleSheet } from 'react-native';
import {backgroundColor} from '../styles/main';

const Divider = ({ height = 10, color = backgroundColor }) => {
  return <View style={[styles.divider, { height: height || 10, backgroundColor: color || backgroundColor }]} />;
};

const styles = StyleSheet.create({
  divider: {
    width: 1,
    alignSelf: 'stretch',
    backgroundColor: backgroundColor,
    color: backgroundColor
  },
});

export default Divider;

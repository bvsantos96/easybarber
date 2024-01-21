import React from 'react';
import { View, StyleSheet } from 'react-native';
import { backgroundColor } from '../styles/Main';

type DividerProps = {
    height?: number,
    color?: string
}

const Divider = ({ height = 10, color = backgroundColor }: DividerProps) => {
    return <View style={[styles.divider, { height: height, backgroundColor: color }]} />;
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

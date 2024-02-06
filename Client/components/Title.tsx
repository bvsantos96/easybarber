import React from 'react';
import { Text, View } from 'react-native';
import { getStyles } from '../styles/Title';

export type Line = {
    text: string,
    highlight: boolean
}

export type Title = {
    line: Line[]
}

export default function Title({ line = [] }: Title) {
    const styles = getStyles();
    return (
        <View style={styles.titleLine}>
            {line.map((item: Line, i: number) => (
                <Text key={i} style={item.highlight ? styles.hText : styles.text}>
                    {item.text}
                </Text>
            ))}
        </View>
    );
}


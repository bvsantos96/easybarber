import React from 'react';
import { Pressable, Text } from 'react-native';
import { styles } from '../styles/Button';

type ButtonProps = {
    title: string,
    onPress: () => void
}

export default function Button({ onPress = () => { alert("onPress button function not passed in props") }, title = "No button tittle" }: ButtonProps) {
    return (
        <Pressable style={styles.button} onPress={onPress}>
            <Text style={styles.textButton}>{title}</Text>
        </Pressable>
    );
}

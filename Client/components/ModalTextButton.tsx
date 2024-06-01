import React from 'react';
import Pressable from './Pressable';
import { Text } from 'react-native';
import { AntDesign } from '@expo/vector-icons';

export default function ModalTextButton({ buttonText = "Open modal" }) {
    const styles = require('../styles/TopBar').getStyles();
    return (
        <Pressable onPress={() => { alert("Open modal") }} style={{ flexDirection: 'row' }}>
            <Text style={styles.nameText}>
                {buttonText}
                <AntDesign name="down" size={styles.nameText.lineHeight} color={styles.nameText.color} />
            </Text>
        </Pressable>
    );
}

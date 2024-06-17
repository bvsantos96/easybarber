import React from 'react';
import { Text } from 'react-native';
import { AntDesign } from '@expo/vector-icons';

export default function ModalTextButton({ buttonText = "Open modal" }) {
    const styles = require('../styles/TopBar').getStyles();
    return (
        <Text style={styles.nameText}>
            {buttonText}
            <AntDesign name="down" size={styles.nameText.lineHeight} color={styles.nameText.color} />
        </Text>
    );
}

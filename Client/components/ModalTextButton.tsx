import React from 'react';
import { Text, View } from 'react-native';
import { AntDesign } from '@expo/vector-icons';

export default function ModalTextButton({ buttonText = "", style = {} }) {
    const styles = require('../styles/TopBar').getStyles();
    return (
        <View style={styles.flexDirection}>
            <Text style={[styles.nameText, style]} numberOfLines={1} ellipsizeMode='tail' >
                {buttonText}
            </Text>
            <AntDesign name="down" size={styles.nameText.lineHeight} color={styles.nameText.color} />
        </View>
    );
}

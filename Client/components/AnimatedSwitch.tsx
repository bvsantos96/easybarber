import React, { useEffect, useRef } from 'react';
import { Animated, View, Text, Pressable } from 'react-native';
import { getStyles } from '../styles/AnimatedSwitch';

export default function AnimatedSwitch({ text1, text2, setSelected }: { text1: string, text2: string, setSelected: (selected: boolean) => void }) {
    const maxVal = 150;
    const position = useRef(new Animated.Value(0)).current;
    const [value, setValue] = React.useState(maxVal);
    const styles = getStyles();
    const [selected, setSelectedState] = React.useState(true);

    const handleSelection = () => {
        moveView();
    }

    const moveView = () => {
        Animated.timing(position, {
            toValue: value,
            duration: 500,
            useNativeDriver: true,
        }).start(() => {
            if (value === 0) {
                setSelected(true);
            } else {
                setSelected(false);
            }
        });

        if (value === 0) {
            setSelectedState(true);
            setValue(maxVal);
        } else {
            setSelectedState(false);
            setValue(0);
        }
    };

    return (
        <Pressable onPress={handleSelection} style={styles.container}>
            <View style={styles.box} />
            <Animated.View style={[styles.movingBox, { transform: [{ translateX: position }] }]} />
            <View style={styles.textContainer}>
                <View style={styles.textLeftContainer}>
                    <Text style={selected ? styles.textSelected : styles.textUnselected}>{text1}</Text>
                </View>
                <View style={styles.textRightContainer}>
                    <Text style={selected ? styles.textUnselected : styles.textSelected}>{text2}</Text>
                </View>
            </View>
        </Pressable>
    );
};

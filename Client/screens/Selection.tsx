import Button from "@components/Button";
import React from "react";
import { View, Text } from "react-native";
import { getStyles } from "@styles/Selection";

type Props = {
    onButtonPress: () => void;
    children: React.ReactNode;
    selected: boolean;
    buttonText: string;
    selectionText: string;
}

export default function Selection({ onButtonPress, children, selected, selectionText, buttonText }: Props) {
    const styles = getStyles();

    return (
        <View style={styles.container} >
            <Text style={styles.selectTextContainer}>{selectionText}</Text>
            {children}
            <View style={styles.button}>
                <Button
                    disabled={!selected}
                    stylesInput={{ width: '100%' }}
                    onPress={onButtonPress} title={buttonText} />
            </View>
        </View>
    );
}

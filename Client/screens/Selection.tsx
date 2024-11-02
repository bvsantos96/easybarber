import Button from "@components/Button";
import React from "react";
import { View, Text } from "react-native";
import { getStyles } from "@styles/Selection";
import Divider from "@components/Divider";

type Props = {
    onButtonPress: () => void;
    children: React.ReactNode;
    selected: boolean;
    buttonText: string;
    selectionText: string;
    setTopPadding?: (lineCount: number) => void;
}

export default function Selection({ onButtonPress, children, selected, selectionText, buttonText, setTopPadding: setSelectionTextNLines }: Props) {
    const styles = getStyles();
    const topPadding = 30;

    const handleTextLayout = (e: any) => {
        const { lines } = e.nativeEvent;
        setSelectionTextNLines && setSelectionTextNLines((lines.length * topPadding - 10) + 10);
    };

    return (
        <View style={styles.container} >
            <Divider size={topPadding} />
            <Text onTextLayout={handleTextLayout} style={styles.selectTextContainer}>{selectionText}</Text>
            {children}
            <View style={styles.button}>
                <Button
                    preventMultiplePress
                    disabled={!selected}
                    stylesInput={{ width: '100%' }}
                    onPress={onButtonPress} title={buttonText} />
            </View>
        </View>
    );
}

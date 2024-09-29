import React, { useState } from "react";
import { View, Text, ViewStyle } from "react-native";
import { EvilIcons } from '@expo/vector-icons';

import { useTheme } from "../styles/ThemeContext";
import Pressable from "./Pressable";
import { getStyles } from '../styles/LocationModal';
import useLocationStore from "../storage/stores/LocationStore";

const LocationItem = ({ idx, location, reset, highlightFirst, style }: { style?: ViewStyle, idx: Number | string, location: ILocation, reset: () => void, highlightFirst?: boolean }) => {
    const theme = useTheme();
    const styles = getStyles();
    const [numLines, setNumLines] = useState(1);

    const {
        selectLocation
    } = useLocationStore();

    const handleAddressLayout = (event: any) => {
        let nLines = Math.floor(event.nativeEvent.layout.height / styles.itemTitle.fontSize);
        setNumLines(nLines);
    }

    const handleSelectNewLocation = async () => {
        reset();
        await selectLocation(location);
    }

    return (
        <Pressable
            key={+idx}
            style={[styles.itemContainer, highlightFirst && idx === 0 && styles.selectedItem, { ...style }]}
            onPress={handleSelectNewLocation} >
            <View style={[styles.horizontalPadding, styles.maxHeight, styles.maxWidth, styles.rowContainer]}>
                <View style={[styles.itemIconContainer, { 'marginRight': styles.itemIconPadding.padding }]}>
                    <EvilIcons name="location" size={styles.itemIcon.width} color={theme.colors.text.main} />
                </View>
                <View style={styles.itemTextContainer}>
                    <Text
                        style={styles.itemTitle}
                        onLayout={handleAddressLayout}
                        numberOfLines={2}
                        ellipsizeMode='tail'>
                        {location.name ? location.name : location.address}
                    </Text>
                    {
                        numLines > 1 ?
                            <Text
                                style={styles.itemSubtitle}
                                numberOfLines={2}
                                ellipsizeMode='tail'>
                                {`${location.city} - ${location.country}`}
                            </Text>
                            :
                            <>
                                <Text style={styles.itemSubtitle} >{location.city}</Text>
                                <Text style={styles.itemSubtitle}>{location.country}</Text>
                            </>
                    }
                </View>
                {
                    // <View style={[styles.itemIconContainer]}>
                    //     <Feather name="edit-2" size={styles.itemIcon.width * 0.7} color={theme.colors.text.main} />
                    // </View>
                    // 
                }
            </View>
        </Pressable>
    );
}

export default LocationItem;

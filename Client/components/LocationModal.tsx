import React, { useEffect, useRef, useState } from 'react';
import { View, Text } from 'react-native';
import { ILocation } from '../declarations';
import SearchBar from './SearchBar';
import { useTheme } from '../styles/ThemeContext';
import { getStyles } from '../styles/LocationModal';
import Divider from './Divider';
import { EvilIcons } from '@expo/vector-icons';
import { Feather } from '@expo/vector-icons';
import PageList, { PageListRef } from './PageList';
import { getLocationsRequest } from '../utils/ApiRequest';

export default function LocationModal() {
    const styles = getStyles();
    const texts = require("@lang/en.json");
    const theme = useTheme();
    const pageListRef = useRef<PageListRef<ILocation>>(null);
    const [topHeight, setTopHeight] = useState(0);
    const [height, setHeight] = useState(0);
    const [numItems, setNumItems] = useState(0);
    useEffect(() => {
        if (numItems === 0) {
            return;
        }
    }, [numItems]);

    const handleLayout = (event: any) => {
        setHeight(event.nativeEvent.layout.height);
        if (topHeight > 0) {
            setNumItems(Math.floor((event.nativeEvent.layout.height - topHeight) / styles.itemContainer.height) - 2);
        }
    }

    const handleTopLayout = (event: any) => {
        setTopHeight(event.nativeEvent.layout.height);
        if (height > 0) {
            setNumItems(Math.floor((height - topHeight) / styles.itemContainer.height) - 2);
        }
    }

    return (
        <View style={styles.container} onLayout={handleLayout}>
            <View onLayout={handleTopLayout} style={[styles.paddingHorizontal, styles.centerHorizontal, styles.maxWidth]}>
                <SearchBar placeholder={texts.searchAddress} altColor backgroundColor={theme.colors.backgroundColor} borderColor={theme.colors.text.main} />
                <Divider size={styles.divider.maxHeight} />
                <View style={styles.titleContainer}>
                    <Text style={styles.title}>{texts.recentAddresses}</Text>
                </View>
                <Divider size={styles.divider.minHeight} />
            </View>
            <PageList<ILocation> ref={pageListRef} renderItem={({ item }) => <Item idx={item.id} location={item} />} requestFunction={getLocationsRequest} />
        </View>
    );
}

const Item = ({ idx, location }: { idx: Number | string, location: ILocation }) => {
    const theme = useTheme();
    const styles = getStyles();
    const [numLines, setNumLines] = useState(1);

    const handleAddressLayout = (event: any) => {
        let nLines = Math.floor(event.nativeEvent.layout.height / styles.itemTitle.fontSize);
        setNumLines(nLines);
    }

    return (
        <View style={[styles.itemContainer, idx === 0 && styles.selectedItem]}>
            <View style={[styles.paddingHorizontal, styles.maxHeight, styles.maxWidth, styles.rowContainer]}>
                <View style={[styles.itemIconContainer, { 'marginRight': styles.itemIconPadding.padding }]}>
                    <EvilIcons name="location" size={styles.itemIcon.width} color={theme.colors.text.main} />
                </View>
                <View style={styles.itemTextContainer}>
                    <Text style={styles.itemTitle} onLayout={handleAddressLayout} numberOfLines={2} ellipsizeMode='tail'>{location.name ? location.name : location.address}</Text>
                    {
                        numLines > 1 ?
                            <Text style={styles.itemSubtitle} numberOfLines={2} ellipsizeMode='tail'>{`${location.city} - ${location.country}`}</Text>
                            :
                            <>
                                <Text style={styles.itemSubtitle} >{location.city}</Text>
                                <Text style={styles.itemSubtitle}>{location.country}</Text>
                            </>
                    }
                </View>
                <View style={[styles.itemIconContainer]}>
                    <Feather name="edit-2" size={styles.itemIcon.width * 0.7} color={theme.colors.text.main} />
                </View>
            </View>
        </View>
    );
}

import React, { useEffect, useRef, useState } from 'react';
import { View, Text } from 'react-native';
import { ILocation } from '../declarations';
import SearchBar from './SearchBar';
import { useTheme } from '../styles/ThemeContext';
import { getStyles } from '../styles/LocationModal';
import Divider from './Divider';
import { EvilIcons } from '@expo/vector-icons';
import PageList, { PageListRef } from './PageList';
import { getLocationList } from '../utils/ApiRequest';
import Pressable from './Pressable';
import useLocationStore from '../storage/stores/LocationStore';

interface LocationModalProps {
    toggleModal: () => void;
}

const LocationModal: React.FC<LocationModalProps> = ({ toggleModal }) => {
    const styles = getStyles();
    const texts = require("@lang/en.json");
    const theme = useTheme();
    const pageListRef = useRef<PageListRef<ILocation>>(null);
    const [topHeight, setTopHeight] = useState(0);
    const [height, setHeight] = useState(0);
    const [numItems, setNumItems] = useState(0);
    const [resetList, setResetList] = useState(false);
    const [addresses, setAddresses] = useState<ILocation[]>([]);

    const {
        locations,
        setLocations
    } = useLocationStore();

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

    const searchAddress = async (address: string) => {
    }

    return (
        <View style={styles.container} onLayout={handleLayout}>
            <View onLayout={handleTopLayout} style={[styles.paddingHorizontal, styles.centerHorizontal, styles.maxWidth]}>
                <SearchBar onTextChange={searchAddress} placeholder={texts.searchAddress} altColor backgroundColor={theme.colors.backgroundColor} borderColor={theme.colors.text.main} />
                <Divider size={styles.divider.maxHeight} />
                <View style={styles.titleContainer}>
                    <Text style={styles.title}>{texts.recentAddresses}</Text>
                </View>
                <Divider size={styles.divider.minHeight} />
            </View>
            <PageList<ILocation> saveCache={setLocations} loadCache={() => locations} reset={resetList} inModal ref={pageListRef} renderItem={({ item, index }) => <Item reset={() => { setResetList(!resetList); toggleModal() }} key={index} idx={index} location={item} />} requestFunction={getLocationList} />
        </View>
    );
}

const Item = ({ idx, location, reset }: { idx: Number | string, location: ILocation, reset: () => void }) => {
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
        selectLocation(location);
        reset();
    }

    return (
        <Pressable key={+idx} style={[styles.itemContainer, idx === 0 && styles.selectedItem]} onPress={handleSelectNewLocation} >
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
export default LocationModal;

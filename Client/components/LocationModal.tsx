import React, { useState } from 'react';
import { View, Text } from 'react-native';
import { ILocation } from '../declarations';
import SearchBar from './SearchBar';
import { useTheme } from '../styles/ThemeContext';
import { getStyles } from '../styles/LocationModal';
import Divider from './Divider';
import PageList from './PageList';
import LocationItem from './LocationItem';
import { getLocationList } from '../utils/ApiRequest';
import useLocationStore from '../storage/stores/LocationStore';
import { fetchSuggestions, suggestionsInputValidation } from '../utils/Location';
import { debounce } from 'lodash';
import { PageListType } from '../enums';

interface LocationModalProps {
    toggleModal: () => void;
}

const LocationModal: React.FC<LocationModalProps> = ({ toggleModal }) => {
    const styles = getStyles();
    const texts = require("@lang/en.json");
    const theme = useTheme();
    const [resetList, setResetList] = useState(false);
    const [addresses, setAddresses] = useState<ILocation[]>([]);

    const {
        locations,
        setLocations
    } = useLocationStore();

    const searchAddress = async (address: string) => {
        if (!suggestionsInputValidation(address)) {
            setAddresses([]);
            return;
        }
        const _addresses = await fetchSuggestions(address);
        setAddresses(_addresses);
    }

    const debounceSearchAddress = debounce(searchAddress, 300);

    return (
        <View style={styles.container} >
            <SearchBar<ILocation>
                style={styles.horizontalPadding}
                options={addresses}
                inModal
                renderOption={
                    ({ item, index }) => {
                        return (
                            <LocationItem
                                reset={() => { setResetList(!resetList); setAddresses([]); toggleModal() }}
                                key={index}
                                idx={index}
                                location={item} />
                        );
                    }
                }
                onTextChange={debounceSearchAddress}
                placeholder={texts.searchAddress}
                altColor
                backgroundColor={theme.colors.backgroundColor}
                borderColor={theme.colors.text.main} />
            <Divider size={styles.divider.maxHeight} />
            <View style={[styles.horizontalPadding, styles.titleContainer]}>
                <Text style={styles.title}>{texts.recentAddresses}</Text>
            </View>
            <Divider size={styles.divider.minHeight} />
            <PageList<ILocation>
                saveCache={setLocations}
                loadCache={() => locations}
                reset={resetList}
                type={PageListType.BOTTOM_SHEET}
                renderItem={
                    ({ item, index }) =>
                        <LocationItem
                            highlightFirst
                            reset={() => { setResetList(!resetList); toggleModal() }}
                            key={index}
                            idx={index}
                            location={item} />
                }
                requestFunction={getLocationList} />
        </View>
    );
}

export default LocationModal;

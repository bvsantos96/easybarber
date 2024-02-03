import React from 'react';
import { View, TextInput } from 'react-native';
import Pressable from '../components/Pressable';

import SearchIcon from "@assets/icons/search.svg";

import { styles } from '../styles/TopBar';
import { absoluteWidth, buttonTextColor } from '../styles/Main';

export default function SearchBar({ onTextChange = (e: string) => { alert(`No function call to search ${e}`) } }) {
    const texts = require('../langs/en.json');
    const textInputRef = React.useRef<TextInput>(null);

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    return (
        <Pressable style={styles.searchBarInput} onPress={handleViewPress}>
            <View style={styles.iconView}>
                <SearchIcon style={styles.icon} width={25 * absoluteWidth} height={25 * absoluteWidth} />
            </View>
            <TextInput
                ref={textInputRef}
                style={[styles.textInput, styles.textColor]}
                placeholder={texts.search}
                placeholderTextColor={buttonTextColor}
                onChangeText={onTextChange}
            />
        </Pressable>
    );
}


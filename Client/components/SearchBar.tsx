import React from 'react';
import { TextInput } from 'react-native';
import Pressable from '../components/Pressable';

import SearchIcon from "@assets/icons/search.svg";

import { getStyles } from '../styles/TopBar';

export default function SearchBar({ search = () => { }, onTextChange = (e: string) => { alert(`No function call to search ${e}`) } }) {
    const styles = getStyles();
    const texts = require('../langs/en.json');
    const textInputRef = React.useRef<TextInput>(null);

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    return (
        <Pressable style={styles.searchBarInput} onPress={handleViewPress}>
            <Pressable style={styles.iconView} onPress={search} >
                <SearchIcon style={styles.icon} width={styles.icon.width} height={styles.icon.height} />
            </Pressable>
            <TextInput
                ref={textInputRef}
                style={[styles.textInput, styles.textColor]}
                placeholder={texts.search}
                placeholderTextColor={styles.textColor.color}
                onChangeText={onTextChange}
                onSubmitEditing={search}
                returnKeyType="done"
            />
        </Pressable>
    );
}


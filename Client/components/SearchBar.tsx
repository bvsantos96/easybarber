import React from 'react';
import { View, Image, TextInput, Pressable } from 'react-native';

import { styles } from '../styles/TopBar';
import { buttonTextColor } from '../styles/Main';

export default function SearchBar({ onTextChange = (e: string) => { alert(`No function call to search ${e}`) } }) {
    const texts = require('../langs/en.json');
    const textInputRef = React.useRef<TextInput>(null);

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    return (
        <Pressable style={[styles.searchBarInput, styles.inputWidth]} onPress={handleViewPress}>
            <View style={[styles.iconView, styles.iconView]}>
                <Image source={require("../assets/icons/search.png")} style={styles.icon} />
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


import React from 'react';
import { TextInput } from 'react-native';
import Pressable from '../components/Pressable';

import SearchIcon from "@assets/icons/search.svg";
import SearchIconAlt from "@assets/icons/searchMainColor.svg";

import { getStyles } from '../styles/TopBar';

interface SearchBarProps {
    search?: () => void;
    onTextChange?: (e: string) => void;
    onFocus?: () => void;
    backgroundColor?: string;
    borderColor?: string;
    altColor?: boolean;
    placeholder?: string;
}

export default function SearchBar({
    search = () => { },
    onTextChange = (e: string) => { alert(`No function call to search ${e}`) },
    onFocus = () => { },
    backgroundColor = undefined,
    borderColor = undefined,
    altColor = false,
    placeholder = ""
}: SearchBarProps) {
    const styles = getStyles();
    const textInputRef = React.useRef<TextInput>(null);
    borderColor = borderColor || styles.searchBarInput.borderColor;

    const handleViewPress = () => {
        textInputRef.current?.focus();
    };

    return (
        <Pressable style={[styles.searchBarInput, borderColor ? { borderColor: borderColor } : {}]} onPress={handleViewPress}>
            <Pressable style={[styles.iconView, backgroundColor ? { backgroundColor: backgroundColor } : {}]} onPress={search} >
                {altColor ? (
                    <SearchIconAlt style={styles.icon} width={styles.icon.width} height={styles.icon.height} />
                ) : (
                    <SearchIcon style={styles.icon} width={styles.icon.width} height={styles.icon.height} />
                )}
            </Pressable>
            <TextInput
                onFocus={onFocus}
                ref={textInputRef}
                style={[styles.textInput, styles.textColor, { color: borderColor }]}
                placeholder={placeholder}
                placeholderTextColor={borderColor}
                onChangeText={onTextChange}
                onSubmitEditing={search}
                returnKeyType="done"
            />
        </Pressable>
    );
}


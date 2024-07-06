import React, { useEffect } from 'react';
import { TextInput, View, FlatList, ViewStyle } from 'react-native';
import Pressable from '../components/Pressable';

import SearchIcon from "@assets/icons/search.svg";
import SearchIconAlt from "@assets/icons/searchMainColor.svg";

import { getStyles } from '../styles/TopBar';
import { Identifiable } from '../declarations';
import { BottomSheetFlatList } from '@gorhom/bottom-sheet';
import { useTheme } from '../styles/ThemeContext';

interface SearchBarProps<T extends Identifiable> {
    search?: () => void;
    onTextChange?: (e: string) => void;
    onFocus?: () => void;
    backgroundColor?: string;
    borderColor?: string;
    altColor?: boolean;
    placeholder?: string;
    options?: T[]
    renderOption?: ({ item, index }: { item: T, index: number }) => JSX.Element;
    inModal?: boolean;
    style?: ViewStyle;
}

export default function SearchBar<T extends Identifiable>({
    search = () => { },
    onTextChange = (e: string) => { alert(`No function call to search ${e}`) },
    onFocus = () => { },
    backgroundColor = undefined,
    borderColor = undefined,
    altColor = false,
    placeholder = "",
    options = [],
    renderOption,
    inModal,
    style
}: SearchBarProps<T>) {
    const styles = getStyles();
    const textInputRef = React.useRef<TextInput>(null);
    const [height, setHeight] = React.useState(0);
    const theme = useTheme();
    const handleViewPress = () => {
        textInputRef.current?.focus();
    };
    const searchBarHeight = 50 * theme.dimensions.absoluteHeight;
    borderColor = borderColor || styles.searchBarInput.borderColor;

    useEffect(() => {
        setHeight(searchBarHeight + 70 * options.length * theme.dimensions.absoluteHeight);
    }, [options]);

    return (
        <View style={[styles.searchBarContainerExpanded, { minHeight: height }, { ...style }]}>
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
            {options.length > 0 && renderOption && (
                inModal ? (
                    <BottomSheetFlatList
                        style={styles.optionsList}
                        data={options}
                        renderItem={renderOption as any}
                        keyExtractor={(item) => `${item.id}`}
                    />
                ) : (
                    <FlatList
                        style={styles.optionsList}
                        data={options}
                        renderItem={renderOption as any}
                        keyExtractor={(item) => `${item.id}`}
                    />
                )
            )}
        </View >
    );
}


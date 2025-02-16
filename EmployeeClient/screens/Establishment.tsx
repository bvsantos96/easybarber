import React, { useState } from 'react';
import { Text, View, TouchableOpacity } from 'react-native';
import { Params } from "@navigation/Router";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { getStyles } from '@styles/Establishment';
import { Image } from 'expo-image';
import * as ImagePicker from 'expo-image-picker';
import Input from '@components/Input';
import Button from '@components/Button';
import { Ionicons } from '@expo/vector-icons';

type Props = NativeStackScreenProps<typeof Params, 'Establishment'>;

export default function Establishment({ route, navigation }: Props) {
    const _establishment: EstablishmentInfo | undefined = route.params;
    const styles = getStyles();
    const isNew = !_establishment;

    const [images, setImages] = useState<string[]>(_establishment?.images?.map(img => img.data) || []);

    const openNativeImagePicker = async () => {
        try {
            const result = await ImagePicker.launchImageLibraryAsync({
                mediaTypes: ImagePicker.MediaTypeOptions.Images,
                allowsEditing: true,
                quality: 1,
                base64: true
            });

            if (!result.canceled) {
                setImages([result.assets[0].uri]);
            }
        } catch (error) {
            console.error('Image picker error:', error);
        }
    };

    const ImageUploadPlaceholder = () => (
        <TouchableOpacity
            style={styles.uploadContainer}
            onPress={openNativeImagePicker}
        >
            <Ionicons name="add" size={114} style={styles.uploadIcon} />
        </TouchableOpacity>
    );

    return (
        <View style={styles.container}>
            {images.length > 0 ? (
                <Image
                    source={{ uri: images[0] }}
                    style={styles.imageStyle}
                />
            ) : (
                <ImageUploadPlaceholder />
            )}

            <Text style={styles.nameText}>Name</Text>
            <View style={styles.nameInput}>
                <Input
                    type="text"
                    placeholder={_establishment?.name || 'Enter establishment name'}
                />
            </View>

            <Text style={styles.phoneText}>Phone</Text>
            <View style={styles.phoneInput}>
                <Input
                    type="text"
                    placeholder={'Enter phone number'}
                />
            </View>

            <Text style={styles.addressText}>Address</Text>
            <View style={styles.addressInput}>
                <Input
                    type="text"
                    placeholder={_establishment?.address || 'Enter address'}
                />
            </View>

            <View style={styles.buttonContainer}>
                <Button
                    title={isNew ? "Create" : "Save"}
                    onPress={() => {
                        // TODO: Implement save/create logic
                        console.log('Save/Create pressed');
                    }}
                />
            </View>
        </View>
    );
}

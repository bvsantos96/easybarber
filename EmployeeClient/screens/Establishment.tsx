import Button from '@components/Button';
import Input from '@components/Input';
import { Ionicons } from '@expo/vector-icons';
import texts from '@lang/en.json';
import { Params } from "@navigation/Router";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { getStyles } from '@styles/Establishment';
import { Image } from 'expo-image';
import * as ImagePicker from 'expo-image-picker';
import React, { useRef, useState } from 'react';
import { Text, TouchableOpacity, View } from 'react-native';

export type Route = {
    Establishment?: EstablishmentInfo;
}

type Props = NativeStackScreenProps<typeof Params, 'Establishment'>;

export default function Establishment({ route, navigation }: Props) {
    const styles = getStyles();
    let _establishment: EstablishmentInfo | undefined = useRef<EstablishmentInfo | undefined>(undefined).current;
    if (route.params) {
        const { Establishment: establishment } = route.params;
        _establishment = establishment;
    } else {
        _establishment = {
            id: 0,
            name: "",
            description: "",
            address: "",
            admin: true,
            images: [],
            latitude: 0,
            longitude: 0,
            nvotes: 0,
            sumVotes: 0,
        } as EstablishmentInfo;
    }
    const isNew = useRef<boolean>(!_establishment);
    const [images, setImages] = useState<IImage[] | undefined>(_establishment?.images);

    const openNativeImagePicker = async () => {
        try {
            const result = await ImagePicker.launchImageLibraryAsync({
                mediaTypes: ImagePicker.MediaTypeOptions.Images,
                allowsEditing: true,
                quality: 1,
                base64: true
            });

            if (!result.canceled) {
                setImages([{ data: result.assets[0].uri, id: 0, isMain: true }]);
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
            {(images && images?.length > 0) ? (
                <Image
                    source={{ uri: _establishment?.images[0].data }}
                    style={styles.imageStyle}
                />
            ) : (
                <ImageUploadPlaceholder />
            )}

            <Text style={styles.nameText}>{texts.name}</Text>
            <View style={styles.nameInput}>
                <Input
                    disabled={_establishment ? !_establishment.admin : false}
                    type="text"
                    placeholder={_establishment?.name || texts.name}
                    defaultValue={_establishment?.name || ""}
                />
            </View>

            <Text style={styles.phoneText}>{texts.description}</Text>
            <View style={styles.phoneInput}>
                <Input
                    disabled={_establishment ? !_establishment.admin : false}
                    type="text"
                    placeholder={_establishment?.description || texts.description}
                    defaultValue={_establishment?.description || ""}
                />
            </View>

            <Text style={styles.addressText}>{texts.address}</Text>
            <View style={styles.addressInput}>
                <Input
                    disabled={_establishment ? !_establishment.admin : false}
                    type="text"
                    placeholder={_establishment?.address || texts.address}
                    defaultValue={_establishment?.address || ""}
                />
            </View>

            <View style={styles.buttonContainer}>
                <Button
                    title={isNew.current ? texts.create : texts.save}
                    onPress={() => {
                        console.log('Save/Create pressed');
                    }}
                />
            </View>
        </View>
    );
}

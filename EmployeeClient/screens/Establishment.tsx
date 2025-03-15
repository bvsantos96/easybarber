import FontAwesome6 from '@expo/vector-icons/FontAwesome6';
import { Image } from 'expo-image';
import * as ImagePicker from 'expo-image-picker';
import { useEffect, useRef, useState } from "react";
import { Alert, View } from "react-native";

import { AlertType } from "@components/Alert";
import Button from "@components/Button";
import Divider from '@components/Divider';
import Input from "@components/Input";
import Pressable from "@components/Pressable";
import texts from "@lang/en.json";
import { Params } from "@navigation/Router";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { getStyles } from "@styles/Service";
import { ServiceAction } from "enums";
import useAlertStore from "storage/stores/AlertStore";
import useUpdateStore from "storage/stores/UpdateStore";
import { replaceMainImage, storeEstablishmentDetails, storeImage, updateEstablishmentDetails } from "utils/ApiRequest";

export type Route = {
    establishment?: EstablishmentDetail;
};

type Props = NativeStackScreenProps<typeof Params, 'Establishment'>;

export default function Establishment({ route }: Props) {
    const { toUpdate, setToUpdate } = useUpdateStore();
    const { alert, setAlertVisible } = useAlertStore();
    const { establishment: _establishment } = route.params;
    const styles = getStyles();
    const getDefaultEstablishment = (item?: EstablishmentDetail): EstablishmentDetail => {
        if (item) {
            return item;
        }
        return {
            id: 0,
            name: "",
            description: "",
            images: [],
            address: "",
            availableServices: [],
            latitude: 0,
            longitude: 0,
            nvotes: 0,
            sumVotes: 0,
            rating: 0,
        }
    }

    const getHash = (establishmentDetails?: EstablishmentDetail): string => {
        const _establishmentDetails = getDefaultEstablishment(establishmentDetails);
        return `1:${_establishmentDetails.name}2:${_establishmentDetails.description}3:${_establishmentDetails.address}5:${_establishmentDetails.images?.[0]?.data}`;
    }

    const initialItem = useRef<string>(getHash(_establishment));
    const [changed, setChanged] = useState<boolean>(false);
    const [establishment, setEstablishment] = useState<EstablishmentDetail>(getDefaultEstablishment(_establishment));
    const [image, setImage] = useState<string>(establishment.images?.[0]?.data);

    useEffect(() => {
        const canStore = establishment.name.length > 0 &&
            establishment.description.length > 0 &&
            establishment.address !== undefined &&
            establishment.address.length > 0;
        setChanged(canStore && initialItem.current !== getHash(establishment));
    }, [establishment]);

    const chooseImage = async () => {
        const { status: cameraStatus } = await ImagePicker.requestCameraPermissionsAsync();
        const { status: galleryStatus } = await ImagePicker.requestMediaLibraryPermissionsAsync();

        if (cameraStatus !== 'granted' || galleryStatus !== 'granted') {
            Alert.alert(texts.permissionRequired, texts.cameraAndGalleryPermissionRequired);
            return;
        }

        Alert.alert(
            texts.chooseOption,
            texts.newPhotoOrGalery,
            [
                {
                    text: texts.takePhoto,
                    onPress: async () => {
                        const result = await ImagePicker.launchCameraAsync({
                            mediaTypes: ImagePicker.MediaTypeOptions.Images,
                            allowsEditing: true,
                            quality: 1,
                            base64: true
                        });
                        if (!result.canceled && result.assets[0].base64) {
                            setEstablishment({ ...establishment, images: [{ data: result.assets[0].uri, id: 0 }] });
                            setImage(result.assets[0].base64);
                        }
                    },
                },
                {
                    text: texts.pickFromGallery,
                    onPress: async () => {
                        const result = await ImagePicker.launchImageLibraryAsync({
                            mediaTypes: ImagePicker.MediaTypeOptions.Images,
                            allowsEditing: true,
                            quality: 1,
                            base64: true
                        });
                        if (!result.canceled && result.assets[0].base64) {
                            setEstablishment({ ...establishment, images: [{ data: result.assets[0].uri, id: 0 }] });
                            setImage(result.assets[0].base64);
                        }
                    },
                },
                { text: texts.dismiss, style: 'cancel' },
            ]
        );
    }

    const storeEstablishment = async () => {
        try {
            alert({ type: AlertType.Loading, message: "" });
            if (!establishment.id) {
                const response = await storeEstablishmentDetails(establishment);

                if (!response.success || !response.data) {
                    setAlertVisible(false);
                    return;
                }

                establishment.id = response.data.id;

                if (establishment.images && establishment.images.length > 0 && establishment.images[0].data !== "") {
                    if (await storeImage("establishment", +establishment.id, image, true)) {
                        initialItem.current = getHash(establishment);
                    }
                }
                initialItem.current = getHash(establishment);
                setChanged(false);
                setToUpdate({
                    action: ServiceAction.REFRESH
                });
                setAlertVisible(false);
                return;
            }
            const _initialItem = initialItem.current.split("7:");
            const _item = getHash(establishment).split("7:");
            let valid = true;
            if (_initialItem[0] !== _item[0]) {
                if (!await updateEstablishmentDetails(establishment)) {
                    valid = false;
                }
            }
            if (_initialItem[1] !== _item[1]) {
                valid &&= await replaceMainImage("establishment", +establishment.id, image);
            }

            if (valid) {
                initialItem.current = getHash(establishment);
                setChanged(false);
                if (toUpdate === undefined || toUpdate.action !== ServiceAction.REFRESH) {
                    setToUpdate({ action: ServiceAction.UPDATE, obj: establishment, id: establishment.id });
                }

            }
            setAlertVisible(false);
        } catch (error: any) {
            setAlertVisible(false);
            alert({ type: AlertType.Error, message: error.message });
        }
    }

    return (
        <View style={styles.container}>
            <View style={styles.imageContainer} >
                {(establishment.images && establishment.images.length > 0) ? (
                    <Image
                        cachePolicy="memory-disk"
                        source={{ uri: establishment.images[0].data }}
                        style={styles.imageStyle}
                    />
                ) : (
                    <View style={[styles.imageStyle, styles.noImage]} />
                )
                }
                <Pressable style={[styles.addIcon, styles.iconContainer]} onPress={chooseImage}>
                    {(establishment.images && establishment.images.length > 0) ? <FontAwesome6 name="edit" size={styles.icon.width} color={styles.addIcon.color} /> : <FontAwesome6 name="plus" size={styles.icon.width} color={styles.addIcon.color} />}
                </Pressable>
                <View style={[styles.inputContainer, { height: styles.inputContainer3Items.height }]}>
                    <Divider />
                    <Input hideTitleIfNoValue placeholder={texts.name} containerStyle={styles.input} title={texts.name} round={false} defaultValue={establishment?.name || ""} onInputChange={(name) => { setEstablishment({ ...establishment, name: name }) }} />
                    <Input hideTitleIfNoValue placeholder={texts.description} containerStyle={styles.input} title={texts.description} round={false} defaultValue={establishment.description} onInputChange={(description) => setEstablishment({ ...establishment, description: description })} />
                    {/* Address input */}
                    <AddressSearchInput hideTitleIfNoValue placeholder={texts.address} containerStyle={styles.input} title={texts.address} round={false} defaultValue={establishment.address} onInputChange={(address, lat, long) => setEstablishment({ ...establishment, address: address, latitude: lat, longitude: long })} />
                </View>
            </View>
            <View style={styles.buttonContainer}>
                <Button disabled={!changed} title={texts.save} onPress={storeEstablishment} />
            </View>
        </View>
    );
}


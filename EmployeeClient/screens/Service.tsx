import { useEffect, useRef, useState } from "react";
import { View } from "react-native";
import { Image } from 'expo-image';
import FontAwesome6 from '@expo/vector-icons/FontAwesome6';
import * as ImagePicker from 'expo-image-picker';
import { Alert } from 'react-native';

import { replaceMainImage, storeImage, storeServiceDetails, updateServiceDetails } from "utils/ApiRequest";
import { getStyles } from "@styles/Service";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Params } from "@navigation/Router";
import Input from "@components/Input";
import texts from "@lang/en.json";
import Button from "@components/Button";
import ServiceTypeCombobox from "@components/ServiceTypeCombobox";
import Pressable from "@components/Pressable";
import useUpdateStore from "storage/stores/UpdateStore";

export type Route = {
    service: ServiceDetails;
};

type Props = NativeStackScreenProps<typeof Params, 'Service'>;

export default function Service({ route }: Props) {
    const { setToUpdate } = useUpdateStore();
    const { service: _service } = route.params;
    const styles = getStyles();
    const getDefaultService = (item?: ServiceDetails): ServiceDetails => {
        if (item) {
            item.images = item.images || [{ id: 0, data: item.image, isMain: true }];
            return item;
        }
        return {
            id: 0,
            name: "",
            description: "",
            duration: 0,
            serviceType: undefined,
            images: [],
            image: "",
            price: 0
        }
    }

    const getHash = (serviceDetails: ServiceDetails): string => {
        const _serviceDetails = getDefaultService(serviceDetails);
        return `1:${_serviceDetails.name}2:${_serviceDetails.description}3:${_serviceDetails.duration}4:${_serviceDetails.price}5:${_serviceDetails.serviceType?.id}6:${_serviceDetails.duration}7:${_serviceDetails.image}`;
    }

    const initialItem = useRef<string>(getHash(_service));
    const [changed, setChanged] = useState<boolean>(false);
    const [service, setService] = useState<ServiceDetails>(getDefaultService(_service));
    const [image, setImage] = useState<string>(service.image);

    useEffect(() => {
        const canStore = service.name.length > 0 && service.description.length > 0 && service.duration > 0 && service.serviceType !== undefined && service.price !== undefined;
        setChanged(canStore && initialItem.current !== getHash(service));
    }, [service]);

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
                            setService({ ...service, image: result.assets[0].uri });
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
                            setService({ ...service, image: result.assets[0].uri });
                            setImage(result.assets[0].base64);
                        }
                    },
                },
                { text: texts.dismiss, style: 'cancel' },
            ]
        );
    }

    const storeService = async () => {
        if (!service.id) {
            const response = await storeServiceDetails(service);

            if (!response.success || !response.data) {
                return;
            }

            service.id = response.data.id;

            if (service.image && service.image !== "") {
                if (await storeImage("service", +service.id, service.image, true)) {
                    initialItem.current = getHash(service);
                }
            }
            return;
        }
        const _initialItem = initialItem.current.split("7:");
        const _item = getHash(service).split("7:");
        let valid = true;
        if (_initialItem[0] !== _item[0]) {
            if (!await updateServiceDetails(service)) {
                valid = false;
            }
        }
        if (_initialItem[1] !== _item[1]) {
            valid &&= await replaceMainImage("service", +service.id, image);
        }

        if (valid) {
            initialItem.current = getHash(service);
            setChanged(false);
            setToUpdate(service);
        }
    }

    return (
        <View style={styles.container}>
            <View style={styles.imageContainer} >
                {service.image &&
                    <Image
                        cachePolicy="memory-disk"
                        source={{ uri: service.image }}
                        style={styles.imageStyle}
                    />
                }
                <Pressable style={[styles.addIcon, styles.iconContainer]} onPress={chooseImage}>
                    <FontAwesome6 name="edit" size={styles.icon.width} color={styles.addIcon.color} />
                </Pressable>
                <View style={styles.inputContainer}>
                    <Input hideTitleIfNoValue placeholder={texts.name} containerStyle={styles.input} title={texts.name} round={false} defaultValue={service.name} onInputChange={(name) => { setService({ ...service, name: name }) }} />
                    <Input hideTitleIfNoValue placeholder={texts.description} containerStyle={styles.input} title={texts.description} round={false} defaultValue={service.description} onInputChange={(description) => setService({ ...service, description: description })} />
                    <ServiceTypeCombobox defaultValue={service.serviceType} onInputChange={(_serviceType: ICategory) => {
                        setService({ ...service, serviceType: _serviceType });
                    }} />
                    <Input hideTitleIfNoValue placeholder={texts.price} containerStyle={styles.input} title={texts.price} round={false} defaultValue={`${service.price}`} onInputChange={(price) => setService({ ...service, price: +price })} />
                    <Input hideTitleIfNoValue placeholder={texts.durationInMinutes} containerStyle={styles.input} title={texts.durationInMinutes} round={false} defaultValue={`${service.duration}`} onInputChange={(duration) => setService({ ...service, duration: +duration })} />
                    <Button disabled={!changed} title={texts.save} onPress={storeService} />
                </View>
            </View>
        </View>
    );
}

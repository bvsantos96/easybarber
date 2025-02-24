import { useState } from "react";
import { View } from "react-native";
import { Image } from 'expo-image';

import PageList from "../components/PageList";
import { PageListType } from "enums";
import { getImageList } from "utils/ApiRequest";
import { getStyles } from "@styles/Service";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Params } from "@navigation/Router";

export type Route = {
    service: ServiceDetails
};

type Props = NativeStackScreenProps<typeof Params, 'Service'>;

export default function Service({ route }: Props) {
    const { service: _service } = route.params;
    const styles = getStyles();
    const getDefaultService = (): ServiceDetails => {
        return {
            id: 0,
            name: "",
            description: "",
            duration: 0,
            serviceType: 0,
            images: [],
            image: "",
            price: 0
        }
    }
    console.log(_service);
    const [service, setService] = useState<ServiceDetails>(_service || getDefaultService());

    const getMoreImages = async (page: IPage<IImage>, params?: Record<string, string | number | boolean>): Promise<IPage<IImage> | undefined> => {
        if (+service.id > 0) {
            return await getImageList(`service/${service.id}`, page, params);
        }
    }

    return (
        <View style={styles.container}>
            <View style={styles.imageContainer} >
                {(service.images && service.images.length > 0) &&
                    <PageList<IImage>
                        preload={!(service.id && +service.id > 0)}
                        type={PageListType.PAGERVIEW}
                        initialItems={service.images}
                        pageSize={4}
                        renderItem={
                            ({ item, index }: { item: IImage, index: number }) => {
                                return (
                                    <Image
                                        key={index}
                                        cachePolicy="memory-disk"
                                        source={{ uri: (item.data) }}
                                        style={styles.imageStyle}
                                    />
                                )
                            }
                        }

                        requestFunction={(page: IPage<IImage>, params?: Record<string, string | number | boolean>) => getMoreImages(page, params)} />
                }
            </View>
        </View>
    );
}

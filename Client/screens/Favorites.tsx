import ListItem from "@components/ListItem";
import PageList from "@components/PageList";
import { Routes } from "@navigation/Router";
import { getFavorites } from "utils/ApiRequest";

export default function Favorites({ navigation }: PropNavigation) {
    const loadMoreLocations = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>) => {
        return await getFavorites(page, params);
    }

    return (
        <PageList<EstablishmentInfo>
            preload={false}
            renderItem={({ item }: { item: EstablishmentInfo }) =>
                <ListItem
                    onPress={
                        () => {
                            navigation.navigate(Routes.EstablishmentDetails, item);
                        }
                    }
                    establishment={item} />
            }
            requestFunction={loadMoreLocations} />
    );
}

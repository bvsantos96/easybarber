import ListItem from "@components/ListItem";
import PageList from "@components/PageList";
import { Routes } from "@navigation/Router";
import useLocationStore from "storage/stores/LocationStore";
import { getFavorites } from "utils/ApiRequest";
import { getSelectedLocation } from "utils/Location";

export default function Favorites({ navigation }: PropNavigation) {
    const { selectedLocation } = useLocationStore();

    const loadMoreLocations = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>) => {
        let _selectedLocation: ILocation | undefined = selectedLocation;
        if (selectedLocation === undefined) {
            _selectedLocation = await getSelectedLocation();
        }
        return await getFavorites(page, params, _selectedLocation);
    }

    return (
        <PageList<EstablishmentInfo>
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

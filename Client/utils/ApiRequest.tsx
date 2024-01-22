import { LocationObject, getCurrentPositionAsync, Accuracy, PermissionResponse, requestForegroundPermissionsAsync } from 'expo-location';

async function getLocation(): Promise<void> {
  let { status }: PermissionResponse = await requestForegroundPermissionsAsync();
  if (status !== 'granted') {
    console.error('Permission to access location was denied');
    return;
  }

  try {
    let location: LocationObject = await getCurrentPositionAsync({accuracy:Accuracy.High});
    console.log(location);
  } catch (error) {
    console.log(error);
  }
}

export type BarberInfo = {
    id: number,
    name: string,
    description: string,
    distance: number,
    rating: number,
    nVotes: number,
    image: string,
}

export const getNearByBarbers = async (location: any) : Promise<BarberInfo[]>  => {
    console.log(location);
    return require("../assets/fakeAPI/nearBarbers.json");
}

export const getBarbersNearMe = async () : Promise<BarberInfo[]> => {
    return getNearByBarbers(getLocation());
}

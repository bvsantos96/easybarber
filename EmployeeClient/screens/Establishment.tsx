import { Text } from 'react-native';
import { Params } from "@navigation/Router";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

export type Route = EstablishmentInfo;

type Props = NativeStackScreenProps<typeof Params, 'Establishment'>;


// Fields to be changed
// images: string[];
// name: string;
// description: string;
// address: string;

export default function Establishment({ route, navigation }: Props) {
    const _establishment: EstablishmentInfo | undefined = route.params;
    const isNew = !_establishment;
    return (
        <Text>{_establishment.name}</Text>
    );
}

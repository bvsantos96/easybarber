import { Text } from 'react-native';
import { Params } from "@navigation/Router";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

export type Route = EstablishmentInfo;

type Props = NativeStackScreenProps<typeof Params, 'Establishment'>;

export default function Establishment({ route, navigation }: Props) {
    const _establishment: EstablishmentInfo = route.params;
    return (
        <Text>{_establishment.name}</Text>
    );
}

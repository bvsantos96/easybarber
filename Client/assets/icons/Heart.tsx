import Feather from '@expo/vector-icons/Feather';

export default function Heart({ width, height, fill }: { width: number, height: number, fill: string }) {
    return (<Feather name="heart" size={Math.min(width, height)} color={fill} />);
}

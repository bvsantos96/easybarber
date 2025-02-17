import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import { useTheme } from '@styles/ThemeContext';

const ProductsIcon = ({ width, fill }: { width: number, height: number, fill?: string }) => {
    const theme = useTheme();
    return <MaterialCommunityIcons name="bottle-tonic-plus-outline" size={width} color={fill || theme.colors.text.black} />;
}

export default ProductsIcon;

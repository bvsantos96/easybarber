import FontAwesome from '@expo/vector-icons/FontAwesome';
import { useTheme } from '@styles/ThemeContext';

const ServiceIcon = ({ width, fill }: { width: number, height: number, fill?: string }) => {
    const theme = useTheme();
    return <FontAwesome name="scissors" size={width} color={fill || theme.colors.text.black} />;
}

export default ServiceIcon;

import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useTheme } from '@styles/ThemeContext';

const EstablishmentsIcon = ({ width, fill }: { width: number, height: number, fill?: string }) => {
    const theme = useTheme();
    return (<MaterialIcons name="add-home-work" size={width} color={fill || theme.colors.text.black} />);
}

export default EstablishmentsIcon;

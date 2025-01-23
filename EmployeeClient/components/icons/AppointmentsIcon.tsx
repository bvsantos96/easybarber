import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useTheme } from '@styles/ThemeContext';

const AppointmentsIcon = ({ width, fill }: { width: number, height: number, fill?: string }) => {
    const theme = useTheme();
    return (<MaterialIcons name="list-alt" size={width} color={fill || theme.colors.text.black} />);
}

export default AppointmentsIcon;

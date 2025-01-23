import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useTheme } from '@styles/ThemeContext';

const SchedulesIcon = ({ width, fill }: { width: number, height?: number, fill?: string }) => {
    const theme = useTheme();
    return (<MaterialIcons name="edit-calendar" size={width} color={fill || theme.colors.text.black} />);
}

export default SchedulesIcon;

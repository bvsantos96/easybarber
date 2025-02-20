import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useTheme } from '@styles/ThemeContext';

const WeekViewIcon = ({ width, fill }: { width: number, height: number, fill?: string }) => {
    const theme = useTheme();
    return (<MaterialIcons name="calendar-view-week" size={width} color={fill || theme.colors.text.black} />);
}

export default WeekViewIcon;

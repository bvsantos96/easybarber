import { CalendarBody, CalendarContainer, CalendarHeader } from '@howljs/calendar-kit';

const WeekView = () => {
    return (
        <CalendarContainer numberOfDays={7} scrollByDay={true}>
            <CalendarHeader />
            <CalendarBody />
        </CalendarContainer>
    );
}

export default WeekView;

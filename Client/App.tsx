import SignIn from './screens/SignIn';
import Register from './components/Register';

export default function App() {
    return (
        <SignIn page={<Register />} />
    );
}

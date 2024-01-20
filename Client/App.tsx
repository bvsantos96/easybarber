import SignIn from './screens/SignIn';
import Login from './components/Login';

export default function App() {
    return (
        <SignIn page={<Login/>} />
    );
}

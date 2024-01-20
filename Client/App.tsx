import {useState} from 'react';
import { View } from 'react-native';
import {styles} from '@styles/main';
import Login from '@components/Login';

export default function App() {
  const [test, setTest] = useState("");
  return (
    <Login />
  );
}

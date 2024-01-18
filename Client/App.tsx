import {useState} from 'react';
import { View } from 'react-native';
import FirstLogin from '@screens/FirstLogin';
import {styles} from '@styles/main';

export default function App() {
  const [test, setTest] = useState("");
  return (
    <FirstLogin />
  );
}

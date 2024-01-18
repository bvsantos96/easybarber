import {useState} from 'react';
import { View } from 'react-native';
import Page2 from '@screens/firstLogin/Page2';
import {styles} from '@styles/main';

export default function App() {
  const [test, setTest] = useState("");
  return (
    <Page2 />
  );
}

import { Modal, View } from 'react-native';
import { styles } from '../styles/Filter';
export default function Filter({modalVisible, setModalVisible} : {modalVisible: boolean, setModalVisible: Function}){
    return(
         <Modal
            animationType="slide"
            visible={modalVisible}
            onRequestClose={() => {
            setModalVisible(!modalVisible);
        }}>
            <View style={styles.container}>
            </View>
        </Modal>
        );
}

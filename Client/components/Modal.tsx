import React, { useState } from 'react';
import { Modal, View, TouchableOpacity, StyleSheet } from 'react-native';

const CustomModal = ({ children, modalContent }) => {
  const [isVisible, setIsVisible] = useState(false);

  const toggleModal = () => {
    setIsVisible(!isVisible);
  };

  return (
    <>
      <TouchableOpacity onPress={toggleModal}>
        {children}
      </TouchableOpacity>
      <Modal
        transparent={true}
        visible={isVisible}
        onRequestClose={toggleModal}
        animationType="slide"
      >
        <View style={styles.overlay}>
          <TouchableOpacity style={styles.background} onPress={toggleModal} />
          <View style={styles.modalContainer}>
            {modalContent}
          </View>
        </View>
      </Modal>
    </>
  );
};

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  background: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    backgroundColor: 'white',
    padding: 20,
    borderRadius: 10,
    zIndex: 10,
  },
});

export default CustomModal;

package com.teamsantos.easybarber.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;

@Service
public class EstablishmentService {
    @Autowired
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public BaseEstablishmentDTO getEstablishment(Long id) throws NotFoundException {
        return establishmentRepository.findByIDNoOwner(id).orElseThrow(NotFoundException::new);
    }

	public BaseEstablishmentDTO createEstablishment(BaseEstablishmentDTO establishmentDTO, Long userId) {
        Establishment establishment = modelMapper.map(establishmentDTO, Establishment.class);
        if (establishment != null) {
            establishment.setOwnerId(userId);
            establishmentRepository.save(establishment);
            return modelMapper.map(establishment, BaseEstablishmentDTO.class);
        } else
            throw new IllegalArgumentException("Establishment cannot be null");

        User user = modelMapper.map(userCreateDTO, User.class);
        if (user != null) {
            try {
                Optional<User> oUser = userRepository.findByMobileInformation(user.getMobileInformation());
                if (oUser.isPresent()) {
                    user = oUser.get();
                    if (!isEmployee || InitializedBean.isEmployee(user))
                        throw new UserAlreadyExistsException();
                }
            } catch (Exception e) {
                throw new UserAlreadyExistsException();
            }
            user.setUserTypeId(InitializedBean
                    .getUserType(isEmployee ? InitializedBean.UserTypes.EMPLOYEE : InitializedBean.UserTypes.CLIENT));
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else
            throw new IllegalArgumentException("User cannot be null");
	}
}

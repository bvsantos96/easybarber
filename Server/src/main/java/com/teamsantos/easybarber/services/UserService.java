package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.employee.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.product.ProductDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.DTO.user.UserSignInDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository,
            EmployeeRepository employeeRepository,
            EntityManager entityManager,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = Utils.getModelMapper();
        this.entityManager = entityManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return PageDTO.toDTO(modelMapper, userRepository.findAll(pageable), UserDTO.class, pageable);
    }

    @Transactional(readOnly = true)
    public UserSignInDTO findUserSignIn(UserCreateDTO userCreateDTO) throws Exception {
        UserSignInDTO user = userRepository.findUserSignInByMobileInformation(userCreateDTO.getMobileInformation())
                .orElseThrow(UserNotFoundException::new);
        if (PasswordEncoding.getPasswordEncoder().matches(userCreateDTO.getPassword(), user.getPassword())) {
            // TODO: if we get 2 many users this might me moved to a string in the user
            // table so that we can load them faster.
            // This will make the user type change a bit slower but that is not that
            // frequent of a request compared with the login that affects everyuser
            user.addUserTypesId(userRepository.getAllUserTypes(user.getId()));
            return user;
        } else {
            throw new IllegalArgumentException("Password is incorrect");
        }
    }

    @Transactional(readOnly = true)
    public String loginUser(UserSignInDTO user) throws Exception {
        return jwtUtils.generateToken(user.getId(), user.getEmployeeId(),
                UserTypeService.getUserRoles(user.getUserTypeIds()));
    }

    @Transactional(readOnly = false)
    public void createEmployee(Long userId) throws Exception {
        if (employeeRepository.existsByUserId(userId))
            throw new UserAlreadyExistsException();
        Employee employee = new Employee();
        employee.setEnabled(true);
        employee.setUser(entityManager.getReference(User.class, userId));
        employeeRepository.save(employee);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.addUserType(entityManager.getReference(UserType.class,
                UserTypeService.getUserType(UserTypeService.UserTypes.EMPLOYEE)));
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee) throws Exception {
        return createUser(userCreateDTO, isEmployee, false);
    }

    public UserDTO createAdmin(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false, true);
    }

    @Transactional
    private Employee createEmployee(EmployeeCreateDTO employeeDTO, long userId) throws UserAlreadyExistsException {
        if (employeeRepository.existsByUserId(userId))
            throw new UserAlreadyExistsException();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setEnabled(true);
        employee.setUser(entityManager.getReference(User.class, userId));
        return employeeRepository.save(employee);
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee, boolean systemAdmin) throws Exception {
        if (!userCreateDTO.isValidNumberString()) {
            throw new IllegalArgumentException("Mobile information is not valid");
        }
        if (!userCreateDTO.isValidPassword()) {
            throw new IllegalArgumentException("Password is not valid");
        }
        userCreateDTO.setPassword(PasswordEncoding.encode(userCreateDTO.getPassword()));
        User user = User.load(userCreateDTO);
        if (user != null) {
            try {
                if (userCreateDTO.getId() != null && userRepository.existsById(userCreateDTO.getId())) {
                    userCreateDTO.setId(null);
                }
                if (userRepository.existsByMobileInformation(user.getMobileInformation())) {
                    if (!isEmployee) {
                        throw new UserAlreadyExistsException();
                    } else {
                        user = userRepository.findByMobileInformation(user.getMobileInformation())
                                .orElseThrow(UserAlreadyExistsException::new);
                    }
                }
            } catch (Exception e) {
                throw new UserAlreadyExistsException();
            }

            user.addUserType(entityManager.getReference(UserType.class, UserTypeService
                    .getUserType(isEmployee ? UserTypeService.UserTypes.EMPLOYEE : UserTypeService.UserTypes.CLIENT)));
            if (user.getUserTypes() == null || user.getUserTypes().isEmpty()) {
                user.addUserType(entityManager.getReference(UserType.class, UserTypeService
                        .getUserType(UserTypeService.UserTypes.CLIENT)));
            }
            if (systemAdmin)
                user.addUserType(entityManager.getReference(UserType.class,
                        UserTypeService.getUserType(UserTypeService.UserTypes.SYSTEM_ADMIN)));

            user = userRepository.save(user);
            if (isEmployee) {
                UserDTO ret = new UserDTO();
                ret.setId(createEmployee((EmployeeCreateDTO) userCreateDTO, user.getId()).getId());
                return ret;
            }
            return modelMapper.map(user, UserDTO.class);
        } else
            throw new IllegalArgumentException("User cannot be null");
    }

    @Transactional
    public void deleteUser(Long id) {
        if (employeeRepository.existsByUserId(id))
            employeeRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(UserCreateDTO userCreateDTO) {
        User oldUser = userRepository.findById(UserContext.getUserId()).orElseThrow(UserNotFoundException::new);
        oldUser.updateNonNullValues(userCreateDTO);
        userRepository.save(oldUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUser() {
        return modelMapper.map(userRepository.findById(UserContext.getUserId()), UserDTO.class);
    }

    @Transactional(readOnly = true)
    public boolean userChangePermissions(Principal principal, String mobileInformation) {
        return principal.getName().equals(mobileInformation);
    }

    @Transactional(readOnly = true)
    public boolean existsUserByMobileInformation(String mobileInformation) {
        return userRepository.existsByMobileInformation(mobileInformation);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsersByType(String userType, Pageable pageable) {
        return userRepository
                .findByUserTypeId(UserTypeService.getUserType(userType), pageable)
                .map((element) -> modelMapper.map(element, UserDTO.class));
    }

    @Transactional(readOnly = true)
    public boolean existsByMobileInformation(String mobileInformation) {
        return userRepository.existsByMobileInformation(mobileInformation);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByMobileNr(String mobileNr) throws Exception {
        Optional<User> user = userRepository.findByMobileInformation(mobileNr);

        if (user.isEmpty()) {
            throw new Exception("The mobile number doesnt exist");
        }

        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Transactional
    public void changeUserPwd(UserDTO userDTO, String newPwd) throws Exception {
        Optional<User> userOpt = userRepository.findById(userDTO.getId());

        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOpt.get();
        user.setPassword(PasswordEncoding.encode(newPwd));
        userRepository.save(user);
    }

    @Transactional
    public void favorite(Long establishmentId) {
        userRepository.findById(UserContext.getUserId()).ifPresent(user -> {
            user.addFavoriteEstablishment(entityManager.getReference(Establishment.class, establishmentId));
            userRepository.save(user);
        });
    }

    @Transactional
    public void unfavorite(Long establishmentId) {
        userRepository.findById(UserContext.getUserId()).ifPresent(user -> {
            user.getFavoriteEstablishments().removeIf(establishment -> establishment.getId().equals(establishmentId));
            userRepository.save(user);
        });
    }

    @Transactional(readOnly = true)
    public Page<EstablishmentDTO> getFavoriteEstablishments(Long userId, Point location, Pageable pageable) {
        if (location == null)
            return userRepository.findFavoriteEstablishmentsByUserId(userId, pageable);
        return userRepository.findFavoriteEstablishmentsByUserId(userId, location, pageable);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Long establishmentId) {
        return userRepository.findById(UserContext.getUserId()).map(user -> {
            return user.getFavoriteEstablishments().stream()
                    .anyMatch(establishment -> establishment.getId().equals(establishmentId)) ? true : false;
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Long> getFavoriteEstablishmentsIds(Long userId) {
        return userRepository.getFavoriteEstablishmentsIds(userId);
    }

    @Transactional(readOnly = false)
    public void addSuggestionToUser(Long userId, Set<Long> productIds) {
        for (Long productId : productIds) {
            userRepository.createSuggestion(userId, productId);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProductSuggestions(long userId) {
        return userRepository.getProductSuggestions(userId);
    }
}

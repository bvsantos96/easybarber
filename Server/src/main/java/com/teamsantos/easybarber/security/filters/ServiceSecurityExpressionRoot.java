package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.ServiceService;

public class ServiceSecurityExpressionRoot extends SecurityExpressionRoot {
    private final ServiceRepository serviceRepository;
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;

    public ServiceSecurityExpressionRoot(Authentication authentication, ServiceRepository serviceRepository,
            EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository,
            EstablishmentServiceRepository establishmentServiceRepository) {
        super(authentication);
        this.serviceRepository = serviceRepository;
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
        this.establishmentServiceRepository = establishmentServiceRepository;
    }

    public static boolean _hasServiceOwnerPermission(ServiceRepository _serviceRepository, Long serviceId) {
        Long id = UserContext.getCurrentUser().getEmployeeId();
        if (id != null && _serviceRepository.checkIfEmployeeIsServiceOwner(serviceId, id)) {
            return true;
        }
        return false;
    }

    public static boolean _hasServiceOwnerPermission(ServiceService serviceService, Long serviceId) {
        Long id = UserContext.getCurrentUser().getEmployeeId();
        return id != null && serviceService.checkIfEmployeeIsServiceOwner(serviceId, id);
    }

    public boolean hasServiceOwnerPermission(Long targetDomainObject) {
        return _hasServiceOwnerPermission(serviceRepository, targetDomainObject);
    }

    public boolean hasServiceEstablishmentReadPermission(long establishmentServiceId,
            Long establishmentServiceEmployeeId) {
        try {
            boolean isEstablishmentOwner = establishmentServiceRepository.isEstablishmentOwner(
                    UserContext.getEmployeeId(),
                    establishmentServiceId);
            if (establishmentServiceEmployeeId == null) {
                return isEstablishmentOwner;
            }
            return (isEstablishmentOwner
                    || establishmentServiceEmployeeRepository.canModifyEstablishmentServiceEmployee(
                            UserContext.getEmployeeId(),
                            establishmentServiceEmployeeId));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasServiceEstablishmentWritePermission(long establishmentServiceId,
            Long establishmentServiceEmployeeId) {
        try {
            boolean isEstablishmentServiceOwner = establishmentServiceRepository.isEstablishmentOwner(
                    UserContext.getEmployeeId(),
                    establishmentServiceId);
            if (establishmentServiceEmployeeId == null) {
                return isEstablishmentServiceOwner;
            }
            return (isEstablishmentServiceOwner
                    || establishmentServiceEmployeeRepository.canModifyEstablishmentServiceEmployee(
                            UserContext.getEmployeeId(),
                            establishmentServiceEmployeeId));
        } catch (Exception e) {
            return false;
        }
    }
}

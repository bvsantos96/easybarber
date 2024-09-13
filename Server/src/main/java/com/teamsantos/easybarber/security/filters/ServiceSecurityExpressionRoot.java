package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.ServiceService;

public class ServiceSecurityExpressionRoot extends SecurityExpressionRoot {
    private final ServiceRepository serviceRepository;

    public ServiceSecurityExpressionRoot(Authentication authentication, ServiceRepository serviceRepository) {
        super(authentication);
        this.serviceRepository = serviceRepository;
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
}

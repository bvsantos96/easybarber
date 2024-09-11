package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;

public class ServiceSecurityExpressionRoot extends SecurityExpressionRoot {
    private final ServiceRepository serviceRepository;

    public ServiceSecurityExpressionRoot(Authentication authentication, ServiceRepository serviceRepository) {
        super(authentication);
        this.serviceRepository = serviceRepository;
    }

    public boolean hasServiceOwnerPermission(Long targetDomainObject) {
        Long id = UserContext.getCurrentUser().getEmployeeId();
        if (id != null && serviceRepository.checkIfEmployeeIsServiceOwner(targetDomainObject, id)) {
            return true;
        }
        return false;
    }
}

package com.teamsantos.easybarber.security.filters;

import com.teamsantos.easybarber.repositories.ServiceRepository;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class ServiceSecurityExpressionRoot extends SecurityExpressionRoot {
    private final ServiceRepository serviceRepository;

    public ServiceSecurityExpressionRoot(Authentication authentication, ServiceRepository serviceRepository) {
        super(authentication);
        this.serviceRepository = serviceRepository;
    }

    public boolean hasServiceOwnerPermission(Long targetDomainObject) {
        return serviceRepository.checkIfEmployeeIsServiceOwner(targetDomainObject, this.getAuthentication().getName());
    }
}

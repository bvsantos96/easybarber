package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.ServiceRepository;

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

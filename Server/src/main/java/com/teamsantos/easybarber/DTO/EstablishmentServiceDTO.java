package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstablishmentServiceDTO extends BaseDTO {
    private NameIdImageDTO establishment;
    private ServiceBaseDTO service;
    private NameIdImageDTO employee;
    private Double price;
    private Boolean active;

    public EstablishmentServiceDTO(Long id, NameIdImageDTO establishment, ServiceBaseDTO service,
            NameIdImageDTO employee,
            Double price, Boolean active) {
        super(id);
        this.establishment = establishment;
        this.service = service;
        this.employee = employee;
        this.price = price;
        this.active = active;
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName, String establishmentImage,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration, String serviceImage,
            Long employeeId,
            String employeeName, String employeeImage, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName, establishmentImage),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId, serviceImage),
                new NameIdImageDTO(employeeId, employeeName, employeeImage), price, active);
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration, String serviceImage,
            Long employeeId,
            String employeeName, String employeeImage, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId, serviceImage),
                new NameIdImageDTO(employeeId, employeeName, employeeImage), price, active);
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName, String establishmentImage,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration,
            Long employeeId,
            String employeeName, String employeeImage, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName, establishmentImage),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId),
                new NameIdImageDTO(employeeId, employeeName, employeeImage), price, active);
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName, String establishmentImage,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration, String serviceImage,
            Long employeeId,
            String employeeName, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName, establishmentImage),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId, serviceImage),
                new NameIdImageDTO(employeeId, employeeName), price, active);
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration, String serviceImage,
            Long employeeId,
            String employeeName, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId, serviceImage),
                new NameIdImageDTO(employeeId, employeeName), price, active);
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName, String establishmentImage,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration,
            Long employeeId,
            String employeeName, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName, establishmentImage),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId),
                new NameIdImageDTO(employeeId, employeeName), price, active);
    }

    public EstablishmentServiceDTO(Long id, Long establishmentId, String establishmentName,
            Long serviceId, String serviceName, String serviceDescription, int serviceDuration,
            Long employeeId,
            String employeeName, Double price, Boolean active) {
        this(id, new NameIdImageDTO(establishmentId, establishmentName),
                new ServiceBaseDTO(serviceId, serviceName, serviceDescription, serviceDuration,
                        employeeId),
                new NameIdImageDTO(employeeId, employeeName), price, active);
    }
}

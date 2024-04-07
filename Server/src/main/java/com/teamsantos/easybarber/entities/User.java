package com.teamsantos.easybarber.entities;

import java.time.LocalDateTime;
import java.util.Set;

import com.teamsantos.easybarber.DTO.UserCreateDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = @Index(columnList = "mobileInformation"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long userTypeId;
    @Column
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String countryMobile;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String mobileInformation;
    @Column
    private LocalDateTime tokenExpiration;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Employee employee;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Appointment> appointment;

    private int hashCodeWithNullCheck(String item) {
        return item != null ? item.hashCode() : 0;
    }

    @Override
    public int hashCode() {
        return hashCodeWithNullCheck(email) + hashCodeWithNullCheck(password) + hashCodeWithNullCheck(countryMobile)
                + hashCodeWithNullCheck(mobile) + hashCodeWithNullCheck(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (id != null && ((User) o).id != null)
            return id.equals(((User) o).id);
        return email.equals(((User) o).email)
                && password.equals(((User) o).password)
                && countryMobile.equals(((User) o).countryMobile)
                && mobile.equals(((User) o).mobile)
                && name.equals(((User) o).name);
    }

    public boolean equalsIgnoreEmptyValues(Object o) {
        if (this == o)
            return true;
        if (id != null && ((User) o).id != null)
            return id.equals(((User) o).id);
        return isNullOrEqual(email, ((User) o).email)
                && isNullOrEqual(password, ((User) o).password)
                && isNullOrEqual(countryMobile, ((User) o).countryMobile)
                && isNullOrEqual(mobile, ((User) o).mobile)
                && isNullOrEqual(name, ((User) o).name)
                && isNullOrEqual(mobileInformation, ((User) o).mobileInformation);
    }

    /**
     * This method checks if value2 is null or empty, if it is, it returns true, if
     * not, it checks if value1 is equal to value2
     * 
     * @param value1 string 1
     * @param value2 string 2
     * @return boolean representing the comparison
     */
    private boolean isNullOrEqual(String value1, String value2) {
        return isNullOrEmpty(value2) || value1.equals(value2);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Updates the user with the non-null values of the userDTO
     * Note: This method does not update the password, mobileInformation or
     * userTypeId. This is because these values should not be updated by a global
     * user update.
     * 
     * @param userDTO the userDTO with the new values
     */
    public void updateNonNullValues(UserCreateDTO userDTO) {
        if (userDTO.getName() != null)
            name = userDTO.getName();
    }
}

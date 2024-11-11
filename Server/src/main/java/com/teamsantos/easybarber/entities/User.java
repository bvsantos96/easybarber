package com.teamsantos.easybarber.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.teamsantos.easybarber.DTO.user.UserCreateDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = @Index(columnList = "mobileInformation"))
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_user_type", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_type_id"))
    @BatchSize(size = 10)
    private Set<UserType> userTypes = new HashSet<>();
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
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Employee employee;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<Appointment> appointment;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<Location> location;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_favorite", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "establishment_id"))
    @BatchSize(size = 10)
    private Set<Establishment> favoriteEstablishments = new HashSet<>();

    public void addFavoriteEstablishment(Establishment establishment) {
        if (favoriteEstablishments == null) {
            favoriteEstablishments = new HashSet<>();
        }
        favoriteEstablishments.add(establishment);
    }

    public User(long id, String countryMobile, String mobile, String name, Set<UserType> userTypes) {
        this.id = id;
        this.countryMobile = countryMobile;
        this.mobile = mobile;
        this.mobileInformation = countryMobile + mobile;
        this.name = name;
        this.userTypes = userTypes;
    }

    public void addUserType(UserType userType) {
        if (this.userTypes == null) {
            this.userTypes = new HashSet<>();
        }
        this.userTypes.add(userType);
    }

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

    public static User load(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setMobileInformation(userCreateDTO.getMobileInformation());
        user.setCountryMobile(userCreateDTO.getCountryMobile());
        user.setMobile(userCreateDTO.getMobile());
        user.setName(userCreateDTO.getName());
        user.setPassword(userCreateDTO.getPassword());
        return user;
    }
}

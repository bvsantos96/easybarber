package com.teamsantos.easybarber.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private float latitude;
    @Column(nullable = false)
    private float longitude;

    @Override
    public int hashCode() {
        return Long.hashCode(user.getId()) + hashCodeWithNullCheck(address) + Float.hashCode(latitude)
                + Float.hashCode(longitude);
    }

    private int hashCodeWithNullCheck(String item) {
        return item != null ? item.hashCode() : 0;
    }
}

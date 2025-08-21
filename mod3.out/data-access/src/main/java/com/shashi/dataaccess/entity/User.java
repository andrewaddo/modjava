package com.shashi.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "mobile")
    private Long mobile;

    @Column(name = "address", length = 250)
    private String address;

    @Column(name = "pincode")
    private Integer pincode;

    @Column(name = "password", length = 20)
    private String password;
}

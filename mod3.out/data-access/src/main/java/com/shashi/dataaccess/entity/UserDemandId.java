package com.shashi.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDemandId implements Serializable {

    @Column(name = "username", length = 60, nullable = false)
    private String username;

    @Column(name = "prodid", length = 45, nullable = false)
    private String prodid;
}

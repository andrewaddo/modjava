package com.shashi.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "usercart")
@Data
public class UserCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 60)
    private String username;

    @Column(name = "prodid", length = 45)
    private String prodid;

    @Column(name = "quantity")
    private Integer quantity;

    @Transient
    private String pname;

    @Transient
    private BigDecimal pprice;
}

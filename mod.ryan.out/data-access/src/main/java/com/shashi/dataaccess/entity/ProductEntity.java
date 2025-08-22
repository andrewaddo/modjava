package com.shashi.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
public class ProductEntity {

    @Id
    @Column(name = "pid", length = 45, nullable = false)
    private String pid;

    @Column(name = "pname", length = 100)
    private String pname;

    @Column(name = "ptype", length = 20)
    private String ptype;

    @Column(name = "pinfo", length = 350)
    private String pinfo;

    @Column(name = "pprice", precision = 12, scale = 2)
    private BigDecimal pprice;

    @Column(name = "pquantity")
    private Integer pquantity;

    @Lob
    @Column(name = "image")
    private byte[] image;
}

package com.shashi.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usercart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usercart {

    @EmbeddedId
    private UsercartId id;

    @Column(name = "quantity")
    private Integer quantity;
}

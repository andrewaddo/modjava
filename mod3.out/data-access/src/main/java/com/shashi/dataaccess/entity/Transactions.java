package com.shashi.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

    @Id
    @Column(name = "transid", length = 45, nullable = false)
    private String transid;

    @Column(name = "username", length = 60)
    private String username;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
}

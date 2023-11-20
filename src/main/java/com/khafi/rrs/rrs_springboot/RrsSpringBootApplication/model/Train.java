package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_id")
    private Long id;

    @Column(name = "train_name",nullable = false)
    private String name;

    @Column(name = "train_total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "money_amount")
    private Double price;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "train")
    private TrainStatus trainStatus;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "train")
    private List<Ticket> ticket;
    // Getters and setters
}



package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    private Long id;
    @Column(name = "u_email",unique = true)
    private String email;

    @Column(name = "u_username",unique = true,nullable = false)
    private String username;

    @Column(name = "u_password", nullable = false)
    private String password;
    @OneToMany(
            cascade = CascadeType.ALL, orphanRemoval = false,
            mappedBy = "user")
    private List<Ticket> tickets;

    // Getters and setters
}

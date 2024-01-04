package com.hotelreservation.roomreservationservice.model.entity;

import com.hotelreservation.roomreservationservice.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Balance> balance;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "password")
    private String password;

}

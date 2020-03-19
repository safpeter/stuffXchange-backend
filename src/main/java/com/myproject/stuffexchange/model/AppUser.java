package com.myproject.stuffexchange.model;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String country;

    private double rating;

    private LocalDate dateOfSignUp;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<StuffProperty> stuffs;

    @Builder.Default
    @ElementCollection
    private  List<String> roles = new ArrayList<>();
}

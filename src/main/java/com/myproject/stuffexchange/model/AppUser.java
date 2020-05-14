package com.myproject.stuffexchange.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Component
@Builder
@Getter
@Setter
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

    private  int numberOfRating;

    private LocalDate dateOfSignUp;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<StuffProperty> stuffs;

    @Builder.Default
    @ElementCollection
    private  List<String> roles = new ArrayList<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Singular
    @ElementCollection
    private Map<AppUser, Double> myRatings = new HashMap<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Singular
    @JoinTable
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<StuffProperty> myFavourites = new ArrayList<>();

}

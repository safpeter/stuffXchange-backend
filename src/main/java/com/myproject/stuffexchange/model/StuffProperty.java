package com.myproject.stuffexchange.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StuffProperty {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private double price;

    private String currency;

    private LocalDate date;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private AppUser user;

    private long mainPicture;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images;


    @EqualsAndHashCode.Exclude
    @JoinTable
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<AppUser> users = new ArrayList<>();


}

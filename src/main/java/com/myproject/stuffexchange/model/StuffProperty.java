package com.myproject.stuffexchange.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    @ManyToOne
    private User user;

    private long mainPicture;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Image> images;
}

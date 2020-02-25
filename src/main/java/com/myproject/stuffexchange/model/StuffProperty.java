package com.myproject.stuffexchange.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
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

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Image> images;
}

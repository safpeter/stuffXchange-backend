package com.myproject.stuffexchange.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Image {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade=CascadeType.ALL)
    private StuffProperty stuffProperty;

    @Lob
    private byte[] image;
}

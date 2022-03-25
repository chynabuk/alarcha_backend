package com.project.alarcha.entities;


import com.project.alarcha.enums.NatureType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "nature")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Nature extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "photo", nullable = false)
    private byte[] photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NatureType natureType;
}

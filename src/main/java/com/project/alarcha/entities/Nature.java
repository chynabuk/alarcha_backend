package com.project.alarcha.entities;


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

    @ManyToOne
    @JoinColumn(name = "nature_type_id", referencedColumnName = "id")
    private NatureType natureType;
}

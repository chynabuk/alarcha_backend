package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "object_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectType extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL)
    private List<MenuSection> menuSections;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL)
    private List<Object> objects;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    private Area area;
}

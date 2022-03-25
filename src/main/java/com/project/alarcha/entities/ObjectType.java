package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "object_type")
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
    private List<Menu> menus;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL)
    private List<Objects> objects;
}

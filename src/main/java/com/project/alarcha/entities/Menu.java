package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "menu_section_id", referencedColumnName = "id")
    private MenuSection menuSection;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "object_type_id", referencedColumnName = "id")
    private ObjectType objectType;
}
package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuSection extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "menuSection", cascade = CascadeType.ALL)
    private List<Menu> menus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "object_type_id", referencedColumnName = "id")
    private ObjectType objectType;
}

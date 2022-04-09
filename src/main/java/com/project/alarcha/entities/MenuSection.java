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
    private List<Menu> menu;
}

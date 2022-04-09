package com.project.alarcha.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Area extends BaseEntity{
    @Column(name = "area_name", nullable = false, unique = true)
    private String areaName;

    @OneToOne
    @JoinColumn(name = "area_admin", nullable = false)
    private User user;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<Object> objects;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<Hotel> hotels;
}

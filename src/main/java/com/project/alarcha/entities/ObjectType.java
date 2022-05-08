package com.project.alarcha.entities;

import com.project.alarcha.enums.TimeType;
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
    private Float price;

    @Column(name = "price_per_hour")
    private Float pricePerHour;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL)
    private List<MenuSection> menuSections;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL)
    private List<Object> objects;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL)
    private List<ObjectTypeImage> objectTypeImages;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_type")
    private TimeType timeType;
}

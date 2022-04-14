package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "objects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Object extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "object_type_id", referencedColumnName = "id")
    private ObjectType objectType;

    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL)
    private List<ObjectOrder> objectOrders;
}

package com.project.alarcha.entities;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomType extends BaseEntity{

    @OneToMany(mappedBy = "roomType",
    cascade = CascadeType.ALL)
    private List<Room> rooms;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price", nullable = false)
    private float price;
}

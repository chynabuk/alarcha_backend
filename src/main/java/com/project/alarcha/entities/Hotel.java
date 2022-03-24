package com.project.alarcha.entities;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Hotel extends BaseEntity{

    @OneToMany(mappedBy = "hotel",
    cascade = CascadeType.ALL)
    private List<Rooms> rooms;

    @OneToMany(mappedBy = "hotel",
    cascade = CascadeType.ALL)
    private List<HotelHallsType> hotelHalls;

    @Column(name = "name", nullable = false)
    private String name;
}

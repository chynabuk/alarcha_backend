package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hotel_halls_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelHallsType extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "hotelHallsType",
    cascade = CascadeType.ALL)
    private List<HotelHallsOrder>  hotelHallsOrders;

}

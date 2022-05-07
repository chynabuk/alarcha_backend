package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hotel_halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelHall extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "price_for_next_hours")
    private Float priceForNextHours;

    @Column(name = "number_of_seats")
    private Integer numberOfSeats;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "hotelHall",
    cascade = CascadeType.ALL)
    private List<HotelHallOrder> hotelHallOrders;

    @OneToMany(mappedBy = "hotelHall", cascade = CascadeType.ALL)
    private List<HotelHall_IMG> hotelHallImages;

}

package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hotel_hall_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelHallOrder extends BaseEntity{

    @Column(name = "registration_date", nullable = false)
    private Date registration_date;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private HotelHall hotelHall;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

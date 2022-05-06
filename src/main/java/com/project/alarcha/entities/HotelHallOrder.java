package com.project.alarcha.entities;

import com.project.alarcha.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
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

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "total_price")
    private Float totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private HotelHall hotelHall;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_full_name")
    private String userFullName;
}

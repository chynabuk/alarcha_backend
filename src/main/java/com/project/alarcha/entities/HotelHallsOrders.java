package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hotelHallsOrders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelHallsOrders extends BaseEntity{

    @Column(name = "registration_date", nullable = false)
    private Date registration_date;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private HotelHallsType hotelHallsType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

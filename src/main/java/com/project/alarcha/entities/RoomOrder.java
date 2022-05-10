package com.project.alarcha.entities;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.enums.RoomOrderStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;;

@Entity
@Table(name = "room_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomOrder extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_full_name")
    private String userFullName;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus orderStatus;
}
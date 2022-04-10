package com.project.alarcha.entities;

import com.project.alarcha.enums.RoomStatus;
import com.project.alarcha.enums.RoomType;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Room extends BaseEntity{

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus roomStatus;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room",
    cascade = CascadeType.ALL)
    private List<RoomOrder> roomOrders;

    @Column(name = "bed_number", nullable = false)
    private Integer bedNumber;

    @Column(name = "type", nullable = false)
    private RoomType roomType;

    @Column(name = "price", nullable = false)
    private float price;
}

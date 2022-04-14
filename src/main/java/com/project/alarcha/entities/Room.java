package com.project.alarcha.entities;

import com.project.alarcha.enums.RoomStatus;
import lombok.*;

import javax.persistence.*;
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
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "hotel_name")
    private String hotelName;

    @OneToMany(mappedBy = "room",
    cascade = CascadeType.ALL)
    private List<RoomOrder> roomOrders;

    @Column(name = "bed_number", nullable = false)
    private Integer bedNumber;
}

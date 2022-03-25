package com.project.alarcha.entities;

import com.project.alarcha.enums.RoomStatus;
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
public class Rooms extends BaseEntity{

    @Column(name = "room_number", nullable = false)
    private int room_number;

    @Column(name = "start_date", nullable = false)
    private Date start_date;

    @Column(name = "end_date", nullable = false)
    private Date end_date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus roomStatus;

    @ManyToOne
    @JoinColumn(name = "room_type")
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;


    @OneToMany(mappedBy = "rooms",
    cascade = CascadeType.ALL)
    private List<RoomOrders> roomOrders;
}

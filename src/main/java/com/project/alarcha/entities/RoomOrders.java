package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "roomOrders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomOrders extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Rooms rooms;



    @Column(name = "registration_date", nullable = false)
    private Date registration_date;

}

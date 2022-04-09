package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

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

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;



    @Column(name = "registration_date", nullable = false)
    private Date registration_date;

}

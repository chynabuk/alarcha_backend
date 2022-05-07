package com.project.alarcha.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "hotel_hall_images")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HotelHall_IMG extends BaseEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_hall_id")
    private HotelHall hotelHall;

    @Column(name = "img")
    private byte[] img;
}

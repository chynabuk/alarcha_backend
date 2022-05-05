package com.project.alarcha.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hotel_hall_images")
@Getter
@Setter
@NoArgsConstructor
public class HotelHall_IMG extends BaseEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_hall_id")
    private HotelHall hotelHall;

    @Column(name = "img")
    private byte[] img;
}

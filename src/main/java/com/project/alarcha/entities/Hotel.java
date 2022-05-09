package com.project.alarcha.entities;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Hotel extends BaseEntity{

    @OneToMany(mappedBy = "hotel",
    cascade = CascadeType.ALL)
    private List<RoomType> roomTypes;

    @OneToMany(mappedBy = "hotel",
    cascade = CascadeType.ALL)
    private List<HotelHall> hotelHalls;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Hotel_Img> hotelImgs;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    private Area area;
}

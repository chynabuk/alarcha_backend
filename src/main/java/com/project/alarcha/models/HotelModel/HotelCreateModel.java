package com.project.alarcha.models.HotelModel;

import com.project.alarcha.entities.HotelHallsType;
import com.project.alarcha.entities.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelCreateModel {
    private String hotelName;
    private List<Room> rooms;
    private List<HotelHallsType> hotelHallsTypes;
    private Long areaId;
}
package com.project.alarcha.models.HotelModel;

import com.project.alarcha.models.RoomModel.RoomModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelModel {
    private String hotelName;
    private List<RoomModel> roomModels;
    private List<HotelHallModel> hotelHallModels;
    private Long areaId;
}
package com.project.alarcha.models.HotelModel;

import com.project.alarcha.models.BaseModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelModel extends BaseModel {
    private String hotelName;
    private MultipartFile multipartFile;
    private String imgName;
    private List<RoomTypeModel> roomTypeModels;
    private List<HotelHallModel> hotelHallModels;
    private Long areaId;
    private String areaName;
}
package com.project.alarcha.models.RoomModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomTypeModel extends BaseModel {
    private String type;
    private Float price;
    private String hotelName;
    private List<RoomModel> roomModels;
    private List<RoomTypeImageModel> roomTypeImageModels;
}

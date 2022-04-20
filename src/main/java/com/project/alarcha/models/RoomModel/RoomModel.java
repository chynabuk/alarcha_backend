package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.RoomStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomModel extends BaseModel {
    private Integer roomNumber;
    private RoomStatus roomStatus;
    private List<RoomOrderModel> roomOrderModels;
    private Integer bedNumber;
    private Long hotelId;
    private String hotelName;
}

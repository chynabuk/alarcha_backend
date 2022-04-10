package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.RoomStatus;
import com.project.alarcha.enums.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomModel {
    private Integer roomNumber;
    private RoomStatus roomStatus;
    private List<RoomOrderModel> roomOrderModels;
    private RoomType roomType;
    private Integer bedNumber;
    private Integer price;
}

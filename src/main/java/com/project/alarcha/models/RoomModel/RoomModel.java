package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.RoomStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomModel extends BaseModel {
    private Long roomTypeId;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private String roomTypeName;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private Integer roomNumber;
    private RoomStatus roomStatus;
    private List<RoomOrderModel> roomOrderModels;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private Integer bedNumber;
    private Long hotelId;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private String hotelName;
    private String img;
}

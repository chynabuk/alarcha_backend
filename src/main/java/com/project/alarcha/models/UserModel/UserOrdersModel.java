package com.project.alarcha.models.UserModel;

import com.project.alarcha.models.BaseModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderBasketModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderBasketModel;
import com.project.alarcha.models.RoomModel.RoomOrderBasketModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserOrdersModel extends BaseModel {
    private List<RoomOrderBasketModel> roomOrderBasketModels;
    private List<ObjectOrderBasketModel> orderBasketModels;
    private List<HotelHallOrderBasketModel> hotelHallOrderBasketModels;

}

package com.project.alarcha.service;

import com.project.alarcha.models.HotelModel.HotelHallOrderModel;

public interface HotelHallOrderService {
    HotelHallOrderModel order(HotelHallOrderModel hotelHallOrderModel);
    HotelHallOrderModel acceptOrder(Long orderId);
}

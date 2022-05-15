package com.project.alarcha.service;

import com.project.alarcha.models.HotelModel.HotelHallOrderModel;

import java.util.List;

public interface HotelHallOrderService {
    HotelHallOrderModel order(HotelHallOrderModel hotelHallOrderModel);
    HotelHallOrderModel acceptOrder(Long orderId);
    HotelHallOrderModel declineOrder(Long orderId);
    List<HotelHallOrderModel> getAll();
    List<HotelHallOrderModel> getInProcessOrders();
    List<HotelHallOrderModel> getConfirmedOrDeclinedOrders();
    HotelHallOrderModel getById(Long id);
    HotelHallOrderModel deleteOrder(Long id);
}

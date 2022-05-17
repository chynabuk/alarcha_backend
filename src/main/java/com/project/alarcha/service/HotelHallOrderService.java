package com.project.alarcha.service;

import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderPayModel;

import java.util.List;

public interface HotelHallOrderService {
    HotelHallOrderModel order(HotelHallOrderModel hotelHallOrderModel);
    HotelHallOrderPayModel pay(HotelHallOrderPayModel hotelHallOrderPayModel);
    HotelHallOrderModel acceptOrder(Long orderId);
    HotelHallOrderModel declineOrder(Long orderId);
    HotelHallOrderModel acceptPayOrder(Long orderId);
    List<HotelHallOrderModel> getAll();
    List<HotelHallOrderModel> getInProcessOrders();
    List<HotelHallOrderModel> getConfirmedOrDeclinedOrders();
    List<HotelHallOrderModel> getInCheckPay();
    List<HotelHallOrderModel> getCheckedPay();
    List<HotelHallOrderModel> convertToModels(List<HotelHallOrder> hotelHallOrders);
    HotelHallOrderModel getById(Long id);
    HotelHallOrderModel deleteOrder(Long id);
}

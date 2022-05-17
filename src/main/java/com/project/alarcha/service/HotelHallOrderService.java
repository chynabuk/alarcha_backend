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
    List<HotelHallOrderModel> getAll(int page);
    List<HotelHallOrderModel> getInProcessOrders(int page);
    List<HotelHallOrderModel> getConfirmedOrDeclinedOrders(int page);
    List<HotelHallOrderModel> getInCheckPay(int page);
    List<HotelHallOrderModel> getCheckedPay(int page);
    List<HotelHallOrderModel> convertToModels(List<HotelHallOrder> hotelHallOrders);
    HotelHallOrderModel getById(Long id);
    HotelHallOrderModel deleteOrder(Long id);
}

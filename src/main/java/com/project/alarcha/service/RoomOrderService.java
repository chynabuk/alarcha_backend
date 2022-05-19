package com.project.alarcha.service;

import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderPayModel;

import java.util.List;

public interface RoomOrderService {
    RoomOrderModel order(RoomOrderModel roomOrderModel);
    RoomOrderPayModel pay(RoomOrderPayModel roomOrderPayModel);
    RoomOrderModel acceptOrder(Long orderId);
    RoomOrderModel declineOrder(Long orderId);
    RoomOrderModel acceptPayOrder(Long orderId);
    List<RoomOrderModel> getAll(int page);
    List<RoomOrderModel> getInProcessOrders(int page);
    List<RoomOrderModel> getConfirmedOrDeclinedOrders(int page);
    List<RoomOrderModel> getInCheckPay(int page);
    List<RoomOrderModel> getCheckedPay(int page);
    List<RoomOrderModel> convertToModels(List<RoomOrder> roomOrders);
    RoomOrderModel getById(Long id);
    RoomOrderModel deleteOrder(Long id);
}

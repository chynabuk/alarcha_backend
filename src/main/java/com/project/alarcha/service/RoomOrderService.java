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
    List<RoomOrderModel> getAll();
    List<RoomOrderModel> getInProcessOrders();
    List<RoomOrderModel> getConfirmedOrDeclinedOrders();
    List<RoomOrderModel> getInCheckPay();
    List<RoomOrderModel> getCheckedPay();
    List<RoomOrderModel> convertToModels(List<RoomOrder> roomOrders);
    RoomOrderModel getById(Long id);
    RoomOrderModel deleteOrder(Long id);
}

package com.project.alarcha.service;

import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.models.OrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderPayModel;

import java.util.List;

public interface RoomOrderService {
    RoomOrderModel order(RoomOrderModel roomOrderModel);
    RoomOrderModel adminOrder(RoomOrderModel roomOrderModel);
    RoomOrderModel update(RoomOrderModel roomOrderModel);
    RoomOrderPayModel pay(RoomOrderPayModel roomOrderPayModel);
    RoomOrderModel acceptOrder(OrderModel orderModel);
    RoomOrderModel declineOrder(OrderModel orderModel);
    RoomOrderModel acceptPayOrder(OrderModel orderModel);
    List<RoomOrderModel> getAll(int page);
    List<RoomOrderModel> getInProcessOrders(int page);
    List<RoomOrderModel> getConfirmedOrDeclinedOrders(int page);
    List<RoomOrderModel> getInCheckPay(int page);
    List<RoomOrderModel> getCheckedPay(int page);
    List<RoomOrderModel> convertToModels(List<RoomOrder> roomOrders);
    RoomOrderModel getById(Long id);
    RoomOrderModel deleteOrder(Long id);

}

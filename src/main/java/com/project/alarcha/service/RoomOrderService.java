package com.project.alarcha.service;

import com.project.alarcha.models.RoomModel.RoomOrderModel;

import java.util.List;

public interface RoomOrderService {
    RoomOrderModel order(RoomOrderModel roomOrderModel);
    RoomOrderModel acceptOrder(Long orderId);
    RoomOrderModel declineOrder(Long orderId);
    List<RoomOrderModel> getAll();
    RoomOrderModel getById(Long id);
    RoomOrderModel deleteOrder(Long id);
}
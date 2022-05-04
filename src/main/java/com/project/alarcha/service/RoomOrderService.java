package com.project.alarcha.service;

import com.project.alarcha.models.RoomModel.RoomOrderModel;

public interface RoomOrderService {
    RoomOrderModel order(RoomOrderModel roomOrderModel);
    RoomOrderModel acceptOrder(Long orderId);
}

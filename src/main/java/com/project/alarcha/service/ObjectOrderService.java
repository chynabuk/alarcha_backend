package com.project.alarcha.service;

import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderPayModel;

import java.util.List;

public interface ObjectOrderService {
    ObjectOrderModel order(ObjectOrderModel objectOrderModel);
    ObjectOrderPayModel pay(ObjectOrderPayModel objectOrderPayModel);
    ObjectOrderModel acceptOrder(Long orderId);
    ObjectOrderModel declineOrder(Long orderId);
    ObjectOrderModel acceptPayOrder(Long orderId);
    List<ObjectOrderModel> getAll();
    List<ObjectOrderModel> getInProcessOrders();
    List<ObjectOrderModel> getConfirmedOrDeclinedOrders();
    List<ObjectOrderModel> convertToModels(List<ObjectOrder> objectOrders);
    ObjectOrderModel getById(Long id);
    ObjectOrderModel deleteOrder(Long orderId);
}

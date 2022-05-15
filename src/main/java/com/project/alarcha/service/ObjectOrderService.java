package com.project.alarcha.service;

import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;

import java.util.List;

public interface ObjectOrderService {
    ObjectOrderModel order(ObjectOrderModel objectOrderModel);
    ObjectOrderModel acceptOrder(Long orderId);
    ObjectOrderModel declineOrder(Long orderId);
    List<ObjectOrderModel> getAll();
    List<ObjectOrderModel> convertToModels(List<ObjectOrder> objectOrders);
    ObjectOrderModel getById(Long id);
    ObjectOrderModel deleteOrder(Long orderId);
}

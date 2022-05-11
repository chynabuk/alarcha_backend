package com.project.alarcha.service;

import com.project.alarcha.models.ObjectModel.ObjectOrderModel;

import java.util.List;

public interface ObjectOrderService {
    ObjectOrderModel order(ObjectOrderModel objectOrderModel);
    ObjectOrderModel acceptOrder(Long orderId);
    ObjectOrderModel declineOrder(Long orderId);
    ObjectOrderModel updateOrder(ObjectOrderModel objectOrderModel);
    List<ObjectOrderModel> getAll();
    ObjectOrderModel getById(Long id);
    ObjectOrderModel deleteOrder(Long orderId);
}

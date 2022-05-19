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
    ObjectOrderModel updateOrder(ObjectOrderModel objectOrderModel);
    ObjectOrderModel acceptPayOrder(Long orderId);
    List<ObjectOrderModel> getAll(int page);
    List<ObjectOrderModel> getInProcessOrders(int page);
    List<ObjectOrderModel> getConfirmedOrDeclinedOrders(int page);
    List<ObjectOrderModel> getInCheckPay(int page);
    List<ObjectOrderModel> getCheckedPay(int page);
    List<ObjectOrderModel> convertToModels(List<ObjectOrder> objectOrders);
    ObjectOrderModel getById(Long id);
    ObjectOrderModel deleteOrder(Long orderId);
}

package com.project.alarcha.service;

import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderPayModel;
import com.project.alarcha.models.OrderModel;

import java.util.List;

public interface ObjectOrderService {
    ObjectOrderModel order(ObjectOrderModel objectOrderModel);
    ObjectOrderPayModel pay(ObjectOrderPayModel objectOrderPayModel);
    ObjectOrderModel acceptOrder(OrderModel orderModel);
    ObjectOrderModel declineOrder(OrderModel orderModel);
    ObjectOrderModel updateOrder(ObjectOrderModel objectOrderModel);
    ObjectOrderModel acceptPayOrder(OrderModel orderModel);
    List<ObjectOrderModel> getAll(int page);
    List<ObjectOrderModel> getInProcessOrders(int page);
    List<ObjectOrderModel> getConfirmedOrDeclinedOrders(int page);
    List<ObjectOrderModel> getInCheckPay(int page);
    List<ObjectOrderModel> getCheckedPay(int page);
    List<ObjectOrderModel> convertToModels(List<ObjectOrder> objectOrders);
    ObjectOrderModel getById(Long id);
    ObjectOrderModel deleteOrder(Long orderId);
}

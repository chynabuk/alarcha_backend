package com.project.alarcha.service;

import com.project.alarcha.models.ObjectModel.ObjectOrderModel;

public interface ObjectOrderService {
    ObjectOrderModel order(ObjectOrderModel objectOrderModel);
    ObjectOrderModel acceptOrder(Long orderId);
}

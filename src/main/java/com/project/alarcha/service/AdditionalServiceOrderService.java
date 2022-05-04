package com.project.alarcha.service;

import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceOrderModel;

public interface AdditionalServiceOrderService {
    AdditionalServiceOrderModel order(AdditionalServiceOrderModel additionalServiceOrderModel);
    AdditionalServiceOrderModel acceptOrder(Long orderId);
}

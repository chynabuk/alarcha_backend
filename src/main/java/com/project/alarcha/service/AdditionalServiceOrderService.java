package com.project.alarcha.service;

import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceOrderModel;

import java.util.List;

public interface AdditionalServiceOrderService {
    AdditionalServiceOrderModel order(AdditionalServiceOrderModel additionalServiceOrderModel);
    AdditionalServiceOrderModel acceptOrder(Long orderId);
    AdditionalServiceOrderModel declineOrder(Long orderId);
    List<AdditionalServiceOrderModel> getAll();
    AdditionalServiceOrderModel getById(Long id);
    AdditionalServiceOrderModel deleteOrder(Long orderId);
}

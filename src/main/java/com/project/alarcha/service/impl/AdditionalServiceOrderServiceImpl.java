package com.project.alarcha.service.impl;

import com.project.alarcha.entities.AdditionalService;
import com.project.alarcha.entities.AdditionalServiceOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.AdditionalServiceOrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceOrderModel;
import com.project.alarcha.repositories.AdditionalServiceOrderRepository;
import com.project.alarcha.service.AdditionalServiceOrderService;
import com.project.alarcha.service.AdditionalServiceService;
import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;

@Service
public class AdditionalServiceOrderServiceImpl implements AdditionalServiceOrderService {
    @Autowired
    private AdditionalServiceOrderRepository additionalServiceOrderRepository;

    @Autowired
    private AdditionalServiceService additionalServiceS;

    @Autowired
    private UserService userService;

    @Override
    public AdditionalServiceOrderModel order(AdditionalServiceOrderModel additionalServiceOrderModel) {
        additionalServiceOrderRepository.save(initAndGetAdditionalServiceOrder(additionalServiceOrderModel));
        return additionalServiceOrderModel;
    }

    @Override
    public AdditionalServiceOrderModel acceptOrder(Long orderId) {
        AdditionalServiceOrder additionalServiceOrder = additionalServiceOrderRepository.getById(orderId);

        if(additionalServiceOrder.getAdditionalServiceOrderStatus() == AdditionalServiceOrderStatus.IN_PROCESS){
            additionalServiceOrder.setAdditionalServiceOrderStatus(AdditionalServiceOrderStatus.CONFIRMED);
        }

        additionalServiceOrderRepository.save(additionalServiceOrder);
        return toModel(additionalServiceOrder);
    }

    private AdditionalServiceOrder initAndGetAdditionalServiceOrder(AdditionalServiceOrderModel additionalServiceOrderModel){
        AdditionalServiceOrder additionalServiceOrder = new AdditionalServiceOrder();

        AdditionalService additionalService = additionalServiceS.getByAdditionalServiceId(additionalServiceOrderModel.getAdditionalServiceId());

        checkStock(additionalService);

        User user = userService.getById(additionalServiceOrderModel.getUserId());

        additionalServiceOrder.setUser(user);
        additionalServiceOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        additionalServiceOrder.setIsDeleted(false);
        additionalServiceOrder.setAdditionalService(additionalService);
        additionalServiceOrder.setRegistrationDate(additionalServiceOrderModel.getRegistrationDate());
        additionalServiceOrder.setStartTime(additionalServiceOrderModel.getStartTime());
        additionalServiceOrder.setEndTime(additionalServiceOrderModel.getEndTime());

        checkTime(additionalServiceOrder);

        additionalServiceOrder.setAdditionalServiceOrderStatus(AdditionalServiceOrderStatus.IN_PROCESS);

        return additionalServiceOrder;
    }

    private void checkTime(AdditionalServiceOrder additionalServiceOrder){
        Time startTime = additionalServiceOrder.getStartTime();
        Time endTime = additionalServiceOrder.getEndTime();

        if(endTime == null){
            Time eTime;
            Time tempTime = new Time(0);
            tempTime.setHours(startTime.getHours() + 4);
            eTime = tempTime;
            additionalServiceOrder.setEndTime(eTime);
        }
        else if(endTime != null){
            int hours = endTime.getHours() - startTime.getHours();

            if(hours < 4){
                throw new ApiFailException("You can't order additional service for less than 4 hours!");
            }
        }
    }

    private void checkStock(AdditionalService additionalService){
        int stock = additionalService.getInStock();

        if(stock >= 1){
            stock--;
            additionalService.setInStock(stock);
        }
        else {
            throw new ApiFailException("The following additional service is not in stock!");
        }
    }

    private AdditionalServiceOrderModel toModel(AdditionalServiceOrder additionalServiceOrder){
        AdditionalServiceOrderModel additionalServiceOrderModel = new AdditionalServiceOrderModel();
        additionalServiceOrderModel.setUserId(additionalServiceOrder.getUser().getId());
        additionalServiceOrderModel.setFullName(additionalServiceOrder.getFullName());
        additionalServiceOrderModel.setAdditionalServiceId(additionalServiceOrder.getAdditionalService().getId());
        additionalServiceOrderModel.setRegistrationDate(additionalServiceOrder.getRegistrationDate());
        additionalServiceOrderModel.setStartTime(additionalServiceOrder.getStartTime());
        additionalServiceOrderModel.setEndTime(additionalServiceOrder.getEndTime());

        return additionalServiceOrderModel;
    }
}

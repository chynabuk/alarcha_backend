package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.enums.ObjectOrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.repositories.ObjectOrderRepository;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.ObjectService;
import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Service
public class ObjectOrderServiceImpl implements ObjectOrderService {
    @Autowired
    private ObjectOrderRepository objectOrderRepository;

    @Autowired
    private ObjectService objectService;

    @Autowired
    private UserService userService;

    @Override
    public ObjectOrderModel order(ObjectOrderModel objectOrderModel) {
        objectOrderRepository.save(initAndGetObjectOrder(objectOrderModel));
        return objectOrderModel;
    }

    @Override
    public ObjectOrderModel acceptOrder(Long orderId) {
        ObjectOrder objectOrder = objectOrderRepository.getById(orderId);

        if(objectOrder.getObjectOrderStatus() == ObjectOrderStatus.IN_PROCESS){
            objectOrder.setObjectOrderStatus(ObjectOrderStatus.CONFIRMED);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    private ObjectOrder initAndGetObjectOrder(ObjectOrderModel objectOrderModel){
        ObjectOrder objectOrder = new ObjectOrder();

        checkObjectOrderTime(objectOrderModel);

        objectOrder.setUser(userService.getById(objectOrderModel.getUserId()));
        objectOrder.setIsDeleted(false);
        Object object = objectService.getByObjectId(objectOrderModel.getObjectId());
        objectOrder.setObject(object);
        objectOrder.setRegistrationDate(objectOrderModel.getRegistrationDate());
        objectOrder.setStartTime(objectOrderModel.getStartTime());
        objectOrder.setEndTime(objectOrderModel.getEndTime());
        objectOrder.setObjectOrderStatus(ObjectOrderStatus.IN_PROCESS);

        return objectOrder;
    }

    private void checkObjectOrderTime(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Time startTime = objectOrderModel.getStartTime();
        Time endTime = objectOrderModel.getEndTime();
        Date registrationDate = objectOrderModel.getRegistrationDate();

        for(ObjectOrder objectOrder : objectOrders){
            if(objectOrderModel.getObjectId() == objectOrder.getObject().getId()){
                if( (registrationDate.getYear() == objectOrder.getRegistrationDate().getYear() && registrationDate.getMonth() == objectOrder.getRegistrationDate().getMonth()
                && registrationDate.getDay() == objectOrder.getRegistrationDate().getDay())
                ){
                    if(objectOrder.getObjectOrderStatus() == ObjectOrderStatus.CONFIRMED){
                        Time rSTime = objectOrder.getStartTime();
                        Time rETime = objectOrder.getEndTime();

                        for(int i = startTime.getHours(); i < endTime.getHours(); i++){
                            if(i >= rSTime.getHours() && i <= rETime.getHours()){
                                throw new ApiFailException("You can't order for this period of time");
                            }
                        }
                    }
                }
            }
        }
    }

    private ObjectOrderModel toModel(ObjectOrder objectOrder){
        ObjectOrderModel objectOrderModel = new ObjectOrderModel();
        objectOrderModel.setUserId(objectOrder.getUser().getId());
        objectOrderModel.setObjectId(objectOrder.getObject().getId());
        objectOrderModel.setRegistrationDate(objectOrder.getRegistrationDate());
        objectOrderModel.setStartTime(objectOrder.getStartTime());
        objectOrderModel.setEndTime(objectOrder.getEndTime());

        return objectOrderModel;
    }
}

package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.entities.User;
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
import java.util.ArrayList;
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

    @Override
    public ObjectOrderModel declineOrder(Long orderId) {
        ObjectOrder objectOrder = objectOrderRepository.getById(orderId);

        if(objectOrder.getObjectOrderStatus() == ObjectOrderStatus.IN_PROCESS){
            objectOrder.setObjectOrderStatus(ObjectOrderStatus.CANCELLED);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    @Override
    public List<ObjectOrderModel> getAll() {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        for(ObjectOrder objectOrder : objectOrderRepository.findAll()){
            if(!objectOrder.getIsDeleted()){
                objectOrderModels.add(toModel(objectOrder));
            }
        }
        return objectOrderModels;
    }

    @Override
    public ObjectOrderModel getById(Long id) {
        ObjectOrder objectOrder = objectOrderRepository.getById(id);

        if(objectOrder == null){
            throw new ApiFailException("Object order is not found!");
        }

        if(objectOrder.getIsDeleted()){
            throw new ApiFailException("Object order is already deleted!");
        }

        return toModel(objectOrder);
    }

    @Override
    public ObjectOrderModel deleteOrder(Long orderId) {
        ObjectOrder objectOrder = objectOrderRepository.getById(orderId);
        if(objectOrder != null){
            if(objectOrder.getIsDeleted()){
                throw new ApiFailException("Object order is already deleted!");
            }
            objectOrder.setIsDeleted(true);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    private ObjectOrder initAndGetObjectOrder(ObjectOrderModel objectOrderModel){
        ObjectOrder objectOrder = new ObjectOrder();

        checkObjectOrderTime(objectOrderModel);

        User user = userService.getById(objectOrderModel.getUserId());

        objectOrder.setUser(user);
        objectOrder.setIsDeleted(false);
        objectOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        Object object = objectService.getByObjectId(objectOrderModel.getObjectId());
        objectOrder.setObject(object);
        objectOrder.setRegistrationDate(objectOrderModel.getRegistrationDate());
        objectOrder.setStartTime(objectOrderModel.getStartTime());
        objectOrder.setEndTime(objectOrderModel.getEndTime());
        objectOrder.setObjectOrderStatus(ObjectOrderStatus.IN_PROCESS);
        objectOrder.setTotalPrice(getTotalPrice(object.getObjectType(), objectOrderModel));

        return objectOrder;
    }

    private void checkObjectOrderTime(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Time startTime = objectOrderModel.getStartTime();
        Time endTime = objectOrderModel.getEndTime();
        Date registrationDate = objectOrderModel.getRegistrationDate();

        for(ObjectOrder objectOrder : objectOrders){
            if(objectOrderModel.getObjectId().equals(objectOrder.getObject().getId())){
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

    private float getTotalPrice(ObjectType objectType, ObjectOrderModel objectOrderModel){
        Time startTime = objectOrderModel.getStartTime();
        Time endTime = objectOrderModel.getEndTime();
        int hours = endTime.getHours() - startTime.getHours();
        float price = objectType.getPrice();
        float pricePerHour = objectType.getPricePerHour();

        float totalPrice = (hours - 1) * pricePerHour + price;

        return totalPrice;
    }

    private ObjectOrderModel toModel(ObjectOrder objectOrder){
        ObjectOrderModel objectOrderModel = new ObjectOrderModel();
        objectOrderModel.setUserId(objectOrder.getUser().getId());
        objectOrderModel.setFullName(objectOrder.getFullName());
        objectOrderModel.setObjectId(objectOrder.getObject().getId());
        objectOrderModel.setRegistrationDate(objectOrder.getRegistrationDate());
        objectOrderModel.setStartTime(objectOrder.getStartTime());
        objectOrderModel.setEndTime(objectOrder.getEndTime());

        return objectOrderModel;
    }
}

package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.ObjectOrderStatus;
import com.project.alarcha.enums.TimeType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.repositories.ObjectOrderRepository;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.ObjectService;
import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
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
            objectOrder.setObjectOrderStatus(ObjectOrderStatus.DECLINED);
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

        User user = userService.getById(objectOrderModel.getUserId());
        Object object = objectService.getByObjectId(objectOrderModel.getObjectId());
        ObjectType objectType = object.getObjectType();

        objectOrder.setUser(user);
        objectOrder.setIsDeleted(false);
        objectOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        objectOrder.setObject(object);
        objectOrder.setObjectOrderStatus(ObjectOrderStatus.IN_PROCESS);

        if(objectType.getTimeType() == TimeType.TIME){
            checkObjectOrderTime(objectOrderModel);

            Time startTime = objectOrderModel.getStartTime();
            Time endTime = objectOrderModel.getEndTime();
            Float price = objectType.getPrice();
            Float pricePerHour = objectType.getPricePerHour();

            objectOrder.setRegistrationDate(objectOrderModel.getRegistrationDate());
            objectOrder.setStartTime(objectOrderModel.getStartTime());
            objectOrder.setEndTime(objectOrderModel.getEndTime());
            objectOrder.setTotalPrice(getTotalPriceByTime(price, pricePerHour, startTime, endTime));
        }
        else if(objectType.getTimeType() == TimeType.DATE){
            checkObjectOrderDate(objectOrderModel);

            Float price = object.getObjectType().getPrice();
            Date startDate = objectOrderModel.getStartDate();
            Date endDate = objectOrderModel.getEndDate();

            objectOrder.setStartDate(objectOrderModel.getStartDate());
            objectOrder.setEndDate(objectOrderModel.getEndDate());
            objectOrder.setTotalPrice(getTotalPriceByDate(price, startDate, endDate));
        }

        return objectOrder;
    }

    private void checkObjectOrderTime(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        LocalTime currentTime = LocalTime.now();

        Time startTime = objectOrderModel.getStartTime();
        Time endTime = objectOrderModel.getEndTime();
        Date registrationDate = objectOrderModel.getRegistrationDate();

        if(startTime == null || endTime == null){
            throw new ApiFailException("Time must not be null!");
        }

        if (endTime.compareTo(startTime) <= 0){
            throw new ApiFailException("endTime must be greater than startTime");
        }

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

    private void checkObjectOrderDate(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Date currentDate = new Date();

        Date startDate = objectOrderModel.getStartDate();
        Date endDate = objectOrderModel.getEndDate();

        if (startDate == null || endDate == null){
            throw new ApiFailException("dates must not be null");
        }

        if (endDate.compareTo(startDate) <= 0){
            throw new ApiFailException("endDate must be greater than startDate");
        }

        if (startDate.compareTo(currentDate) < 0){
            throw new ApiFailException("start date can not be less than currentDate");
        }


        for (ObjectOrder objectOrder : objectOrders){
            if (objectOrderModel.getObjectId() == objectOrder.getObject().getId()){
                if (objectOrder.getObjectOrderStatus() == ObjectOrderStatus.CONFIRMED){
                    Date rSDate = objectOrder.getStartDate();
                    Date rEDate = objectOrder.getEndDate();
                    if (
                            (startDate.getYear() == rSDate.getYear() && startDate.getMonth() == rSDate.getMonth())
                                    && (endDate.getYear() == rEDate.getYear() && endDate.getMonth() == rEDate.getMonth())
                    )
                    {
                        for (int i = startDate.getDate(); i < endDate.getDate(); i++){
                            if (i >= rSDate.getDate() && i <= rEDate.getDate()){
                                throw new ApiFailException("You can't order for this date");
                            }
                        }
                    }
                }
            }
        }
    }

    private Float getTotalPriceByTime(Float price, Float pricePerHour, Time startTime, Time endTime){
        int hours = endTime.getHours() - startTime.getHours();
        return (hours - 1) * pricePerHour + price;
    }

    private Float getTotalPriceByDate(Float price, Date startDate, Date endDate){
        return (endDate.getDate() - startDate.getDate()) * price;
    }

    private ObjectOrderModel toModel(ObjectOrder objectOrder){
        ObjectOrderModel objectOrderModel = new ObjectOrderModel();
        objectOrderModel.setUserId(objectOrder.getUser().getId());
        objectOrderModel.setFullName(objectOrder.getFullName());
        objectOrderModel.setObjectId(objectOrder.getObject().getId());

        ObjectType objectType = objectOrder.getObject().getObjectType();

        if(objectType.getTimeType() == TimeType.TIME){
            objectOrderModel.setRegistrationDate(objectOrder.getRegistrationDate());
            objectOrderModel.setStartTime(objectOrder.getStartTime());
            objectOrderModel.setEndTime(objectOrder.getEndTime());
        }
        else if(objectType.getTimeType() == TimeType.DATE){
            objectOrderModel.setStartDate(objectOrder.getStartDate());
            objectOrderModel.setEndDate(objectOrder.getEndDate());
        }

        return objectOrderModel;
    }
}

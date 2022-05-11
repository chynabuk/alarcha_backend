package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
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
        ObjectOrder objectOrder = getObjectOrder(orderId);

        if(objectOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            objectOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    @Override
    public ObjectOrderModel declineOrder(Long orderId) {
        ObjectOrder objectOrder = getObjectOrder(orderId);

        if(objectOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            objectOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    @Override
    public ObjectOrderModel updateOrder(ObjectOrderModel objectOrderModel) {
        ObjectOrder objectOrder = getObjectOrder(objectOrderModel.getId());

        setValuesOnUpdateObjectOrder(objectOrder, objectOrderModel);

        objectOrderRepository.save(objectOrder);

        return objectOrderModel;
    }

    @Override
    public List<ObjectOrderModel> getAll() {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        for(ObjectOrder objectOrder : objectOrderRepository.findAll()){
            if(!objectOrder.getIsDeleted()){
                if(isExpired(objectOrder.getExpirationDate())){
                    objectOrder.setIsDeleted(true);
                    objectOrderRepository.save(objectOrder);
                }
            }
            if(!objectOrder.getIsDeleted()){
                objectOrderModels.add(toModel(objectOrder));
            }
        }
        return objectOrderModels;
    }

    @Override
    public ObjectOrderModel getById(Long id) {
        ObjectOrder objectOrder = getObjectOrder(id);

        return toModel(objectOrder);
    }

    @Override
    public ObjectOrderModel deleteOrder(Long orderId) {
        ObjectOrder objectOrder = getObjectOrder(orderId);

        objectOrder.setIsDeleted(true);

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
        objectOrder.setOrderStatus(OrderStatus.IN_PROCESS);

        Date expirationDate = new Date();

        if(objectType.getTimeType() == TimeType.TIME){
            checkObjectOrderTime(objectOrderModel, objectType);

            Date startDate = objectOrderModel.getStartDate();
            Time startTime = objectOrderModel.getStartTime();
            Time endTime = objectOrderModel.getEndTime();
            Float price = objectType.getPrice();
            Float pricePerHour = objectType.getPricePerHour();

            expirationDate.setDate(startDate.getDate() + 3);

            objectOrder.setStartDate(startDate);
            objectOrder.setEndDate(startDate);
            objectOrder.setExpirationDate(expirationDate);
            objectOrder.setStartTime(startTime);
            objectOrder.setEndTime(endTime);
            objectOrder.setTotalPrice(getTotalPriceByTime(price, pricePerHour, startTime, endTime));
        }
        else if(objectType.getTimeType() == TimeType.DATE){
            checkObjectOrderDate(objectOrderModel);

            Float price = object.getObjectType().getPrice();
            Date startDate = objectOrderModel.getStartDate();
            Date endDate = objectOrderModel.getEndDate();

            expirationDate.setDate(startDate.getDate() + 5);

            objectOrder.setStartDate(startDate);
            objectOrder.setEndDate(endDate);
            objectOrder.setExpirationDate(expirationDate);
            objectOrder.setTotalPrice(getTotalPriceByDate(price, startDate, endDate));
        }

        return objectOrder;
    }

    private ObjectOrder getObjectOrder(Long objectOrderId){
        ObjectOrder objectOrder = objectOrderRepository
                .findById(objectOrderId)
                .orElseThrow(() -> new ApiFailException("ObjectOrder is not found!"));

        if(isExpired(objectOrder.getExpirationDate())){
            objectOrder.setIsDeleted(true);
            objectOrderRepository.save(objectOrder);
        }

        if(objectOrder.getIsDeleted()){
            throw new ApiFailException("ObjectOrder is not found or deleted!");
        }

        return objectOrder;
    }

    private boolean isExpired(Date expiredDate){
        Date currentDate = new Date();

        return currentDate.after(expiredDate);
    }

    private void checkObjectOrderTime(ObjectOrderModel objectOrderModel, ObjectType objectType){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Time startTime = objectOrderModel.getStartTime();
        Time endTime = objectOrderModel.getEndTime();
        Date registrationDate = objectOrderModel.getStartDate();
        Integer minHours = objectType.getMinHours();
        Time currentTime = Time.valueOf(LocalTime.now());

        checkTimeForNull(startTime, endTime, registrationDate);

        registrationDate.setHours(0);
        registrationDate.setMinutes(0);
        registrationDate.setSeconds(0);

        Date currentDate = getCurrentDate();

        compareRegistrationDateWithCurrentDate(registrationDate, currentDate);

        compareEndTimeWithStartTime(startTime, endTime);

        compareStartTimeWithCurrentTime(registrationDate, currentDate, startTime, currentTime);

        checkMinHours(startTime, endTime, minHours);

        doTimeValidation(objectOrders, objectOrderModel, registrationDate, startTime, endTime);
    }

    private void checkObjectOrderDate(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Date currentDate = getCurrentDate();

        Date startDate = objectOrderModel.getStartDate();
        Date endDate = objectOrderModel.getEndDate();

        checkDateForNull(startDate, endDate);

        startDate.setHours(12);
        endDate.setHours(12);

        compareStartWithCurrentDate(startDate, currentDate);

        compareEndWithStartDate(startDate, endDate);

        doDatesValidation(objectOrders, objectOrderModel, startDate, endDate);
    }

    private Date getCurrentDate(){
        Date currentDate = new Date();
        currentDate.setTime(0);
        Date tempDate = new Date();

        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(0);
        currentDate.setMinutes(0);
        currentDate.setSeconds(0);

        return currentDate;
    }

    private Float getTotalPriceByTime(Float price, Float pricePerHour, Time startTime, Time endTime){
        int hours = endTime.getHours() - startTime.getHours();
        return (hours - 1) * pricePerHour + price;
    }

    private Float getTotalPriceByDate(Float price, Date startDate, Date endDate){
        return (endDate.getDate() - startDate.getDate()) * price;
    }

    private void checkMinHours(Time startTime, Time endTime, Integer minHours){
        int hours = endTime.getHours() - startTime.getHours();
        if(hours < minHours){
            throw new ApiFailException("You can not order this ObjectType for less than " + minHours + " hours.");
        }
    }

    private void checkDateForNull(Date startDate, Date endDate){
        if (startDate == null || endDate == null){
            throw new ApiFailException("Dates must not be null");
        }
    }

    private void checkTimeForNull(Time startTime, Time endTime, Date registrationDate){
        if(startTime == null || endTime == null || registrationDate == null){
            throw new ApiFailException("Time or registration date must not be null!");
        }
    }

    private void compareStartTimeWithCurrentTime(Date registrationDate, Date currentDate, Time startTime, Time currentTime){
        if(registrationDate.equals(currentDate) && startTime.before(currentTime)){
            throw new ApiFailException("startTime can not be less than currentTime");
        }
    }

    private void compareEndTimeWithStartTime(Time startTime, Time endTime){
        if (endTime.compareTo(startTime) <= 0){
            throw new ApiFailException("endTime must be greater than startTime");
        }
    }

    private void compareRegistrationDateWithCurrentDate(Date registrationDate, Date currentDate){
        if(registrationDate.before(currentDate)){
            throw new ApiFailException("RegistrationDate can not be less than currentDate");
        }
    }

    private void compareStartWithCurrentDate(Date startDate, Date currentDate){
        if (startDate.compareTo(currentDate) < 0){
            throw new ApiFailException("start date can not be less than currentDate");
        }
    }

    private void compareEndWithStartDate(Date startDate, Date endDate){
        if (endDate.compareTo(startDate) <= 0){
            throw new ApiFailException("endDate must be greater than startDate");
        }
    }

    private void doTimeValidation(List<ObjectOrder> objectOrders, ObjectOrderModel objectOrderModel, Date registrationDate, Time startTime, Time endTime){
        for(ObjectOrder objectOrder : objectOrders){
            if(objectOrderModel.getObjectId().equals(objectOrder.getObject().getId())){
                if( (registrationDate.getYear() == objectOrder.getStartDate().getYear() && registrationDate.getMonth() == objectOrder.getStartDate().getMonth()
                        && registrationDate.getDay() == objectOrder.getStartDate().getDay())
                ){
                    if(objectOrder.getOrderStatus() == OrderStatus.CONFIRMED){
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

    private void doDatesValidation(List<ObjectOrder> objectOrders, ObjectOrderModel objectOrderModel, Date startDate, Date endDate){
        for (ObjectOrder objectOrder : objectOrders){
            if (objectOrderModel.getObjectId() == objectOrder.getObject().getId()){
                if (objectOrder.getOrderStatus() == OrderStatus.CONFIRMED){
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

    private void setValuesOnUpdateObjectOrder(ObjectOrder objectOrder, ObjectOrderModel objectOrderModel){
        Object object = objectService.getByObjectId(objectOrder.getObject().getId());
        ObjectType objectType = object.getObjectType();

        Date expirationDate = new Date();
        Float price = objectType.getPrice();

        if (objectOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            if (objectType.getTimeType() == TimeType.TIME){
                Time startTime = objectOrderModel.getStartTime();
                Time endTime = objectOrderModel.getEndTime();
                Date registrationDate = objectOrderModel.getStartDate();

                if(startTime != null){
                    objectOrder.setStartTime(startTime);
                }

                if(endTime != null){
                    objectOrder.setEndTime(endTime);
                }

                if(registrationDate != null){
                    objectOrder.setStartDate(registrationDate);
                    objectOrder.setEndDate(registrationDate);
                }

                List<ObjectOrder> objectOrders = objectOrderRepository.findAll();
                startTime = objectOrder.getStartTime();
                endTime = objectOrder.getEndTime();
                registrationDate = objectOrder.getStartDate();

                Time currentTime = Time.valueOf(LocalTime.now());
                Integer minHours = objectType.getMinHours();
                Float pricePerHour = objectType.getPricePerHour();

                checkTimeForNull(startTime, endTime, registrationDate);

                registrationDate.setHours(0);
                registrationDate.setMinutes(0);
                registrationDate.setSeconds(0);

                Date currentDate = getCurrentDate();

                compareRegistrationDateWithCurrentDate(registrationDate, currentDate);
                compareEndTimeWithStartTime(startTime, endTime);
                compareStartTimeWithCurrentTime(registrationDate, currentDate, startTime, currentTime);
                checkMinHours(startTime, endTime, minHours);
                doTimeValidation(objectOrders, toModel(objectOrder), registrationDate, startTime, endTime);

                expirationDate.setDate(registrationDate.getDate() + 3);
                objectOrder.setExpirationDate(expirationDate);
                objectOrder.setTotalPrice(getTotalPriceByTime(price, pricePerHour, startTime, endTime));

            }
            else if(objectType.getTimeType() == TimeType.DATE){
                Date startDate = objectOrderModel.getStartDate();
                Date endDate = objectOrderModel.getEndDate();

                if(startDate!= null){
                    objectOrder.setStartDate(startDate);
                }

                if(endDate != null){
                    objectOrder.setEndDate(endDate);
                }

                List<ObjectOrder> objectOrders = objectOrderRepository.findAll();
                Date currentDate = getCurrentDate();

                startDate = objectOrder.getStartDate();
                endDate = objectOrder.getEndDate();

                checkDateForNull(startDate, endDate);

                startDate.setHours(12);
                endDate.setHours(12);
                expirationDate.setDate(startDate.getDate() + 5);

                compareStartWithCurrentDate(startDate, currentDate);
                compareEndWithStartDate(startDate, endDate);
                doDatesValidation(objectOrders, toModel(objectOrder), startDate, endDate);

                objectOrder.setExpirationDate(expirationDate);
                objectOrder.setTotalPrice(getTotalPriceByDate(price, startDate, endDate));
            }
        }

    }

    private ObjectOrderModel toModel(ObjectOrder objectOrder){
        ObjectOrderModel objectOrderModel = new ObjectOrderModel();
        objectOrderModel.setUserId(objectOrder.getUser().getId());
        objectOrderModel.setFullName(objectOrder.getFullName());
        objectOrderModel.setObjectId(objectOrder.getObject().getId());
        objectOrderModel.setStartDate(objectOrder.getStartDate());
        objectOrderModel.setEndDate(objectOrder.getEndDate());

        ObjectType objectType = objectOrder.getObject().getObjectType();

        if(objectType.getTimeType() == TimeType.TIME){
            objectOrderModel.setStartTime(objectOrder.getStartTime());
            objectOrderModel.setEndTime(objectOrder.getEndTime());
        }

        return objectOrderModel;
    }
}

package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.entities.Object;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.enums.TimeType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderPayModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.repositories.ObjectOrderRepository;
import com.project.alarcha.repositories.ObjectRepository;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.ObjectService;
import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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
    private ObjectRepository objectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public ObjectOrderModel order(ObjectOrderModel objectOrderModel) {
        objectOrderRepository.save(initAndGetObjectOrder(objectOrderModel));
        return objectOrderModel;
    }

    @Override
    public ObjectOrderPayModel pay(ObjectOrderPayModel objectOrderPayModel) {
        ObjectOrder objectOrder = objectOrderRepository.getById(objectOrderPayModel.getObjectOrderId());

        if (objectOrder.getOrderStatus() == OrderStatus.CONFIRMED){
            if (!objectOrderPayModel.getImg().isEmpty() || objectOrderPayModel.getImg() != null){
                objectOrder.setImgOfCheck(objectOrderPayModel.getImg().getBytes(StandardCharsets.UTF_8));
                objectOrder.setOrderStatus(OrderStatus.CHECK_CHECK);
                objectOrderRepository.save(objectOrder);
            }
        }
        return objectOrderPayModel;
    }

    @Override
    public ObjectOrderModel acceptOrder(Long orderId) {
        ObjectOrder objectOrder = objectOrderRepository.getById(orderId);

        if(objectOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            objectOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    @Override
    public ObjectOrderModel declineOrder(Long orderId) {
        ObjectOrder objectOrder = objectOrderRepository.getById(orderId);

        if(
                objectOrder.getOrderStatus() == OrderStatus.IN_PROCESS
                || objectOrder.getOrderStatus() == OrderStatus.CONFIRMED
                || objectOrder.getOrderStatus() == OrderStatus.CHECK_CHECK
                || objectOrder.getOrderStatus() == OrderStatus.PAID
        ){
            objectOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        objectOrderRepository.save(objectOrder);
        return toModel(objectOrder);
    }

    @Override
    public ObjectOrderModel acceptPayOrder(Long orderId) {
        ObjectOrder objectOrder = objectOrderRepository.getById(orderId);

        if (objectOrder.getOrderStatus() == OrderStatus.CHECK_CHECK){
            objectOrder.setOrderStatus(OrderStatus.PAID);
            objectOrderRepository.save(objectOrder);
        }

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
    public List<ObjectOrderModel> getInProcessOrders() {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        for (ObjectOrder objectOrder : objectOrderRepository.findAll()){
            if (!objectOrder.getIsDeleted()){
                if (objectOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
                    objectOrderModels.add(toModel(objectOrder));
                }
            }
        }

        return objectOrderModels;
    }

    @Override
    public List<ObjectOrderModel> getConfirmedOrDeclinedOrders() {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        for (ObjectOrder objectOrder : objectOrderRepository.findAll()){
            if (!objectOrder.getIsDeleted()){
                if (objectOrder.getOrderStatus() == OrderStatus.CONFIRMED || objectOrder.getOrderStatus() == OrderStatus.DECLINED){
                    objectOrderModels.add(toModel(objectOrder));
                }
            }
        }

        return objectOrderModels;
    }

    @Override
    public List<ObjectOrderModel> getInCheckPay() {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        for (ObjectOrder objectOrder : objectOrderRepository.findAll()){
            if (!objectOrder.getIsDeleted()){
                if (objectOrder.getOrderStatus() == OrderStatus.CHECK_CHECK){
                    objectOrderModels.add(toModel(objectOrder));
                }
            }
        }

        return objectOrderModels;
    }

    @Override
    public List<ObjectOrderModel> getCheckedPay() {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        for (ObjectOrder objectOrder : objectOrderRepository.findAll()){
            if (!objectOrder.getIsDeleted()){
                if (objectOrder.getOrderStatus() == OrderStatus.PAID){
                    objectOrderModels.add(toModel(objectOrder));
                }
            }
        }

        return objectOrderModels;
    }

    @Override
    public List<ObjectOrderModel> convertToModels(List<ObjectOrder> objectOrders) {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();
        objectOrders.forEach(objectOrder -> {
            if (!objectOrder.getIsDeleted()) {
                if (objectOrder.getOrderStatus() == OrderStatus.CONFIRMED){
                    objectOrderModels.add(toModel(objectOrder));
                }
            }
        });
        return objectOrderModels;
    }

    @Override
    public ObjectOrderModel getById(Long id) {
        ObjectOrder objectOrder = objectOrderRepository.getById(id);

        if(objectOrder == null){
            throw new ApiFailException("Object order is not found!");
        }

        if(objectOrder.getIsDeleted()){
            throw new ApiFailException("Object order is deleted!");
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
        Object object = objectRepository.findById(objectOrderModel.getObjectId())
                .orElseThrow(() -> new ApiFailException("object is not found"));
        ObjectType objectType = object.getObjectType();

        objectOrder.setUser(user);
        objectOrder.setIsDeleted(false);
        objectOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        objectOrder.setObject(object);
        objectOrder.setOrderStatus(OrderStatus.IN_PROCESS);

        if(objectType.getTimeType() == TimeType.TIME){
            checkObjectOrderTime(objectOrderModel);

            objectOrder.setStartDate(objectOrderModel.getStartDate());
            objectOrder.setEndDate(objectOrderModel.getStartDate());

            Time startTime = objectOrderModel.getStartTime();
            Time endTime = objectOrderModel.getEndTime();
            Float price = objectType.getPrice();
            Float pricePerHour = objectType.getPricePerHour();

            objectOrder.setStartTime(startTime);
            objectOrder.setEndTime(endTime);
            objectOrder.setTotalPrice(getTotalPriceByTime(price, pricePerHour, startTime, endTime));
        }
        else if(objectType.getTimeType() == TimeType.DATE){
            checkObjectOrderDate(objectOrderModel);

            Float price = object.getObjectType().getPrice();
            Date startDate = objectOrderModel.getStartDate();
            Date endDate = objectOrderModel.getEndDate();

            objectOrder.setStartDate(startDate);
            objectOrder.setEndDate(endDate);
            objectOrder.setTotalPrice(getTotalPriceByDate(price, startDate, endDate));
        }

        emailSenderService.sendEmail(
                object.getObjectType().getArea().getUser().getEmail(),
                "Новая бронь объекта",
                "от " + objectOrder.getFullName() + " поступил запрос на бронирование \n" +
                        "http://localhost:8080/admin/book-object");

        return objectOrder;
    }

    private void checkObjectOrderTime(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Time currentTime = Time.valueOf(LocalTime.now());

        Time startTime = objectOrderModel.getStartTime();
        Time endTime = objectOrderModel.getEndTime();
        Date registrationDate = objectOrderModel.getStartDate();

        if(startTime == null || endTime == null || registrationDate == null){
            throw new ApiFailException("Time or registration date must not be null!");
        }

        registrationDate.setHours(12);
        registrationDate.setMinutes(0);
        registrationDate.setSeconds(0);

        Date currentDate = new Date();
        currentDate.setTime(0);
        Date tempDate = new Date();

        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(12);
        currentDate.setMinutes(0);
        currentDate.setSeconds(0);

        if(registrationDate.before(currentDate)){
            throw new ApiFailException("RegistrationDate can not be less than currentDate");
        }

        if (endTime.compareTo(startTime) <= 0){
            throw new ApiFailException("endTime must be greater than startTime");
        }

        if(registrationDate.equals(currentDate) && startTime.before(currentTime)){
            throw new ApiFailException("startTime can not be less than currentTime");
        }

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

    private void checkObjectOrderDate(ObjectOrderModel objectOrderModel){
        List<ObjectOrder> objectOrders = objectOrderRepository.findAll();

        Date currentDate = new Date();
        currentDate.setTime(0);
        Date tempDate = new Date();
        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(tempDate.getHours());

        Date startDate = objectOrderModel.getStartDate();
        Date endDate = objectOrderModel.getEndDate();

        if (startDate == null || endDate == null){
            throw new ApiFailException("Dates must not be null");
        }

        startDate.setHours(12);
        endDate.setHours(12);

        if (endDate.compareTo(startDate) <= 0){
            throw new ApiFailException("endDate must be greater than startDate");
        }

        if (startDate.compareTo(currentDate) < 0){
            throw new ApiFailException("start date can not be less than currentDate");
        }

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

    private Float getTotalPriceByTime(Float price, Float pricePerHour, Time startTime, Time endTime){
        int hours = endTime.getHours() - startTime.getHours();
        return (hours - 1) * pricePerHour + price;
    }

    private Float getTotalPriceByDate(Float price, Date startDate, Date endDate){
        return (endDate.getDate() - startDate.getDate()) * price;
    }

    private ObjectOrderModel toModel(ObjectOrder objectOrder){
        ObjectOrderModel objectOrderModel = new ObjectOrderModel();
        objectOrderModel.setId(objectOrder.getId());
        objectOrderModel.setUserId(objectOrder.getUser().getId());
        objectOrderModel.setFullName(objectOrder.getFullName());
        objectOrderModel.setObjectId(objectOrder.getObject().getId());
        objectOrderModel.setObjectName(objectOrder.getObject().getName());
        objectOrderModel.setObjectTypeName(objectOrder.getObject().getObjectType().getName());
        objectOrderModel.setStartDate(objectOrder.getStartDate());
        objectOrderModel.setEndDate(objectOrder.getEndDate());
        objectOrderModel.setUserPhone(objectOrder.getUser().getPhone());
        objectOrderModel.setOrderStatus(objectOrder.getOrderStatus());
        objectOrderModel.setTotalPrice(objectOrder.getTotalPrice());

        ObjectType objectType = objectOrder.getObject().getObjectType();

        if(objectType.getTimeType() == TimeType.TIME){
            objectOrderModel.setStartTime(objectOrder.getStartTime());
            objectOrderModel.setEndTime(objectOrder.getEndTime());
        }

        if (objectOrder.getImgOfCheck() != null){
            objectOrderModel.setImg(new String(objectOrder.getImgOfCheck(), StandardCharsets.UTF_8));
        }

        return objectOrderModel;
    }
}

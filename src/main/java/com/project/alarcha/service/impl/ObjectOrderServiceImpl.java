package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.entities.Object;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.enums.TimeType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderPayModel;
import com.project.alarcha.repositories.ObjectOrderRepository;
import com.project.alarcha.repositories.ObjectRepository;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ObjectOrderModel updateOrder(ObjectOrderModel objectOrderModel) {
        ObjectOrder objectOrder = getObjectOrder(objectOrderModel.getId());

        setValuesOnUpdateObjectOrder(objectOrder, objectOrderModel);

        objectOrderRepository.save(objectOrder);

        return objectOrderModel;
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
    public List<ObjectOrderModel> getAll(int page) {
        Page<ObjectOrder> objectOrders = objectOrderRepository.getAll(PageRequest.of(page, 10));
        return getModelListFrom(objectOrders);
    }

    @Override
    public List<ObjectOrderModel> getInProcessOrders(int page) {
        Page<ObjectOrder> objectOrders = objectOrderRepository.getInProcessOrders(PageRequest.of(page, 10));
        return getModelListFrom(objectOrders);
    }

    @Override
    public List<ObjectOrderModel> getConfirmedOrDeclinedOrders(int page) {
        Page<ObjectOrder> objectOrders = objectOrderRepository.getConfirmedOrDeclinedOrders(PageRequest.of(page, 10));
        return getModelListFrom(objectOrders);
    }

    @Override
    public List<ObjectOrderModel> getInCheckPay(int page) {
        Page<ObjectOrder> objectOrders = objectOrderRepository.getInCheckPay(PageRequest.of(page, 10));
        return getModelListFrom(objectOrders);
    }

    @Override
    public List<ObjectOrderModel> getCheckedPay(int page) {
        Page<ObjectOrder> objectOrders = objectOrderRepository.getCheckedPay(PageRequest.of(page, 10));
        return getModelListFrom(objectOrders);
    }

    @Override
    public List<ObjectOrderModel> convertToModels(List<ObjectOrder> objectOrders) {
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();
        objectOrders.forEach(objectOrder -> {
            if (!objectOrder.getIsDeleted()) {
                if (
                        objectOrder.getOrderStatus() == OrderStatus.CONFIRMED
                        ||objectOrder.getOrderStatus() == OrderStatus.CHECK_CHECK
                        ||objectOrder.getOrderStatus() == OrderStatus.PAID
                ){
                    objectOrderModels.add(toModel(objectOrder));
                }
            }
        });
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
        objectOrder.setDeletedDate(new Date());

        objectOrderRepository.save(objectOrder);

        return toModel(objectOrder);
    }

    private ObjectOrder initAndGetObjectOrder(ObjectOrderModel objectOrderModel){
        ObjectOrder objectOrder = new ObjectOrder();

        User user = userService.getById(objectOrderModel.getUserId());
        Object object = objectRepository.findById(objectOrderModel.getObjectId())
                .orElseThrow(() -> new ApiFailException("Обьект не найден."));
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

        emailSenderService.sendEmail(
                object.getObjectType().getArea().getUser().getEmail(),
                "Новая бронь объекта",
                "от " + objectOrder.getFullName() + " поступил запрос на бронирование \n" +
                        "http://localhost:8080/admin/book-object");

        return objectOrder;
    }

    private ObjectOrder getObjectOrder(Long objectOrderId){
        ObjectOrder objectOrder = objectOrderRepository
                .findById(objectOrderId)
                .orElseThrow(() -> new ApiFailException("Заказ объкта не найден."));

        if(isExpired(objectOrder.getExpirationDate())){
            objectOrder.setIsDeleted(true);
            objectOrderRepository.save(objectOrder);
        }

        if(objectOrder.getIsDeleted()){
            throw new ApiFailException("Заказ объекта не найден или удален.");
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

        registrationDate.setHours(12);
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
        currentDate.setHours(12);
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
            throw new ApiFailException("Вы не можете заказать этот тип объета меньше чем на " + minHours + " часа.");
        }
    }

    private void checkDateForNull(Date startDate, Date endDate){
        if (startDate == null || endDate == null){
            throw new ApiFailException("Даты не должны быть пустыми.");
        }
    }

    private void checkTimeForNull(Time startTime, Time endTime, Date registrationDate){
        if(startTime == null || endTime == null || registrationDate == null){
            throw new ApiFailException("Время или дата регистрации не должны быть пустыми.");
        }
    }

    private void compareStartTimeWithCurrentTime(Date registrationDate, Date currentDate, Time startTime, Time currentTime){
        if(registrationDate.equals(currentDate) && startTime.before(currentTime)){
            throw new ApiFailException("Время начала не может быть раньше текущего времени.");
        }
    }

    private void compareEndTimeWithStartTime(Time startTime, Time endTime){
        if (endTime.compareTo(startTime) <= 0){
            throw new ApiFailException("Время окончания не может быть раньше времени начала.");
        }
    }

    private void compareRegistrationDateWithCurrentDate(Date registrationDate, Date currentDate){
        if(registrationDate.before(currentDate)){
            throw new ApiFailException("Дата регистрации не может быть раньше текущей даты.");
        }
    }

    private void compareStartWithCurrentDate(Date startDate, Date currentDate){
        if (startDate.compareTo(currentDate) < 0){
            throw new ApiFailException("Дата начала не может быть раньше текущей даты.");
        }
    }

    private void compareEndWithStartDate(Date startDate, Date endDate){
        if (endDate.compareTo(startDate) <= 0){
            throw new ApiFailException("Дата окончания не может быть раньше даты начала.");
        }
    }

    private void doTimeValidation(List<ObjectOrder> objectOrders, ObjectOrderModel objectOrderModel, Date registrationDate, Time startTime, Time endTime){
        for(ObjectOrder objectOrder : objectOrders){
            if(objectOrderModel.getObjectId().equals(objectOrder.getObject().getId())){
                if( (registrationDate.getYear() == objectOrder.getStartDate().getYear() && registrationDate.getMonth() == objectOrder.getStartDate().getMonth()
                        && registrationDate.getDay() == objectOrder.getStartDate().getDay())
                ){
                    OrderStatus orderStatus = objectOrder.getOrderStatus();
                    if(orderStatus == OrderStatus.CONFIRMED || orderStatus == OrderStatus.CHECK_CHECK || orderStatus == OrderStatus.PAID){
                        Time rSTime = objectOrder.getStartTime();
                        Time rETime = objectOrder.getEndTime();

                        for(int i = startTime.getHours(); i < endTime.getHours(); i++){
                            if(i >= rSTime.getHours() && i <= rETime.getHours()){
                                throw new ApiFailException("Вы не можете сделать заказ в этот промежуток вермени.");
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
                OrderStatus orderStatus = objectOrder.getOrderStatus();
                if (orderStatus == OrderStatus.CONFIRMED || orderStatus == OrderStatus.CHECK_CHECK || orderStatus == OrderStatus.PAID){
                    Date rSDate = objectOrder.getStartDate();
                    Date rEDate = objectOrder.getEndDate();
                    if (
                            (startDate.getYear() == rSDate.getYear() && startDate.getMonth() == rSDate.getMonth())
                                    && (endDate.getYear() == rEDate.getYear() && endDate.getMonth() == rEDate.getMonth())
                    )
                    {
                        for (int i = startDate.getDate(); i < endDate.getDate(); i++){
                            if (i >= rSDate.getDate() && i <= rEDate.getDate()){
                                throw new ApiFailException("Вы не можете сделать заказ на эту дату.");
                            }
                        }
                    }
                }
            }
        }
    }

    private void setValuesOnUpdateObjectOrder(ObjectOrder objectOrder, ObjectOrderModel objectOrderModel){
        Object object = objectRepository
                .findById(objectOrder.getObject().getId())
                .orElseThrow(() -> new ApiFailException("Объект не найден."));

        if(object.getIsDeleted()){
            throw new ApiFailException("Объект не найден или удален.");
        }

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

                registrationDate.setHours(12);
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

    private List<ObjectOrderModel> getModelListFrom(Page<ObjectOrder> objectOrders){
        List<ObjectOrderModel> objectOrderModels = new ArrayList<>();

        int countExpiredOrder = 0;
        for (ObjectOrder objectOrder : objectOrders){
            if (isExpired(objectOrder.getExpirationDate())){
                objectOrder.setIsDeleted(true);
                objectOrder.setDeletedDate(new Date());
                objectOrderRepository.save(objectOrder);
                countExpiredOrder++;
            }
            objectOrderModels.add(toModel(objectOrder));
        }

        if (countExpiredOrder > 0){
            throw new ApiFailException("Обновите страницу");
        }

        ObjectOrderModel objectOrderModel = new ObjectOrderModel();
        objectOrderModel.setTotalPage(objectOrders.getTotalPages());
        objectOrderModels.add(objectOrderModel);

        return objectOrderModels;
    }
}

package com.project.alarcha.service.impl;

import com.project.alarcha.entities.AdditionalService;
import com.project.alarcha.entities.AdditionalServiceOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceOrderModel;
import com.project.alarcha.repositories.AdditionalServiceOrderRepository;
import com.project.alarcha.service.AdditionalServiceOrderService;
import com.project.alarcha.service.AdditionalServiceService;
import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        AdditionalServiceOrder additionalServiceOrder = getAdditionalServiceOrder(orderId);

        if(additionalServiceOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            additionalServiceOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        additionalServiceOrderRepository.save(additionalServiceOrder);
        return toModel(additionalServiceOrder);
    }

    @Override
    public AdditionalServiceOrderModel declineOrder(Long orderId) {
        AdditionalServiceOrder additionalServiceOrder = getAdditionalServiceOrder(orderId);

        if(additionalServiceOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            additionalServiceOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        additionalServiceOrderRepository.save(additionalServiceOrder);
        return toModel(additionalServiceOrder);
    }

    @Override
    public List<AdditionalServiceOrderModel> getAll() {
        List<AdditionalServiceOrderModel> additionalServiceOrderModels = new ArrayList<>();

        for(AdditionalServiceOrder additionalServiceOrder : additionalServiceOrderRepository.findAll()){
            if(!additionalServiceOrder.getIsDeleted()){
                if(isExpired(additionalServiceOrder.getExpirationDate())){
                    additionalServiceOrder.setIsDeleted(true);
                    additionalServiceOrderRepository.save(additionalServiceOrder);
                }
            }
            if(!additionalServiceOrder.getIsDeleted()){
                additionalServiceOrderModels.add(toModel(additionalServiceOrder));
            }
        }

        return additionalServiceOrderModels;
    }

    @Override
    public AdditionalServiceOrderModel getById(Long id) {
        AdditionalServiceOrder additionalServiceOrder = getAdditionalServiceOrder(id);

        return toModel(additionalServiceOrder);
    }

    @Override
    public AdditionalServiceOrderModel deleteOrder(Long orderId) {
        AdditionalServiceOrder additionalServiceOrder = getAdditionalServiceOrder(orderId);

        additionalServiceOrder.setIsDeleted(true);

        additionalServiceOrderRepository.save(additionalServiceOrder);

        return toModel(additionalServiceOrder);
    }

    private AdditionalServiceOrder getAdditionalServiceOrder(Long id){
        AdditionalServiceOrder additionalServiceOrder = additionalServiceOrderRepository
                .findById(id)
                .orElseThrow(() -> new ApiFailException("Заказ дополнительных услуг не найден."));

        if(isExpired(additionalServiceOrder.getExpirationDate())){
            additionalServiceOrder.setIsDeleted(true);
            additionalServiceOrderRepository.save(additionalServiceOrder);
        }

        if(additionalServiceOrder.getIsDeleted()){
            throw new ApiFailException("Заказ дополнительных услуг не найден или удален.");
        }

        return additionalServiceOrder;
    }

    private boolean isExpired(Date expiredDate){
        Date currentDate = new Date();

        return currentDate.after(expiredDate);
    }

    private AdditionalServiceOrder initAndGetAdditionalServiceOrder(AdditionalServiceOrderModel additionalServiceOrderModel){
        AdditionalServiceOrder additionalServiceOrder = new AdditionalServiceOrder();

        AdditionalService additionalService = additionalServiceS.getByAdditionalServiceId(additionalServiceOrderModel.getAdditionalServiceId());
        User user = userService.getById(additionalServiceOrderModel.getUserId());

        checkStock(additionalService);
        checkAdditionalServiceOrderTime(additionalServiceOrderModel);

        Float price = additionalService.getPrice();
        Float priceForNextHours = additionalService.getPriceForNextHours();
        Time startTime = additionalServiceOrderModel.getStartTime();
        Time endTime = additionalServiceOrderModel.getEndTime();

        additionalServiceOrder.setUser(user);
        additionalServiceOrder.setFullName(user.getFirstName() + " " + user.getLastName());
        additionalServiceOrder.setAdditionalService(additionalService);
        additionalServiceOrder.setRegistrationDate(additionalServiceOrderModel.getRegistrationDate());
        additionalServiceOrder.setStartTime(startTime);
        additionalServiceOrder.setEndTime(endTime);

        Date expirationDate = new Date();
        Date registrationDate = additionalServiceOrder.getRegistrationDate();
        expirationDate.setDate(registrationDate.getDate() + 3);
        additionalServiceOrder.setExpirationDate(expirationDate);

        additionalServiceOrder.setTotalPrice(getTotalPrice(price, priceForNextHours, startTime, endTime));
        additionalServiceOrder.setOrderStatus(OrderStatus.IN_PROCESS);
        additionalServiceOrder.setIsDeleted(false);

        return additionalServiceOrder;
    }

    private void checkStock(AdditionalService additionalService){
        int stock = additionalService.getInStock();

        if(stock >= 1){
            stock--;
            additionalService.setInStock(stock);
        }
        else {
            throw new ApiFailException("Этой дополнительной услуги нет в наличии.");
        }
    }

    private Float getTotalPrice(Float price, Float priceForNextHours, Time startTime, Time endTime){
        int hours = endTime.getHours() - startTime.getHours();

        return (hours - 1) * priceForNextHours + price;
    }

    private void checkAdditionalServiceOrderTime(AdditionalServiceOrderModel additionalServiceOrderModel){
        Time currentTime = Time.valueOf(LocalTime.now());

        Time startTime = additionalServiceOrderModel.getStartTime();
        Time endTime = additionalServiceOrderModel.getEndTime();
        Date registrationDate = additionalServiceOrderModel.getRegistrationDate();

        if(startTime == null || registrationDate == null){
            throw new ApiFailException("Время и дата регистрации не должны быть пустыми.");
        }

        registrationDate.setHours(0);
        registrationDate.setMinutes(0);
        registrationDate.setSeconds(0);

        Date currentDate = new Date();
        currentDate.setTime(0);
        Date tempDate = new Date();

        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(0);
        currentDate.setMinutes(0);
        currentDate.setSeconds(0);

        if(registrationDate.before(currentDate)){
            throw new ApiFailException("Дата регитсрации не может быть раньше текущей даты.");
        }

        if(registrationDate.equals(currentDate) && startTime.before(currentTime)){
            throw new ApiFailException("Время начала не может быть раньше текущего времени.");
        }

        if(endTime == null){
            Time eTime;
            Time tempTime = new Time(0);
            tempTime.setHours(startTime.getHours() + 4);
            eTime = tempTime;
            additionalServiceOrderModel.setEndTime(eTime);
        }
        else if(endTime != null){
            if (endTime.compareTo(startTime) <= 0){
                throw new ApiFailException("Время окончания не может быть раньше времени начала");
            }

            int hours = endTime.getHours() - startTime.getHours();

            if(hours < 4){
                throw new ApiFailException("Вы не можете заказать дополнительную услугу меньше чем на 4 часа!");
            }
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

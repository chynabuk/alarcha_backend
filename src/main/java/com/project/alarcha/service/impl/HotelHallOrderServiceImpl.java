package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderPayModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.repositories.HotelHallOrderRepository;
import com.project.alarcha.repositories.HotelHallsRepository;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.HotelHallOrderService;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class HotelHallOrderServiceImpl implements HotelHallOrderService {
    @Autowired
    private HotelHallOrderRepository hotelHallOrderRepository;

    @Autowired
    private HotelHallsRepository hotelHallsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public HotelHallOrderModel order(HotelHallOrderModel hotelHallOrderModel) {
        HotelHallOrder hotelHallOrder = initAndGetHotelHallOrder(hotelHallOrderModel);
        hotelHallOrderRepository.save(hotelHallOrder);
        return toModel(hotelHallOrder);
    }

    @Override
    public HotelHallOrderPayModel pay(HotelHallOrderPayModel hotelHallOrderPayModel) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(hotelHallOrderPayModel.getHotelHallOrderId());
        if (hotelHallOrder.getOrderStatus() == OrderStatus.CONFIRMED){
            if (!hotelHallOrderPayModel.getImg().isEmpty() || hotelHallOrderPayModel.getImg() != null){
                hotelHallOrder.setImgOfCheck(hotelHallOrderPayModel.getImg().getBytes(StandardCharsets.UTF_8));
                hotelHallOrder.setOrderStatus(OrderStatus.CHECK_CHECK);
                hotelHallOrderRepository.save(hotelHallOrder);
            }
        }
        return hotelHallOrderPayModel;
    }

    @Override
    public HotelHallOrderModel acceptOrder(Long orderId) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(orderId);

        if(hotelHallOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            hotelHallOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        hotelHallOrderRepository.save(hotelHallOrder);
        return toModel(hotelHallOrder);
    }

    @Override
    public HotelHallOrderModel declineOrder(Long orderId) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(orderId);

        if(
                hotelHallOrder.getOrderStatus() == OrderStatus.IN_PROCESS
                || hotelHallOrder.getOrderStatus() == OrderStatus.CONFIRMED
                || hotelHallOrder.getOrderStatus() == OrderStatus.CHECK_CHECK
                || hotelHallOrder.getOrderStatus() == OrderStatus.PAID
        ){
            hotelHallOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        hotelHallOrderRepository.save(hotelHallOrder);
        return toModel(hotelHallOrder);
    }

    @Override
    public HotelHallOrderModel acceptPayOrder(Long orderId) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(orderId);

        if (hotelHallOrder.getOrderStatus() == OrderStatus.CHECK_CHECK){
            hotelHallOrder.setOrderStatus(OrderStatus.PAID);
            hotelHallOrderRepository.save(hotelHallOrder);
        }

        return toModel(hotelHallOrder);
    }

    @Override
    public HotelHallOrderModel deleteOrder(Long id) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(id);

        hotelHallOrderRepository.save(hotelHallOrder);

        return toModel(hotelHallOrder);
    }

    @Override
    public List<HotelHallOrderModel> getAll(int page) {
        Page<HotelHallOrder> hotelHallOrders = hotelHallOrderRepository.getAll(PageRequest.of(page, 10));
        return getModelListFrom(hotelHallOrders);
    }

    @Override
    public List<HotelHallOrderModel> getInProcessOrders(int page) {
        Page<HotelHallOrder> hotelHallOrders = hotelHallOrderRepository.getInProcessOrders(PageRequest.of(page, 10));
        return getModelListFrom(hotelHallOrders);
    }

    @Override
    public List<HotelHallOrderModel> getConfirmedOrDeclinedOrders(int page) {
        Page<HotelHallOrder> hotelHallOrders = hotelHallOrderRepository.getConfirmedOrDeclinedOrders(PageRequest.of(page, 10));
        return getModelListFrom(hotelHallOrders);
    }

    @Override
    public List<HotelHallOrderModel> getInCheckPay(int page) {
        Page<HotelHallOrder> hotelHallOrders = hotelHallOrderRepository.getInCheckPay(PageRequest.of(page, 10));
        return getModelListFrom(hotelHallOrders);
    }

    @Override
    public List<HotelHallOrderModel> getCheckedPay(int page) {
        Page<HotelHallOrder> hotelHallOrders = hotelHallOrderRepository.getCheckedPay(PageRequest.of(page, 10));
        return getModelListFrom(hotelHallOrders);
    }

    @Override
    public HotelHallOrderModel getById(Long id) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(id);

        return toModel(hotelHallOrder);
    }

    @Override
    public List<HotelHallOrderModel> convertToModels(List<HotelHallOrder> hotelHallOrders) {
        List<HotelHallOrderModel> hotelHallOrderModels = new ArrayList<>();
        hotelHallOrders.forEach(hotelHallOrder -> {
            if (!hotelHallOrder.getIsDeleted()) {
                if (hotelHallOrder.getOrderStatus() == OrderStatus.CONFIRMED){
                    hotelHallOrderModels.add(toModel(hotelHallOrder));
                }
            }
        });

        return hotelHallOrderModels;
    }

    private HotelHallOrder getHotelHallOrder(Long id){
        HotelHallOrder hotelHallOrder = hotelHallOrderRepository
                .findById(id)
                .orElseThrow(() -> new ApiFailException("HotelHall order not found"));

        if (isExpired(hotelHallOrder.getExpirationDate())){
            hotelHallOrder.setIsDeleted(true);
            hotelHallOrderRepository.save(hotelHallOrder);
        }

        if (hotelHallOrder.getIsDeleted()){
            throw new ApiFailException("HotelHall order is not found or deleted");
        }

        return hotelHallOrder;
    }

    private HotelHallOrder initAndGetHotelHallOrder(HotelHallOrderModel hotelHallOrderModel){
        HotelHallOrder hotelHallOrder = new HotelHallOrder();

        checkHotelHallOrderTime(hotelHallOrderModel);

        User user = userService.getById(hotelHallOrderModel.getUserId());
        hotelHallOrder.setUser(user);

        HotelHall hotelHall = hotelHallsRepository.findById(hotelHallOrderModel.getHotelHallId())
                .orElseThrow(() -> new ApiFailException("HotelHall is not found"));
        Float price = hotelHall.getPrice();
        Float priceForNextHours = hotelHall.getPriceForNextHours();

        hotelHallOrder.setHotelHall(hotelHall);
        Date startDate = hotelHallOrderModel.getStartDate();
        hotelHallOrder.setStartDate(startDate);
        hotelHallOrder.setStartTime(hotelHallOrderModel.getStartTime());
        hotelHallOrder.setEndTime(hotelHallOrderModel.getEndTime());
        hotelHallOrder.setEndDate(startDate);

        Date expirationDate = new Date();
        expirationDate.setDate(startDate.getDate() + 3);
        hotelHallOrder.setExpirationDate(expirationDate);
        hotelHallOrder.setUserFullName(user.getFirstName() + " " + user.getLastName());

        hotelHallOrder.setTotalPrice(getTotalPrice(price, priceForNextHours, hotelHallOrderModel));
        hotelHallOrder.setOrderStatus(OrderStatus.IN_PROCESS);
        hotelHallOrder.setIsDeleted(false);

        emailSenderService.sendEmail(
                hotelHall.getHotel().getArea().getUser().getEmail(),
                "Новая бронь доп комнаты",
                "от " + hotelHallOrder.getUserFullName() + " поступил запрос на бронирование \n" +
                        "http://localhost:8080/admin/book-hall");

        return hotelHallOrder;
    }

    private boolean isExpired(Date expiredDate){
        Date currentDate = new Date();

        return currentDate.after(expiredDate);
    }

    private void checkHotelHallOrderTime(HotelHallOrderModel hotelHallOrderModel){
        List<HotelHallOrder> hotelHallOrderList = hotelHallOrderRepository.findAll();

        Time startTime = hotelHallOrderModel.getStartTime();
        Time endTime = hotelHallOrderModel.getEndTime();
        Date startDate = hotelHallOrderModel.getStartDate();

        startDate.setHours(12);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        Date currentDate = new Date();
        currentDate.setTime(0);

        Date tempDate = new Date();

        Time currentTime = Time.valueOf(LocalTime.now());


        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(12);
        currentDate.setMinutes(0);
        currentDate.setSeconds(0);

        if (startTime == null || endTime == null || startDate == null){
            throw new ApiFailException("startTime or endTime or startDate can not be null");
        }

        if (startDate.before(currentDate)){
            throw new ApiFailException("StartDate can not be less than currentDate");
        }

        if (endTime.compareTo(startTime) <= 0){
            throw new ApiFailException("endTime must be greater than startTime");
        }

        if (startDate.equals(currentDate) && startTime.compareTo(currentTime) < 0){
            throw new ApiFailException("startTime can not be less than currentTime");
        }


        for(HotelHallOrder hotelHallOrder : hotelHallOrderList){
            if(hotelHallOrderModel.getHotelHallId() == hotelHallOrder.getHotelHall().getId()){
                if( (startDate.getYear() == hotelHallOrder.getStartDate().getYear() && startDate.getMonth() == hotelHallOrder.getStartDate().getMonth()
                        && startDate.getDate() == hotelHallOrder.getStartDate().getDate())
                ){
                    OrderStatus orderStatus = hotelHallOrder.getOrderStatus();
                    if(orderStatus == OrderStatus.CONFIRMED || orderStatus == OrderStatus.CHECK_CHECK || orderStatus == OrderStatus.PAID){
                        Time rSTime = hotelHallOrder.getStartTime();
                        Time rETime = hotelHallOrder.getEndTime();

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


    private Float getTotalPrice(Float price, Float priceForNextHours, HotelHallOrderModel hotelHallOrderModel){
        Time startTime = hotelHallOrderModel.getStartTime();
        Time endTime = hotelHallOrderModel.getEndTime();

        Integer hours = endTime.getHours() - startTime.getHours();
        if (hours <= 0){
            throw new ApiFailException("can not get totalPrice");
        }
        else {
            return price + (hours - 1) * priceForNextHours;
        }
    }

    private HotelHallOrderModel toModel(HotelHallOrder hotelHallOrder){
        HotelHallOrderModel hotelHallOrderModel = new HotelHallOrderModel();
        hotelHallOrderModel.setId(hotelHallOrder.getId());
        hotelHallOrderModel.setHotelHallId(hotelHallOrder.getId());
        hotelHallOrderModel.setStartTime(hotelHallOrder.getStartTime());
        hotelHallOrderModel.setEndTime(hotelHallOrder.getEndTime());
        hotelHallOrderModel.setStartDate(hotelHallOrder.getStartDate());
        hotelHallOrderModel.setUserId(hotelHallOrder.getUser().getId());
        hotelHallOrderModel.setUserFullName(hotelHallOrder.getUserFullName());
        hotelHallOrderModel.setOrderStatus(hotelHallOrder.getOrderStatus());
        hotelHallOrderModel.setHotelName(hotelHallOrder.getHotelHall().getHotel().getHotelName());
        hotelHallOrderModel.setHotelHallName(hotelHallOrder.getHotelHall().getName());
        hotelHallOrderModel.setUserPhone(hotelHallOrder.getUser().getPhone());
        hotelHallOrderModel.setTotalPrice(hotelHallOrder.getTotalPrice());
        if (hotelHallOrder.getImgOfCheck() != null){
            hotelHallOrderModel.setImg(new String(hotelHallOrder.getImgOfCheck(), StandardCharsets.UTF_8));
        }
        return hotelHallOrderModel;
    }

    private List<HotelHallOrderModel> getModelListFrom(Page<HotelHallOrder> hotelHallOrders){
        List<HotelHallOrderModel> hotelHallOrderModels = new ArrayList<>();

        int countExpiredOrder = 0;
        for (HotelHallOrder hotelHallOrder : hotelHallOrders){
            if (isExpired(hotelHallOrder.getExpirationDate())){
                hotelHallOrder.setIsDeleted(true);
                hotelHallOrderRepository.save(hotelHallOrder);
                countExpiredOrder++;
            }
            hotelHallOrderModels.add(toModel(hotelHallOrder));
        }

        if (countExpiredOrder > 0){
            throw new ApiFailException("Обновите страницу");
        }

        HotelHallOrderModel hotelHallOrderModel = new HotelHallOrderModel();
        hotelHallOrderModel.setTotalPage(hotelHallOrders.getTotalPages());
        hotelHallOrderModels.add(hotelHallOrderModel);

        return hotelHallOrderModels;
    }
}

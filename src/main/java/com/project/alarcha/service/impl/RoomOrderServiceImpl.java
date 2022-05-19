package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderPayModel;
import com.project.alarcha.repositories.RoomOrderRepository;
import com.project.alarcha.repositories.RoomRepository;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.RoomOrderService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RoomOrderServiceImpl implements RoomOrderService {
    @Autowired
    private RoomOrderRepository roomOrderRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public RoomOrderModel order(RoomOrderModel roomOrderModel) {
        RoomOrder roomOrder = initAndGetRoomOrder(roomOrderModel);
        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel update(RoomOrderModel roomOrderModel) {
        RoomOrder roomOrder = getRoomOrder(roomOrderModel.getId());

        return null;
    }

    @Override
    public RoomOrderPayModel pay(RoomOrderPayModel roomOrderPayModel) {
        RoomOrder roomOrder = getRoomOrder(roomOrderPayModel.getRoomOrderId());

        if (roomOrder.getOrderStatus() == OrderStatus.CONFIRMED){
            if (!roomOrderPayModel.getImg().isEmpty() || roomOrderPayModel.getImg() != null){
                roomOrder.setImgOfCheck(roomOrderPayModel.getImg().getBytes(StandardCharsets.UTF_8));
                roomOrder.setOrderStatus(OrderStatus.CHECK_CHECK);
                roomOrderRepository.save(roomOrder);
            }
        }

        return roomOrderPayModel;
    }

    @Override
    public RoomOrderModel acceptOrder(Long orderId) {
        RoomOrder roomOrder = getRoomOrder(orderId);

        if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            roomOrder.setOrderStatus(OrderStatus.CONFIRMED);
            roomOrderRepository.save(roomOrder);
        }

        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel declineOrder(Long orderId) {
        RoomOrder roomOrder = getRoomOrder(orderId);

        if (
                roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS
                || roomOrder.getOrderStatus() == OrderStatus.CONFIRMED
                || roomOrder.getOrderStatus() == OrderStatus.CHECK_CHECK
                || roomOrder.getOrderStatus() == OrderStatus.PAID
        ){
            roomOrder.setOrderStatus(OrderStatus.DECLINED);
            roomOrderRepository.save(roomOrder);
        }

        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel acceptPayOrder(Long orderId) {
        RoomOrder roomOrder = getRoomOrder(orderId);

        if (roomOrder.getOrderStatus() == OrderStatus.CHECK_CHECK){
            roomOrder.setOrderStatus(OrderStatus.PAID);
            roomOrderRepository.save(roomOrder);
        }

        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel deleteOrder(Long id) {
        RoomOrder roomOrder = getRoomOrder(id);

        roomOrder.setIsDeleted(true);

        roomOrderRepository.save(roomOrder);

        return toModel(roomOrder);
    }

    @Override
    public List<RoomOrderModel> getAll(int page) {
        Page<RoomOrder> roomOrders = roomOrderRepository.getAll(PageRequest.of(page, 10));
        return getModelListFrom(roomOrders);
    }

    @Override
    public List<RoomOrderModel> getInProcessOrders(int page) {
        Page<RoomOrder> roomOrders = roomOrderRepository.getInProcessOrders(PageRequest.of(page, 10));
        return getModelListFrom(roomOrders);
    }

    @Override
    public List<RoomOrderModel> getConfirmedOrDeclinedOrders(int page) {
        Page<RoomOrder> roomOrders = roomOrderRepository.getConfirmedOrDeclinedOrders(PageRequest.of(page, 10));
        return getModelListFrom(roomOrders);
    }

    @Override
    public List<RoomOrderModel> getInCheckPay(int page) {
        Page<RoomOrder> roomOrders = roomOrderRepository.getInCheckPay(PageRequest.of(page, 10));
        return getModelListFrom(roomOrders);
    }

    @Override
    public List<RoomOrderModel> getCheckedPay(int page) {
        Page<RoomOrder> roomOrders = roomOrderRepository.getCheckedPay(PageRequest.of(page, 10));
        return getModelListFrom(roomOrders);
    }

    @Override
    public List<RoomOrderModel> convertToModels(List<RoomOrder> roomOrders) {
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();
        roomOrders.forEach(roomOrder -> {
            if (!roomOrder.getIsDeleted()) {
                if (
                        roomOrder.getOrderStatus() == OrderStatus.CONFIRMED
                        || roomOrder.getOrderStatus() == OrderStatus.CHECK_CHECK
                        || roomOrder.getOrderStatus() == OrderStatus.PAID
                )
                    roomOrderModels.add(toModel(roomOrder));
            }
        });
        return roomOrderModels;
    }

    private boolean isExpired(Date expiredDate){
        Date currentDate = new Date();

        return currentDate.after(expiredDate);
    }


    @Override
    public RoomOrderModel getById(Long id) {
        RoomOrder roomOrder = getRoomOrder(id);

        return toModel(roomOrder);
    }

    private RoomOrder getRoomOrder(Long id){
        RoomOrder roomOrder = roomOrderRepository.findById(id)
                .orElseThrow(() -> new ApiFailException("Заказ комнаты не найден."));

        if (roomOrder.getIsDeleted()){
            throw new ApiFailException("Заказ комнаты не найден или удален.");
        }

        return roomOrder;
    }

    private RoomOrder initAndGetRoomOrder(RoomOrderModel roomOrderModel){
        RoomOrder roomOrder = new RoomOrder();

        checkRoomOrderDate(roomOrderModel);

        User user = userService.getById(roomOrderModel.getUserId());
        roomOrder.setUser(user);
        Room room = roomRepository.findById(roomOrderModel.getRoomId())
                .orElseThrow(() -> new ApiFailException("Комната не найдена."));
        roomOrder.setIsDeleted(false);
        roomOrder.setRoom(room);
        roomOrder.setUserFullName(user.getFirstName() + " " + user.getLastName());

        Float price = room.getRoomType().getPrice();
        Date startDate = roomOrderModel.getStartDate();
        Date endDate = roomOrderModel.getEndDate();

        roomOrder.setStartDate(startDate);
        roomOrder.setEndDate(endDate);
        roomOrder.setExpirationDate(setAndGetExpirationDate(startDate));
        roomOrder.setTotalPrice(getTotalPrice(price, startDate, endDate));
        roomOrder.setOrderStatus(OrderStatus.IN_PROCESS);


        emailSenderService.sendEmail(
                room.getRoomType().getHotel().getArea().getUser().getEmail(),
                "Новая бронь комнаты",
                "от " + roomOrder.getUserFullName() + " поступил запрос на бронирование \n" +
                        "http://localhost:8080/admin/book-room");

        return roomOrder;
    }

    private Date setAndGetExpirationDate(Date startDate){
        Date expirationDate = new Date();
        expirationDate.setDate(startDate.getDate() + 3);

        return expirationDate;
    }

    private Float getTotalPrice(Float price, Date startDate, Date endDate){
        return (endDate.getDate() - startDate.getDate()) * price;
    }

    private void checkRoomOrderDate(RoomOrderModel roomOrderModel){
        List<RoomOrder> roomOrders = roomOrderRepository.getAllRoomOrders();

        Date currentDate = new Date();
        Date tempDate = new Date();
        currentDate.setTime(0);
        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(12);

        Date startDate = roomOrderModel.getStartDate();
        startDate.setHours(12);

        Date endDate = roomOrderModel.getEndDate();
        endDate.setHours(12);

        if (startDate == null || endDate == null){
            throw new ApiFailException("Даты не должны быть пустыми.");
        }

        if (endDate.compareTo(startDate) <= 0){
            throw new ApiFailException("Дата окончания не может быть раньше даты начала.");
        }

        if (startDate.compareTo(currentDate) < 0){
            throw new ApiFailException("Дата начала не может быть раньше текущей даты.");
        }


        for (RoomOrder roomOrder : roomOrders){
            if (roomOrderModel.getRoomId() == roomOrder.getRoom().getId()){
                OrderStatus orderStatus = roomOrder.getOrderStatus();
                if (orderStatus == OrderStatus.CONFIRMED || orderStatus == OrderStatus.CHECK_CHECK || orderStatus == OrderStatus.PAID){
                    Date rSDate = roomOrder.getStartDate();
                    Date rEDate = roomOrder.getEndDate();
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

    private RoomOrderModel toModel(RoomOrder roomOrder){
        RoomOrderModel roomOrderModel = new RoomOrderModel();
        roomOrderModel.setId(roomOrder.getId());
        roomOrderModel.setUserId(roomOrder.getUser().getId());
        roomOrderModel.setRoomId(roomOrder.getId());
        roomOrderModel.setStartDate(roomOrder.getStartDate());
        roomOrderModel.setEndDate(roomOrder.getEndDate());
        roomOrderModel.setUserFullName(roomOrder.getUserFullName());
        roomOrderModel.setOrderStatus(roomOrder.getOrderStatus());
        roomOrderModel.setHotelName(roomOrder.getRoom().getRoomType().getHotel().getHotelName());
        roomOrderModel.setRoomNumber(roomOrder.getRoom().getRoomNumber());
        roomOrderModel.setRoomType(roomOrder.getRoom().getRoomType().getType());
        roomOrderModel.setUserPhone(roomOrder.getUser().getPhone());
        roomOrderModel.setTotalPrice(roomOrder.getTotalPrice());

        if (roomOrder.getImgOfCheck() != null){
            roomOrderModel.setImg(new String(roomOrder.getImgOfCheck(), StandardCharsets.UTF_8));
        }

        return roomOrderModel;
    }

    private List<RoomOrderModel> getModelListFrom(Page<RoomOrder> roomOrders){
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();

        int countExpiredOrder = 0;
        for (RoomOrder roomOrder : roomOrders){
            if (isExpired(roomOrder.getExpirationDate())){
                roomOrder.setIsDeleted(true);
                roomOrderRepository.save(roomOrder);
                countExpiredOrder++;
            }
            roomOrderModels.add(toModel(roomOrder));
        }

        if (countExpiredOrder > 0){
            throw new ApiFailException("Обновите страницу");
        }

        RoomOrderModel roomOrderModel = new RoomOrderModel();
        roomOrderModel.setTotalPage(roomOrders.getTotalPages());
        roomOrderModels.add(roomOrderModel);

        return roomOrderModels;
    }

}
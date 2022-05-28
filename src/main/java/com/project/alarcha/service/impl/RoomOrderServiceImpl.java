package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.enums.UserRole;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.OrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderPayModel;
import com.project.alarcha.repositories.RoomOrderRepository;
import com.project.alarcha.repositories.RoomRepository;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.RoomOrderService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        roomOrder.setOrderStatus(OrderStatus.IN_PROCESS);
        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel adminOrder(RoomOrderModel roomOrderModel) {
        RoomOrder roomOrder = initAndGetRoomOrder(roomOrderModel);
        roomOrder.setOrderStatus(OrderStatus.PAID);
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
    public RoomOrderModel acceptOrder(OrderModel orderModel) {
        RoomOrder roomOrder = getRoomOrder(orderModel.getId());

        if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            roomOrder.setOrderStatus(OrderStatus.CONFIRMED);
            roomOrderRepository.save(roomOrder);
        }

        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel declineOrder(OrderModel orderModel) {
        RoomOrder roomOrder = getRoomOrder(orderModel.getId());

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
    public RoomOrderModel acceptPayOrder(OrderModel orderModel) {
        RoomOrder roomOrder = getRoomOrder(orderModel.getId());

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
        roomOrder.setDeletedDate(new Date());

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

    private boolean isExpired(LocalDate expiredDate){
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(expiredDate);
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

        User user = userService.getById(roomOrderModel.getUserId());
        Room room = roomRepository.findById(roomOrderModel.getRoomId())
                .orElseThrow(() -> new ApiFailException("Комната не найдена."));


        Float price = room.getRoomType().getPrice();
        LocalDate startDate = roomOrderModel.getStartDate();
        LocalDate endDate = roomOrderModel.getEndDate();

        long difference = ChronoUnit.DAYS.between(startDate, endDate);
        checkRoomOrderDate(roomOrderModel, difference);
        roomOrder.setIsDeleted(false);
        roomOrder.setUser(user);
        roomOrder.setRoom(room);
        roomOrder.setUserFullName(user.getFirstName() + " " + user.getLastName());
        roomOrder.setStartDate(startDate);
        roomOrder.setEndDate(endDate);
        roomOrder.setExpirationDate(setAndGetExpirationDate(startDate));
        roomOrder.setTotalPrice(getTotalPrice(price, difference));

        emailSenderService.sendEmail(
                room.getRoomType().getHotel().getArea().getUser().getEmail(),
                "Новая бронь комнаты",
                "от " + roomOrder.getUserFullName() + " поступил запрос на бронирование \n" +
                        "http://localhost:8080/admin/book-room");

        return roomOrder;
    }

    private LocalDate setAndGetExpirationDate(LocalDate startDate){
        return startDate.plusDays(3);
    }

    private Float getTotalPrice(Float price, long differnce){
        return differnce * price;
    }

    private void checkRoomOrderDate(RoomOrderModel roomOrderModel, long difference){
        List<RoomOrder> roomOrders = roomOrderRepository.getAllRoomOrders();

        LocalDate currentDate = LocalDate.now();

        LocalDate startDate = roomOrderModel.getStartDate();

        LocalDate endDate = roomOrderModel.getEndDate();

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
                    LocalDate rSDate = roomOrder.getStartDate();
                    LocalDate rEDate = roomOrder.getEndDate();
                    if (
                            (startDate.getYear() == rSDate.getYear() && startDate.getMonth() == rSDate.getMonth())
                                    && (endDate.getYear() == rEDate.getYear() && endDate.getMonth() == rEDate.getMonth())
                    )
                    {
                        LocalDate startLocalDate = startDate;
                        for (long i = 0; i < difference; i++){
                            if(startLocalDate.getDayOfMonth() >= rSDate.getDayOfMonth() && startLocalDate.getDayOfMonth() <= rEDate.getDayOfMonth()){
                                throw new ApiFailException("Вы не можете сделать заказ на эту дату.");
                            }
                            startLocalDate.plusDays(++i);
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
                roomOrder.setDeletedDate(new Date());
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
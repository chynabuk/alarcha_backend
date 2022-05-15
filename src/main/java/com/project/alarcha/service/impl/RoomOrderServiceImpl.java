package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.repositories.RoomOrderRepository;
import com.project.alarcha.repositories.RoomRepository;
import com.project.alarcha.service.RoomOrderService;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public RoomOrderModel order(RoomOrderModel roomOrderModel) {
        RoomOrder roomOrder = initAndGetRoomOrder(roomOrderModel);
        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel acceptOrder(Long orderId) {
        RoomOrder roomOrder = getRoomOrder(orderId);

        if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            roomOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel declineOrder(Long orderId) {
        RoomOrder roomOrder = getRoomOrder(orderId);

        if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            roomOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        roomOrderRepository.save(roomOrder);
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
    public List<RoomOrderModel> getAll() {
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();

        for (RoomOrder roomOrder : roomOrderRepository.findAll()){
            if (!roomOrder.getIsDeleted()){
                if (isExpired(roomOrder.getExpirationDate())){
                    roomOrder.setIsDeleted(true);
                    roomOrderRepository.save(roomOrder);
                }
            }
            if (!roomOrder.getIsDeleted()){
                roomOrderModels.add(toModel(roomOrder));
            }
        }
        return roomOrderModels;
    }

    @Override
    public List<RoomOrderModel> getInProcessOrders() {
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();

        for (RoomOrder roomOrder : roomOrderRepository.findAll()){
            if (!roomOrder.getIsDeleted()){
                if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
                    roomOrderModels.add(toModel(roomOrder));
                }
            }
        }

        return roomOrderModels;
    }

    @Override
    public List<RoomOrderModel> getConfirmedOrDeclinedOrders() {
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();

        for (RoomOrder roomOrder : roomOrderRepository.findAll()){
            if (!roomOrder.getIsDeleted()){
                if (roomOrder.getOrderStatus() == OrderStatus.CONFIRMED || roomOrder.getOrderStatus() == OrderStatus.DECLINED){
                    roomOrderModels.add(toModel(roomOrder));
                }
            }
        }

        return roomOrderModels;
    }

    @Override
    public List<RoomOrderModel> convertToModels(List<RoomOrder> roomOrders) {
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();
        roomOrders.forEach(roomOrder -> {
            if (!roomOrder.getIsDeleted()) {
                if (roomOrder.getOrderStatus() == OrderStatus.CONFIRMED)
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
                .orElseThrow(() -> new ApiFailException("Room order not found"));

        if (roomOrder.getIsDeleted()){
            throw new ApiFailException("Room order is not found or deleted");
        }

        return roomOrder;
    }

    private RoomOrder initAndGetRoomOrder(RoomOrderModel roomOrderModel){
        RoomOrder roomOrder = new RoomOrder();

        checkRoomOrderDate(roomOrderModel);

        User user = userService.getById(roomOrderModel.getUserId());
        roomOrder.setUser(user);
        Room room = roomRepository.findById(roomOrderModel.getRoomId())
                .orElseThrow(() -> new ApiFailException("room is not found"));
        roomOrder.setIsDeleted(false);
        roomOrder.setRoom(room);
        roomOrder.setUserFullName(user.getFirstName() + " " + user.getLastName());

        Float price = room.getRoomType().getPrice();
        Date startDate = roomOrderModel.getStartDate();
        Date endDate = roomOrderModel.getEndDate();

        roomOrder.setStartDate(startDate);
        roomOrder.setEndDate(endDate);
        Date expirationDate = new Date();
        expirationDate.setDate(startDate.getDate() + 3);
        roomOrder.setExpirationDate(expirationDate);
        roomOrder.setTotalPrice(getTotalPrice(price, startDate, endDate));
        roomOrder.setOrderStatus(OrderStatus.IN_PROCESS);

        return roomOrder;
    }

    private Float getTotalPrice(Float price, Date startDate, Date endDate){
        return (endDate.getDate() - startDate.getDate()) * price;
    }

    private void checkRoomOrderDate(RoomOrderModel roomOrderModel){
        List<RoomOrder> roomOrders = roomOrderRepository.findAll();

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
            throw new ApiFailException("dates must not be null");
        }

        if (endDate.compareTo(startDate) <= 0){
            throw new ApiFailException("endDate must be greater than startDate");
        }

        if (startDate.compareTo(currentDate) < 0){
            throw new ApiFailException("start date can not be less than currentDate");
        }


        for (RoomOrder roomOrder : roomOrders){
            if (roomOrderModel.getRoomId() == roomOrder.getRoom().getId()){
                if (roomOrder.getOrderStatus() == OrderStatus.CONFIRMED){
                    Date rSDate = roomOrder.getStartDate();
                    Date rEDate = roomOrder.getEndDate();
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

    private RoomOrderModel toModel(RoomOrder roomOrder){
        RoomOrderModel roomOrderModel = new RoomOrderModel();
        roomOrderModel.setUserId(roomOrder.getUser().getId());
        roomOrderModel.setRoomId(roomOrder.getId());
        roomOrderModel.setStartDate(roomOrder.getStartDate());
        roomOrderModel.setEndDate(roomOrder.getEndDate());
        roomOrderModel.setUserFullName(roomOrder.getUserFullName());
        roomOrderModel.setOrderStatus(roomOrder.getOrderStatus());
        roomOrderModel.setHotelName(roomOrder.getRoom().getRoomType().getHotel().getHotelName());

        return roomOrderModel;
    }

}
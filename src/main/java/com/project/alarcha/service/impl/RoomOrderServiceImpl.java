package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.repositories.RoomOrderRepository;
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
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Override
    public RoomOrderModel order(RoomOrderModel roomOrderModel) {
        roomOrderRepository.save(initAndGetRoomOrder(roomOrderModel));
        return roomOrderModel;
    }

    @Override
    public RoomOrderModel acceptOrder(Long orderId) {
        RoomOrder roomOrder = roomOrderRepository.getById(orderId);

        if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            roomOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel declineOrder(Long orderId) {
        RoomOrder roomOrder = roomOrderRepository.getById(orderId);

        if (roomOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            roomOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    @Override
    public RoomOrderModel deleteOrder(Long id) {
        RoomOrder roomOrder = roomOrderRepository.getById(id);
        if (roomOrder != null){
            if (roomOrder.getIsDeleted()){
                throw new ApiFailException("Room is already deleted");
            }
            roomOrder.setIsDeleted(true);
        }

        roomOrderRepository.save(roomOrder);

        return toModel(roomOrder);
    }

    @Override
    public List<RoomOrderModel> getAll() {
        List<RoomOrderModel> roomOrderModels = new ArrayList<>();

        for (RoomOrder roomOrder : roomOrderRepository.findAll()){
            if (!roomOrder.getIsDeleted()){
                roomOrderModels.add(toModel(roomOrder));
            }
        }
        return roomOrderModels;
    }

    @Override
    public RoomOrderModel getById(Long id) {
        RoomOrder roomOrder = roomOrderRepository.getById(id);

        if (roomOrder == null){
            throw new ApiFailException("Room order not found");
        }

        if (roomOrder.getIsDeleted()){
            throw new ApiFailException("Room order is deleted");
        }

        return toModel(roomOrder);
    }

    private RoomOrder initAndGetRoomOrder(RoomOrderModel roomOrderModel){
        RoomOrder roomOrder = new RoomOrder();

        checkRoomOrderDate(roomOrderModel);

        User user = userService.getById(roomOrderModel.getUserId());
        roomOrder.setUser(user);
        Room room = roomService.getByRoomId(roomOrderModel.getRoomId());
        roomOrder.setIsDeleted(false);
        roomOrder.setRoom(room);
        roomOrder.setHotelName(room.getHotelName());
        roomOrder.setUserFullName(user.getFirstName() + " " + user.getLastName());

        Float price = room.getRoomType().getPrice();
        Date startDate = roomOrderModel.getStartDate();
        Date endDate = roomOrderModel.getEndDate();

        roomOrder.setStartDate(startDate);
        roomOrder.setEndDate(endDate);
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

        return roomOrderModel;
    }
}
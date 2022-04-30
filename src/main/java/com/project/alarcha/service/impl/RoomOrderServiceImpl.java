package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.enums.RoomOrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.repositories.RoomOrderRepository;
import com.project.alarcha.service.RoomOrderService;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        if (roomOrder.getRoomOrderStatus() == RoomOrderStatus.IN_PROCESS){
            roomOrder.setRoomOrderStatus(RoomOrderStatus.CONFIRMED);
        }

        roomOrderRepository.save(roomOrder);
        return toModel(roomOrder);
    }

    private RoomOrder initAndGetRoomOrder(RoomOrderModel roomOrderModel){
        RoomOrder roomOrder = new RoomOrder();

        checkRoomOrderDate(roomOrderModel);

        roomOrder.setUser(userService.getById(roomOrderModel.getUserId()));
        Room room = roomService.getByRoomId(roomOrderModel.getRoomId());
        roomOrder.setIsDeleted(false);
        roomOrder.setRoom(room);
        roomOrder.setHotelName(room.getHotelName());
        roomOrder.setStartDate(roomOrderModel.getStartDate());
        roomOrder.setEndDate(roomOrderModel.getEndDate());
        roomOrder.setRoomOrderStatus(RoomOrderStatus.IN_PROCESS);

        return roomOrder;
    }

    private void checkRoomOrderDate(RoomOrderModel roomOrderModel){
        List<RoomOrder> roomOrders = roomOrderRepository.findAll();

        Date startDate = roomOrderModel.getStartDate();
        Date endDate = roomOrderModel.getEndDate();

        for (RoomOrder roomOrder : roomOrders){
            if (roomOrderModel.getRoomId() == roomOrder.getRoom().getId()){
                if (roomOrder.getRoomOrderStatus() == RoomOrderStatus.CONFIRMED){
                    Date rSDate = roomOrder.getStartDate();
                    Date rEDate = roomOrder.getEndDate();
                    if (
                            (startDate.getYear() == rSDate.getYear() && startDate.getMonth() == rSDate.getMonth())
                                    && (endDate.getYear() == rEDate.getYear() && endDate.getMonth() == rEDate.getMonth())
                    )
                    {
                        for (int i = startDate.getDay(); i < endDate.getDay(); i++){
                            if (i >= rSDate.getDay() && i <= rEDate.getDay()){
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

        return roomOrderModel;
    }
}
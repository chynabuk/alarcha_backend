package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.entities.Object;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.repositories.*;
import com.project.alarcha.service.Population;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class PopulationImpl implements Population {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomOrderRepository roomOrderRepository;

    @Autowired
    private HotelHallsRepository hotelHallsRepository;

    @Autowired
    private HotelHallOrderRepository hotelHallOrderRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ObjectOrderRepository objectOrderRepository;

    @Override
    public Object create() {
        User user = userRepository.getById(5L);
        User user1 = userRepository.getById(7L);

        Room room = roomRepository.getById(1L);
        HotelHall hotelHall = hotelHallsRepository.getById(1L);
        Object object = objectRepository.getById(1L);

        OrderStatus orderStatus[] = {OrderStatus.CONFIRMED, OrderStatus.DECLINED, OrderStatus.PAID, OrderStatus.IN_PROCESS};

        Random random = new Random();

        for (int i = 0; i < 1000000; i++){
            int orderStatusIndex = random.nextInt(4);

            User tempUser = null;
            if (i % 2 == 0){
                tempUser = user;
            }
            else {
                tempUser = user1;
            }

            RoomOrder roomOrder = new RoomOrder();
            roomOrder.setOrderStatus(orderStatus[orderStatusIndex]);
            Date startDate = new Date();
            roomOrder.setStartDate(startDate);
            Date endDate = new Date();
            endDate.setDate(endDate.getDate() + 3);
            roomOrder.setEndDate(endDate);
            roomOrder.setIsDeleted(false);
            roomOrder.setTotalPrice(2000F);
            roomOrder.setUserFullName(tempUser.getFirstName() + " " + tempUser.getLastName());
            roomOrder.setExpirationDate(endDate);
            roomOrder.setRoom(room);
            roomOrder.setUser(tempUser);

            HotelHallOrder hotelHallOrder = new HotelHallOrder();
            hotelHallOrder.setIsDeleted(false);
            hotelHallOrder.setOrderStatus(orderStatus[orderStatusIndex]);
            hotelHallOrder.setStartDate(startDate);
            hotelHallOrder.setExpirationDate(endDate);
            hotelHallOrder.setEndDate(startDate);

            LocalTime localTime = LocalTime.now();
            Time startTime = Time.valueOf(localTime);
            hotelHallOrder.setStartTime(startTime);

            localTime.plusHours(5);
            Time endTime = Time.valueOf(localTime);
            hotelHallOrder.setEndTime(endTime);
            hotelHallOrder.setTotalPrice(2000F);
            hotelHallOrder.setHotelHall(hotelHall);
            hotelHallOrder.setUser(tempUser);

            ObjectOrder objectOrder = new ObjectOrder();
            objectOrder.setStartDate(startDate);
            objectOrder.setEndDate(endDate);
            objectOrder.setStartTime(startTime);
            objectOrder.setEndTime(endTime);
            objectOrder.setIsDeleted(false);
            objectOrder.setTotalPrice(2000F);
            objectOrder.setOrderStatus(orderStatus[orderStatusIndex]);
            objectOrder.setFullName(tempUser.getFirstName() + " " + tempUser.getLastName());
            objectOrder.setObject(object);
            objectOrder.setUser(tempUser);

            roomOrderRepository.save(roomOrder);

            hotelHallOrderRepository.save(hotelHallOrder);

            objectOrderRepository.save(objectOrder);
        }
        return null;
    }
}

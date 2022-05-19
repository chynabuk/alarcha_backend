package com.project.alarcha.service.impl;

import com.project.alarcha.entities.BaseCheck;
import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.repositories.BaseCheckRepository;
import com.project.alarcha.repositories.HotelHallOrderRepository;
import com.project.alarcha.repositories.ObjectOrderRepository;
import com.project.alarcha.repositories.RoomOrderRepository;
import com.project.alarcha.service.BaseCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BaseCheckServiceImpl implements BaseCheckService {
    @Autowired
    private BaseCheckRepository baseCheckRepository;

    @Autowired
    private RoomOrderRepository roomOrderRepository;

    @Autowired
    private ObjectOrderRepository objectOrderRepository;

    @Autowired
    private HotelHallOrderRepository hotelHallOrderRepository;

    @Override
    public void checkOrders() {
        if (baseCheckRepository.count() > 0){
            BaseCheck baseCheck = baseCheckRepository.getLastRecord();
            Date nextCheckDay = baseCheck.getNextCheckDay();
            Date currentDate = new Date();
            if (currentDate.getHours() == 23 && currentDate.getMinutes() > 30){
                if (nextCheckDay.after(currentDate)){
                    checkRoomOrders();
                    checkObjectOrders();
                    checkHotelHallOrders();
                }

                Date newNextCheckDay = new Date();
                newNextCheckDay.setDate(nextCheckDay.getDate() + 7);

                BaseCheck newBaseCheck = new BaseCheck();
                baseCheck.setCheckedDay(nextCheckDay);
                baseCheck.setNextCheckDay(newNextCheckDay);

                baseCheckRepository.save(newBaseCheck);
            }
        }
        else {
            Date currentDate = new Date();
            if (currentDate.getHours() == 23 && currentDate.getMinutes() > 30){
                checkRoomOrders();
                checkObjectOrders();
                checkHotelHallOrders();

                BaseCheck newBaseCheck = new BaseCheck();
                newBaseCheck.setCheckedDay(currentDate);

                Date nextCheckDay = new Date();
                nextCheckDay.setDate(currentDate.getDate() + 7);

                baseCheckRepository.save(newBaseCheck);
            }
        }
    }

    private void checkRoomOrders(){
        List<RoomOrder> roomOrders = roomOrderRepository.getAllRoomOrders();

        for (RoomOrder roomOrder : roomOrders){
            if (isExpired(roomOrder.getExpirationDate())){
                roomOrder.setIsDeleted(true);
                roomOrderRepository.save(roomOrder);
            }
        }
    }

    private void checkObjectOrders(){
        List<ObjectOrder> objectOrders = objectOrderRepository.getAllObjectOrders();

        for (ObjectOrder objectOrder : objectOrders){
            if (isExpired(objectOrder.getExpirationDate())){
                objectOrder.setIsDeleted(true);
                objectOrderRepository.save(objectOrder);
            }
        }
    }

    private void checkHotelHallOrders(){
        List<HotelHallOrder> hotelHallOrders = hotelHallOrderRepository.getAllHotelHallOrders();

        for (HotelHallOrder hotelHallOrder : hotelHallOrders){
            if (isExpired(hotelHallOrder.getExpirationDate())){
                hotelHallOrder.setIsDeleted(true);
                hotelHallOrderRepository.save(hotelHallOrder);
            }
        }
    }

    private boolean isExpired(Date expiredDate){
        Date currentDate = new Date();

        return currentDate.after(expiredDate);
    }
}

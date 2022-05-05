package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.repositories.HotelHallOrderRepository;
import com.project.alarcha.service.HotelHallOrderService;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HotelHallOrderServiceImpl implements HotelHallOrderService {
    @Autowired
    private HotelHallOrderRepository hotelHallOrderRepository;

    @Autowired
    private HotelHallService hotelHallService;

    @Autowired
    private UserService userService;

    @Override
    public HotelHallOrderModel order(HotelHallOrderModel hotelHallOrderModel) {
        hotelHallOrderRepository.save(initAndGetHotelHallOrder(hotelHallOrderModel));
        return hotelHallOrderModel;
    }

    @Override
    public HotelHallOrderModel acceptOrder(Long orderId) {
        HotelHallOrder hotelHallOrder = hotelHallOrderRepository.getById(orderId);

        if(hotelHallOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            hotelHallOrder.setOrderStatus(OrderStatus.CONFIRMED);
        }

        hotelHallOrderRepository.save(hotelHallOrder);
        return toModel(hotelHallOrder);
    }

    @Override
    public HotelHallOrderModel declineOrder(Long orderId) {
        HotelHallOrder hotelHallOrder = hotelHallOrderRepository.getById(orderId);

        if(hotelHallOrder.getOrderStatus() == OrderStatus.IN_PROCESS){
            hotelHallOrder.setOrderStatus(OrderStatus.DECLINED);
        }

        hotelHallOrderRepository.save(hotelHallOrder);
        return toModel(hotelHallOrder);
    }

    @Override
    public HotelHallOrderModel deleteOrder(Long id) {
        HotelHallOrder hotelHallOrder = hotelHallOrderRepository.getById(id);
        if (hotelHallOrder != null){
            if (hotelHallOrder.getIsDeleted()){
                throw new ApiFailException("Room is already deleted");
            }
            hotelHallOrder.setIsDeleted(true);
        }

        hotelHallOrderRepository.save(hotelHallOrder);

        return toModel(hotelHallOrder);
    }

    @Override
    public List<HotelHallOrderModel> getAll() {
        List<HotelHallOrderModel> hotelHallOrderModels = new ArrayList<>();

        for (HotelHallOrder hotelHallOrder : hotelHallOrderRepository.findAll()){
            if (!hotelHallOrder.getIsDeleted()){
                hotelHallOrderModels.add(toModel(hotelHallOrder));
            }
        }
        return hotelHallOrderModels;
    }

    @Override
    public HotelHallOrderModel getById(Long id) {
        HotelHallOrder hotelHallOrder = hotelHallOrderRepository.getById(id);

        if (hotelHallOrder == null){
            throw new ApiFailException("HotelHall order not found");
        }

        if (hotelHallOrder.getIsDeleted()){
            throw new ApiFailException("HotelHall order is deleted");
        }

        return toModel(hotelHallOrder);
    }

    private HotelHallOrder initAndGetHotelHallOrder(HotelHallOrderModel hotelHallOrderModel){
        HotelHallOrder hotelHallOrder = new HotelHallOrder();

        checkHotelHallOrderTime(hotelHallOrderModel);

        hotelHallOrder.setUser(userService.getById(hotelHallOrderModel.getUserId()));
        hotelHallOrder.setHotelHall(hotelHallService.getHotelHallById(hotelHallOrderModel.getHotelHallId()));
        hotelHallOrder.setRegistration_date(hotelHallOrderModel.getRegistrationDate());
        hotelHallOrder.setStartTime(hotelHallOrderModel.getStartTime());
        hotelHallOrder.setEndTime(hotelHallOrderModel.getEndTime());
        hotelHallOrder.setOrderStatus(OrderStatus.IN_PROCESS);
        hotelHallOrder.setIsDeleted(false);

        return hotelHallOrder;
    }

    private void checkHotelHallOrderTime(HotelHallOrderModel hotelHallOrderModel){
        List<HotelHallOrder> hotelHallOrderList = hotelHallOrderRepository.findAll();

        Time startTime = hotelHallOrderModel.getStartTime();
        Time endTime = hotelHallOrderModel.getEndTime();
        Date registrationDate = hotelHallOrderModel.getRegistrationDate();

        for(HotelHallOrder hotelHallOrder : hotelHallOrderList){
            if(hotelHallOrderModel.getHotelHallId() == hotelHallOrder.getHotelHall().getId()){
                if( (registrationDate.getYear() == hotelHallOrder.getRegistration_date().getYear() && registrationDate.getMonth() == hotelHallOrder.getRegistration_date().getMonth()
                        && registrationDate.getDay() == hotelHallOrder.getRegistration_date().getDay())
                ){
                    if(hotelHallOrder.getOrderStatus() == OrderStatus.CONFIRMED){
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

    private HotelHallOrderModel toModel(HotelHallOrder hotelHallOrder){
        HotelHallOrderModel hotelHallOrderModel = new HotelHallOrderModel();
        hotelHallOrderModel.setHotelHallId(hotelHallOrder.getId());
        hotelHallOrderModel.setStartTime(hotelHallOrder.getStartTime());
        hotelHallOrderModel.setEndTime(hotelHallOrder.getEndTime());
        hotelHallOrderModel.setRegistrationDate(hotelHallOrder.getRegistration_date());
        hotelHallOrderModel.setUserId(hotelHallOrder.getUser().getId());

        return hotelHallOrderModel;
    }
}

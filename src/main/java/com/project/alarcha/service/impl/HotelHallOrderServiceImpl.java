package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.repositories.HotelHallOrderRepository;
import com.project.alarcha.service.HotelHallOrderService;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private HotelHallService hotelHallService;

    @Autowired
    private UserService userService;

    @Override
    public HotelHallOrderModel order(HotelHallOrderModel hotelHallOrderModel) {
        HotelHallOrder hotelHallOrder = initAndGetHotelHallOrder(hotelHallOrderModel);
        hotelHallOrderRepository.save(hotelHallOrder);
        return toModel(hotelHallOrder);
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
        HotelHallOrder hotelHallOrder = getHotelHallOrder(id);

        hotelHallOrderRepository.save(hotelHallOrder);

        return toModel(hotelHallOrder);
    }

    @Override
    public List<HotelHallOrderModel> getAll() {
        List<HotelHallOrderModel> hotelHallOrderModels = new ArrayList<>();

        for (HotelHallOrder hotelHallOrder : hotelHallOrderRepository.findAll()){
            if (!hotelHallOrder.getIsDeleted()){
                if (isExpired(hotelHallOrder.getExpirationDate())){
                    hotelHallOrder.setIsDeleted(true);
                    hotelHallOrderRepository.save(hotelHallOrder);
                }
            }
            if (!hotelHallOrder.getIsDeleted()){
                hotelHallOrderModels.add(toModel(hotelHallOrder));
            }
        }
        return hotelHallOrderModels;
    }

    @Override
    public HotelHallOrderModel getById(Long id) {
        HotelHallOrder hotelHallOrder = getHotelHallOrder(id);

        return toModel(hotelHallOrder);
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

        HotelHall hotelHall = hotelHallService.getHotelHallById(hotelHallOrderModel.getHotelHallId());
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

        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        Date currentDate = new Date();
        currentDate.setTime(0);

        Date tempDate = new Date();

        Time currentTime = Time.valueOf(LocalTime.now());


        currentDate.setYear(tempDate.getYear());
        currentDate.setMonth(tempDate.getMonth());
        currentDate.setDate(tempDate.getDate());
        currentDate.setHours(0);
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


    private Float getTotalPrice(Float price, Float priceForNextHours, HotelHallOrderModel hotelHallOrderModel){
        Time startTime = hotelHallOrderModel.getStartTime();
        Time endTime = hotelHallOrderModel.getEndTime();

        Integer hours = endTime.getHours() - startTime.getHours();
        if (hours <= 0){
            throw new ApiFailException("can not get totalPrice");
        }

        if (hours > 5){
            return 6000F;
        }
        else {
            return price + (hours - 1) * priceForNextHours;
        }
    }

    private HotelHallOrderModel toModel(HotelHallOrder hotelHallOrder){
        HotelHallOrderModel hotelHallOrderModel = new HotelHallOrderModel();
        hotelHallOrderModel.setHotelHallId(hotelHallOrder.getId());
        hotelHallOrderModel.setStartTime(hotelHallOrder.getStartTime());
        hotelHallOrderModel.setEndTime(hotelHallOrder.getEndTime());
        hotelHallOrderModel.setStartDate(hotelHallOrder.getStartDate());
        hotelHallOrderModel.setUserId(hotelHallOrder.getUser().getId());
        hotelHallOrderModel.setUserFullName(hotelHallOrder.getUserFullName());
        hotelHallOrderModel.setOrderStatus(hotelHallOrder.getOrderStatus());

        return hotelHallOrderModel;
    }
}

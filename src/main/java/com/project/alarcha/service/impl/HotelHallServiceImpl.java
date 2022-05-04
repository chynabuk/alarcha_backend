package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.repositories.HotelHallsRepository;
import com.project.alarcha.repositories.HotelRepository;
import com.project.alarcha.service.HotelHallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HotelHallServiceImpl implements HotelHallService {
    @Autowired
    private HotelHallsRepository hotelHallsRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<HotelHall> createHotelHalls(List<HotelHallModel> hotelHallModelModels) {
        List<HotelHall> hotelHalls = new ArrayList<>();

        for (HotelHallModel hotelHallModel : hotelHallModelModels){
            HotelHall hotelHall = new HotelHall();
            hotelHall.setName(hotelHallModel.getName());
            hotelHall.setPrice(hotelHallModel.getPrice());
            hotelHall.setIsDeleted(false);

            hotelHalls.add(hotelHall);
        }
        return hotelHalls;
    }

    @Override
    public HotelHallModel createHotelHall(HotelHallModel hotelHallModel) {
        HotelHall hotelHall = new HotelHall();
        hotelHall.setName(hotelHallModel.getName());
        hotelHall.setPrice(hotelHallModel.getPrice());
        hotelHall.setFinalPrice(hotelHallModel.getFinalPrice());
        hotelHall.setNumberOfSeats(hotelHallModel.getNumberOfSeats());
        hotelHall.setHotel(hotelRepository.getById(hotelHallModel.getHotelId()));
        hotelHall.setIsDeleted(false);

        hotelHallsRepository.save(hotelHall);
        return hotelHallModel;
    }

    @Override
    public List<HotelHall> convertToHotelHalls(List<HotelHallModel> hotelHallModelModels) {
//        List<HotelHall> hotelHalls = new ArrayList<>();
//
//        for (HotelHallModel hotelHallModel : hotelHallModels){
//            HotelHall hotelHall = new HotelHall();
//            hotelHall.setName(hotelHallModel.getName());
//            hotelHall.setPrice(hotelHallModel.getPrice());
//
//            hotelHalls.add(hotelHall);
//        }
        return null;
    }

    @Override
    public HotelHallModel getById(Long id) {
        HotelHall hotelHall = hotelHallsRepository.getById(id);

        return toModel(hotelHall);
    }

    @Override
    public HotelHall getHotelHallById(long id) {
        return hotelHallsRepository.getById(id);
    }

    @Override
    public List<HotelHallModel> getAll() {
        List<HotelHallModel> hotelHallModelModels = new ArrayList<>();

        for (HotelHall hotelHall : hotelHallsRepository.findAll()){
            if (!hotelHall.getIsDeleted()){
                hotelHallModelModels.add(toModel(hotelHall));
            }

        }
        return hotelHallModelModels;
    }

    @Override
    public HotelHallModel updateHotelHall(HotelHallModel hotelHallModel) {
        HotelHall hotel = hotelHallsRepository.getById(hotelHallModel.getId());

        if (hotel != null){
            setValuesOnUpdateHotelHall(hotel, hotelHallModel);
        }

        hotelHallsRepository.save(hotel);

        return hotelHallModel;
    }

    @Override
    public HotelHallModel deleteHotelHall(Long id) {
        HotelHall hotelHall = hotelHallsRepository.getById(id);

        if (hotelHall != null){
            if (hotelHall.getIsDeleted()){
                throw new ApiFailException("Hotell hall is already deleted");
            }
            hotelHall.setIsDeleted(true);
        }

        hotelHallsRepository.save(hotelHall);

        return toModel(hotelHall);
    }

    private void setValuesOnUpdateHotelHall(HotelHall hotelHall, HotelHallModel hotelHallModel){
        hotelHall.setName(hotelHallModel.getName());
        hotelHall.setPrice(hotelHallModel.getPrice());
    }

    private HotelHallModel toModel(HotelHall hotelHall){
        HotelHallModel hotelHallModel = new HotelHallModel();
        hotelHallModel.setId(hotelHall.getId());
        hotelHallModel.setName(hotelHall.getName());
        hotelHallModel.setPrice(hotelHall.getPrice());
        hotelHallModel.setHotelName(hotelHall.getHotel().getHotelName());

        return hotelHallModel;
    }
}

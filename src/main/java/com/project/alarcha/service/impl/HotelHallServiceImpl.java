package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.entities.HotelHall_IMG;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.repositories.HotelHallsRepository;
import com.project.alarcha.repositories.HotelRepository;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.service.HotelHall_ImgService;
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

    @Autowired
    private HotelHall_ImgService hotelHall_imgService;

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
        hotelHallsRepository.save(initAndGet(hotelHallModel));

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
    public List<HotelHallModel> convertToModels(List<HotelHall> hotelHalls) {
        List<HotelHallModel> hotelHallModelModels = new ArrayList<>();

        for (HotelHall hotelHall : hotelHalls){
            if (!hotelHall.getIsDeleted()){
                hotelHallModelModels.add(toModel(hotelHall));
            }

        }
        return hotelHallModelModels;
    }

    @Override
    public HotelHallModel getById(Long id) {
        HotelHall hotelHall = getHotelHall(id);

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
    public List<HotelHallModel> getForList() {
        List<HotelHallModel> hotelHallModelModels = new ArrayList<>();

        for (HotelHall hotelHall : hotelHallsRepository.findAll()){
            if (!hotelHall.getIsDeleted()){
                hotelHall.setHotelHallImages(null);
                hotelHallModelModels.add(toModel(hotelHall));
            }

        }
        return hotelHallModelModels;
    }

    @Override
    public HotelHallModel updateHotelHall(HotelHallModel hotelHallModel) {
        HotelHall hotel = getHotelHall(hotelHallModel.getId());
        setValuesOnUpdateHotelHall(hotel, hotelHallModel);

        hotelHallsRepository.save(hotel);

        return hotelHallModel;
    }

    @Override
    public HotelHallModel deleteHotelHall(Long id) {
        HotelHall hotelHall = getHotelHall(id);

        hotelHall.setIsDeleted(true);

        for (HotelHallOrder hotelHallOrder : hotelHall.getHotelHallOrders()){
            if (!hotelHallOrder.getIsDeleted()){
                hotelHallOrder.setIsDeleted(true);
            }
        }

        for (HotelHall_IMG hotelHall_img : hotelHall.getHotelHallImages()){
            if (!hotelHall_img.getIsDeleted()){
                hotelHall_img.setIsDeleted(true);
            }
        }

        hotelHallsRepository.save(hotelHall);

        return toModel(hotelHall);
    }

    private HotelHall getHotelHall(Long id){
        HotelHall hotelHall = hotelHallsRepository.getById(id);

        if (hotelHall != null) {
            throw new ApiFailException("Hotel hall is not found");
        }
        if (hotelHall.getIsDeleted()){
            throw new ApiFailException("Hotel hall is not found or deleted");
        }

        return hotelHall;
    }

    private HotelHall initAndGet(HotelHallModel hotelHallModel){
        HotelHall hotelHall = new HotelHall();
        hotelHall.setName(hotelHallModel.getName());
        hotelHall.setPrice(hotelHallModel.getPrice());
        hotelHall.setPriceForNextHours(hotelHallModel.getPriceForNextHours());
        hotelHall.setNumberOfSeats(hotelHallModel.getNumberOfSeats());
        hotelHall.setHotel(hotelRepository.getById(hotelHallModel.getHotelId()));
        hotelHall.setIsDeleted(false);

        if (hotelHallModel.getHotelHall_imgModels() != null){
            List<HotelHall_IMG> hotelHall_imgs = hotelHall_imgService.uploadImages(hotelHallModel.getHotelHall_imgModels());

            hotelHall.setHotelHallImages(hotelHall_imgs);

            hotelHall_imgs.forEach(hotelHall_img -> hotelHall_img.setHotelHall(hotelHall));

        }

        return hotelHall;
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
        hotelHallModel.setPriceForNextHours(hotelHall.getPriceForNextHours());
        hotelHallModel.setHotelName(hotelHall.getHotel().getHotelName());
        if (hotelHall.getHotelHallImages() != null){
            hotelHallModel.setHotelHall_imgModels(hotelHall_imgService.convertToModels(hotelHall.getHotelHallImages()));
        }
        hotelHallModel.setNumberOfSeats(hotelHall.getNumberOfSeats());

        return hotelHallModel;
    }
}

package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Hotel_Img;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.Hotel_ImgModel;
import com.project.alarcha.repositories.Hotel_ImgRepository;
import com.project.alarcha.service.Hotel_ImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class Hotel_ImgServiceImpl implements Hotel_ImgService {
    @Autowired
    private Hotel_ImgRepository hotel_imgRepository;

    @Override
    public List<Hotel_Img> uploadImages(List<Hotel_ImgModel> hotelImgModels) {
        List<Hotel_Img> hotelImgs = new ArrayList<>();

        for (Hotel_ImgModel hotelImgModel : hotelImgModels){
            Hotel_Img hallImg = new Hotel_Img();
            hallImg.setImg(hotelImgModel.getImg().getBytes(StandardCharsets.UTF_8));
            hallImg.setIsDeleted(false);

            hotelImgs.add(hallImg);
        }

        return hotelImgs;
    }

    @Override
    public List<Hotel_ImgModel> convertToModels(List<Hotel_Img> hotelImgs) {
        List<Hotel_ImgModel> hotelImgModels = new ArrayList<>();

        hotelImgs.forEach(hotelImg -> hotelImgModels.add(toModel(hotelImg)));

        return hotelImgModels;
    }

    @Override
    public Hotel_ImgModel deleteImage(Long id) {
        Hotel_Img hotelImg = hotel_imgRepository.getById(id);

        if (hotelImg == null){
            throw new ApiFailException("Hotel_Img не найден.");
        }

        if (hotelImg.getIsDeleted()){
            throw new ApiFailException("Hotel_Img уже удален.");
        }

        hotelImg.setIsDeleted(true);

        hotel_imgRepository.save(hotelImg);

        return toModel(hotelImg);
    }

    @Override
    public Hotel_ImgModel updateImage(Long id) {
        return null;
    }

    private Hotel_ImgModel toModel(Hotel_Img hotelImg){
        Hotel_ImgModel hallImgModel = new Hotel_ImgModel();
        hallImgModel.setId(hotelImg.getId());
        hallImgModel.setImg(new String(hotelImg.getImg(), StandardCharsets.UTF_8));
        hallImgModel.setHotelId(hotelImg.getId());

        return hallImgModel;
    }
}

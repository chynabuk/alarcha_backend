package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHall_IMG;
import com.project.alarcha.entities.RoomTypeImage;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHall_ImgModel;
import com.project.alarcha.models.RoomModel.RoomTypeImageModel;
import com.project.alarcha.repositories.HotelHall_ImgRepository;
import com.project.alarcha.service.HotelHall_ImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HotelHall_ImgServiceImpl implements HotelHall_ImgService {
    @Autowired
    private HotelHall_ImgRepository hotelHall_imgRepository;

    @Override
    public List<HotelHall_IMG> uploadImages(List<HotelHall_ImgModel> hotelHall_ImgModels) {
        List<HotelHall_IMG> hotelHall_imgs = new ArrayList<>();

        for (HotelHall_ImgModel hotelHall_imgModel : hotelHall_ImgModels){
            HotelHall_IMG hotelHall_img = new HotelHall_IMG();
            hotelHall_img.setImg(hotelHall_imgModel.getImg().getBytes(StandardCharsets.UTF_8));
            hotelHall_img.setIsDeleted(false);

            hotelHall_imgs.add(hotelHall_img);
        }

        return hotelHall_imgs;
    }

    @Override
    public List<HotelHall_ImgModel> convertToModels(List<HotelHall_IMG> hotelHall_imgs) {
        List<HotelHall_ImgModel> hotelHall_imgModels = new ArrayList<>();

        hotelHall_imgs.forEach(hotelHall_img -> hotelHall_imgModels.add(toModel(hotelHall_img)));

        return hotelHall_imgModels;
    }

    @Override
    public HotelHall_ImgModel deleteImage(Long id) {
        HotelHall_IMG hotelHall_img = hotelHall_imgRepository.getById(id);

        if (hotelHall_img == null){
            throw new ApiFailException("RoomTypeImage is not found");
        }

        if (hotelHall_img.getIsDeleted()){
            throw new ApiFailException("RoomTypeImage is already deleted");
        }

        hotelHall_img.setIsDeleted(true);

        hotelHall_imgRepository.save(hotelHall_img);
        return toModel(hotelHall_img);
    }

    @Override
    public HotelHall_ImgModel updateImage(Long id) {
        return null;
    }

    private HotelHall_ImgModel toModel(HotelHall_IMG hotelHall_img){
        HotelHall_ImgModel hotelHall_imgModel = new HotelHall_ImgModel();
        hotelHall_imgModel.setId(hotelHall_img.getId());
        hotelHall_imgModel.setImg(new String(hotelHall_img.getImg(), StandardCharsets.UTF_8));
        hotelHall_imgModel.setHotelHallId(hotelHall_img.getHotelHall().getId());

        return hotelHall_imgModel;
    }
}

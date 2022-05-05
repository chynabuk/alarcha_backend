package com.project.alarcha.service;

import com.project.alarcha.entities.HotelHall_IMG;
import com.project.alarcha.models.HotelModel.HotelHall_ImgModel;

import java.util.List;

public interface HotelHall_ImgService {
    List<HotelHall_IMG> uploadImages(List<HotelHall_ImgModel> hotelHall_ImgModels);
    HotelHall_ImgModel deleteImage(Long id);
    HotelHall_ImgModel updateImage(Long id);
}

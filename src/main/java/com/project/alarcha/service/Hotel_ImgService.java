package com.project.alarcha.service;

import com.project.alarcha.entities.Hotel_Img;
import com.project.alarcha.models.HotelModel.Hotel_ImgModel;

import java.util.List;

public interface Hotel_ImgService {
    List<Hotel_Img> uploadImages(List<Hotel_ImgModel> hotelImgModels);
    List<Hotel_ImgModel> convertToModels(List<Hotel_Img> hotelImgs);
    Hotel_ImgModel deleteImage(Long id);
    Hotel_ImgModel updateImage(Long id);
}

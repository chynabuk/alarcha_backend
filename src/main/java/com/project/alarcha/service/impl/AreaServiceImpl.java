package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.enums.UserRole;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.repositories.AreaRepository;
import com.project.alarcha.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Override
    public AreaModel createArea(AreaModel areaCreateModel) {
        Area area = new Area();

        areaRepository.save(initAndGet(area, areaCreateModel));

        return areaCreateModel;
    }

    @Override
    public AreaModel updateArea(AreaModel areaModel) {
        Area area = getAreaById(areaModel.getId());
        if (area != null){
            if (!area.getIsDeleted()){
                setValuesOnUpdateArea(area, areaModel);
            }
        }
        areaRepository.save(area);

        return areaModel;
    }

    @Override
    public AreaModel deleteArea(Long areaId) {
        Area area = getAreaById(areaId);

        if (area != null){
            if (area.getIsDeleted()){
                throw new ApiFailException("Area is already deleted");
            }
            deleteHotelDepended(area.getHotels());

            //TODO BEKZHAN
            deleteObjectTypeDepended(area.getObjectTypes());

            area.setIsDeleted(true);


        }
        areaRepository.save(area);

        return toModel(area);
    }

    @Override
    public AreaModel getById(Long areaId) {
        Area area = getAreaById(areaId);
        if (area.getIsDeleted()){
            area = null;
        }

        return toModel(area);
    }

    @Override
    public Area getAreaById(Long areaId) {
        return areaRepository.getById(areaId);
    }

    @Override
    public List<AreaModel> getAll() {
        List<AreaModel> areaModels = new ArrayList<>();

        for (Area area : areaRepository.findAll()){
            if (!area.getIsDeleted()){
                areaModels.add(toModel(area));
            }
        }
        return areaModels;
    }



    private void deleteHotelDepended(List<Hotel> hotels){
        for (Hotel hotel : hotels){
            for (RoomType roomType : hotel.getRoomTypes()){
                for (Room room : roomType.getRooms()){
                    room.setIsDeleted(true);
                }
                roomType.setIsDeleted(true);
            }

            for (HotelHall hotelHall : hotel.getHotelHalls()){
                hotelHall.setIsDeleted(true);
            }

            hotel.setIsDeleted(true);
        }
    }

    //TODO BEKZHAN
    private void deleteObjectTypeDepended(List<ObjectType> objectTypes){
        //YOUR CODE
    }

    private Area initAndGet(Area area, AreaModel areaCreateModel){
        area.setAreaName(areaCreateModel.getAreaName());
        area.setObjectTypes(null);
        area.setHotels(null);
        area.setUser(userService.getByEmail(areaCreateModel.getEmail()));
        area.setIsDeleted(false);
        User user = userService.getByEmail(areaCreateModel.getEmail());

        area.setUser(user);

        return area;
    }

    private void setValuesOnUpdateArea(Area area, AreaModel areaModel){
        String areaName = areaModel.getAreaName();
        User user = userService.getByEmail(areaModel.getEmail());
//        List<Hotel> hotels = areaModel.getHotels();
//        List<Object> objects = areaModel.getObjects();

        if (areaName != null){
            area.setAreaName(areaName);
        }

        if (user != null){
            if (!user.getIsDeleted() && user.getUserRole() == UserRole.ADMIN){
                area.setUser(user);
            }
        }
    }

    private AreaModel toModel(Area area){
        AreaModel areaModel = new AreaModel();
        if (area != null){
            areaModel.setId(area.getId());
            areaModel.setAreaName(area.getAreaName());
            areaModel.setEmail(area.getUser().getEmail());
            areaModel.setHotelModels(hotelService.convertToModel(area.getHotels()));
            areaModel.setObjectTypeModels(objectTypeService.convertToModels(area.getObjectTypes()));
        }

        return areaModel;
    }
}